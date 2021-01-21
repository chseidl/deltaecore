package org.deltaecore.feature.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.eclipse.emf.ecore.EObject;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.ICF;
import solver.constraints.LCF;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 * <p>Concrete scoring class for getting a "latest" configuration score
 * based on</p>
 * 
 * <p>score_configuration = sum_foreachversion((importance_feature + 1) * novelty_version)</p>
 * 
 * <ul>
 * <li>importance_feature := Depth of the feature in model</li>
 * <li>novelty_version := Path length to the most recent version</li>
 * </ul>
 * 
 * <p>the smaller the score_configuration the better</p>
 * 
 * @author David Gollasch
 *
 */
public class DEConfigurationLatestScore extends DEConfigurationScore {

	public DEConfigurationLatestScore(Solver solver, DEFeatureModelEncoderAndDecoder encoderAndDecoder) {
		super(solver, encoderAndDecoder);
	}

	@Override
	public List<IntVar<?>> encodeScore() {
		/*
		 * Principle:
		 * ==========
		 * The optimization objective will be totalscore.
		 * 
		 * totalscore = featurescore1 + featurescore2 + ... (for each feature)
		 * 
		 * Constraint for each feature and version:
		 * if (feature1 == version11) then (featurescore1 == version11score)
		 * if (feature1 == version12) then (featurescore1 == version12score)
		 * ...
		 * if (feature2 == version21) then (featurescore2 == version21score)
		 * if (feature2 == version22) then (featurescore2 == version22score)
		 * ...
		 */
		
		/* 
		 * Reference:
		 * ==========
		 * Implication:		Constraint c = LCF.ifThen(Constraint IF, Constraint THEN)
		 * Integer-Sum:		Constraint c = ICF.sum(IntVar[] VARS, IntVar SUM)
		 * Integer-Equals:	Constraint c = ICF.arithm(IntVar VAR1, "=", IntVar VAR2)
		 *
		 * Constants:		IntVar<?> i = VariableFactory.fixed("seven", 7, solver)
		 * FullInt Vars:	IntVar<?> i = VariableFactory.bounded("name", Integer.MIN_VALUE, Integer.MAX_VALUE, solver)
		 */
		
		// STEP 1. initially create objective(s) (in this case only one to later refer to sum/totalscore)
		IntVar<?> objective = null;
		
		// STEP 2. extract all features and versions of the underlying feature model
		List<DEVersion> allVersions = new ArrayList<DEVersion>();
		List<DEFeature> allFeatures = new ArrayList<DEFeature>();
		
		DEConfiguration configuration = encoderAndDecoder.decodeCurrentConfiguration();
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact artifact : configurationArtifacts) {
			if (artifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) artifact;
				DEFeature feature = featureSelection.getFeature();
				allFeatures.add(feature);
				allVersions.addAll(feature.getVersions());
			}
		}
		
		// STEP 3. score each version
		// 		   score := (importance_feature + 1) * novelty_version
		// 		   stores the scores as constants into a HashMap with versions as keys
		Map<DEVersion, IntVar<?>> versionScores = new HashMap<DEVersion, IntVar<?>>();
		for (DEVersion version : allVersions) {
			int score = scoreVersion(version);
			String name = version.getFeature().getName() + version.getFeature().getVersions().indexOf(version) + "score";
			versionScores.put(version, VariableFactory.fixed(name, score, solver));
		}
		
		// STEP 4. create a list of variables for each featurescore value and generate constraints
		List<IntVar<?>> featurescorevariables = new ArrayList<IntVar<?>>();
		for(DEFeature feature : allFeatures) {
			// STEP 4.1. create a list of variables for each featurescore value
			IntVar<?> featurescorevariable = VariableFactory.bounded(feature.getName() + "score", Integer.MIN_VALUE, Integer.MAX_VALUE, solver);
			featurescorevariables.add(featurescorevariable);
			
			// STEP 4.2. try to get the feature variable out of the solver
			IntVar<?> featurevar = null;
			for(IntVar<?> variable : solver.retrieveIntVars()) {
				if(variable.getName().equals(feature.getName())) {
					featurevar = variable;
				}
			}
			
			// STEP 4.3. if the variable can be found, generate and post the version score constraints
			//			 to specify the featurescore value; else set the variable to constant 0 to
			//			 avoid exceptions and influence on sum/totalscore
			if(featurevar != null) {
				for(DEVersion version : feature.getVersions()) {
					Constraint<?,?> ifside = ICF.arithm(featurevar, "=", feature.getVersions().indexOf(version));
					Constraint<?,?> thenside = ICF.arithm(featurescorevariable, "=", versionScores.get(version));
					Constraint<?,?> ifthen = LCF.ifThen(ifside, thenside);
					solver.post(ifthen);
				}
			} else {
				featurescorevariable = VariableFactory.fixed(0, solver);	// cannot find variable in solver.
			}
		}
		
		// STEP 5. calculate sum/totalscore and set optimization objective to sum
		IntVar<?> sum = VariableFactory.bounded("totalscore", Integer.MIN_VALUE, Integer.MAX_VALUE, solver);
		objective = sum;
		Constraint<?,?> summation = ICF.sum((IntVar<?>[])featurescorevariables.toArray(), "=", sum);
		solver.post(summation);
		
		// STEP 6. generate and return objectives list
		List<IntVar<?>> objectives = new ArrayList<IntVar<?>>();
		objectives.add(objective);
		
		return objectives;
	}
	
	/**
	 * Calculates the score for one given version.
	 * @param version The version, the score should be calculated
	 * @return The intended score
	 */
	private int scoreVersion(DEVersion version) {
		DEFeature feature = version.getFeature();
		
		int importance = getDepthInFeatureModel(feature);
		int novelty = getPathLengthToMostRecentVersion(version);
		
		int versionScore = (importance + 1) * novelty;
		
		return versionScore;
	}
	
	/**
	 * Calculates the depth of a feature in the feature tree
	 * @param feature
	 * @return Calculated depth
	 */
	private int getDepthInFeatureModel(DEFeature feature) {
		if (feature == null) {
			return 0;
		}
		
		EObject parent = feature.eContainer();
		
		if (parent == null || parent instanceof DEFeatureModel) {
			return 0;
		}
		
		DEGroup parentGroup = (DEGroup) parent;
		DEFeature parentFeature = (DEFeature) parentGroup.eContainer();
		
		return getDepthInFeatureModel(parentFeature) + 1;
	}
	
	/**
	 * Calculates the path length of a given version to the most recent 
	 * version of the same feature.
	 * @param version
	 * @return Path length
	 */
	private int getPathLengthToMostRecentVersion(DEVersion version) {
		//Shortest path to last (most recent) version
		List<DEVersion> supersedingVersions = version.getSupersedingVersions();
		
		if (supersedingVersions.isEmpty()) {
			//Reached the end.
			return 0;
		}
		
		int shortestPath = -1;
		
		for (DEVersion supersedingVersion : supersedingVersions) {
			int pathLength = getPathLengthToMostRecentVersion(supersedingVersion) + 1;
			
			if (pathLength < shortestPath || shortestPath == -1) {
				shortestPath = pathLength;
			}
		}
		
		return shortestPath;
	}

}
