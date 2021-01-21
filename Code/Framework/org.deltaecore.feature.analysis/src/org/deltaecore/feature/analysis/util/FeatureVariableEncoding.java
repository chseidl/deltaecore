package org.deltaecore.feature.analysis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.eclipse.emf.ecore.EObject;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.ICF;
import solver.constraints.LCF;
import solver.constraints.LCF2;
import solver.variables.BoolVar;
import solver.variables.VF;
import solver.variables.Variable;

public class FeatureVariableEncoding {
	private Solver solver;
	private Map<DEFeature, BoolVar<?>> featureToFeatureVariableMapping;
	
	public FeatureVariableEncoding(DEFeatureModel featureModel, Solver solver) {
		featureToFeatureVariableMapping = new HashMap<DEFeature, BoolVar<?>>();
		this.solver = solver;
		
		createFeatureVariables(featureModel);
	}
	
	private void createFeatureVariables(DEFeatureModel featureModel) {
		RandomString random = new RandomString(30);
		Iterator<EObject> iterator = featureModel.eAllContents();
		
		while (iterator.hasNext()) {
			EObject element = iterator.next();
			
			if (element instanceof DEFeature) {
				DEFeature feature = (DEFeature) element;
				String featureVariableName = random.nextString();
				
				BoolVar<?> featureVariable = VF.bool(featureVariableName, solver);
				featureToFeatureVariableMapping.put(feature, featureVariable);
			}
		}
	}
	
	public Constraint<?, ?> selected(DEFeature feature) {
		BoolVar<?> featureVariable = featureToFeatureVariableMapping.get(feature);
		return ICF.arithm(featureVariable, "=", 1);
	}
	
	public Constraint<?, ?> implies(DEFeature feature1, DEFeature feature2) {
		return LCF2.implies(selected(feature1), selected(feature2));
	}
	
	protected Constraint<?, ?> equivalent(DEFeature feature1, DEFeature feature2) {
		return LCF.and(LCF2.implies(selected(feature1), selected(feature2)), LCF2.implies(selected(feature2), selected(feature1)));
	}
	
	protected Constraint<?, ?> or(List<DEFeature> features) {
		BoolVar<?>[] featureVariablesArray = getFeatureVariablesArray(features);
		return LCF.or(featureVariablesArray);
	}
	
	protected Constraint<?, ?> alternative(List<DEFeature> features) {
		BoolVar<?>[] featureVariablesArray = getFeatureVariablesArray(features);
		
		//sum(features) = 1
		return ICF.sum(featureVariablesArray, VF.fixed(1, solver));
	}
	
	protected Constraint<?, ?> arbitrary(List<DEFeature> features, int minCardinality, int maxCardinality) {
		BoolVar<?>[] featureVariablesArray = getFeatureVariablesArray(features);
		
		//sum(features) >= min && sum(features) <= max
		return LCF.and(ICF.sum(featureVariablesArray, ">=", VF.fixed(minCardinality, solver)), ICF.sum(featureVariablesArray, "<=", VF.fixed(maxCardinality, solver)));
	}
	
	
	public Constraint<?, ?> alternativeGroup(DEGroup group) {
		DEFeature parentFeature = group.getParentOfGroup();
		List<DEFeature> features = group.getFeatures();
		
		return LCF2.implies(selected(parentFeature), alternative(features));
	}
	
	public Constraint<?, ?> orGroup(DEGroup group) {
		DEFeature parentFeature = group.getParentOfGroup();
		List<DEFeature> features = group.getFeatures();
		
		return LCF2.implies(selected(parentFeature), or(features));
	}
	
	public Constraint<?, ?> andGroup(DEGroup group) {
		int minCardinality = group.getMinCardinality();
		int maxCardinality = group.getMaxCardinality();
		
		DEFeature parentFeature = group.getParentOfGroup();
		List<DEFeature> features = group.getFeatures();
		
		return LCF2.implies(selected(parentFeature), arbitrary(features, minCardinality, maxCardinality));
	}
	
	
	protected BoolVar<?>[] getFeatureVariablesArray(List<DEFeature> features) {
		List<BoolVar<?>> featureVariables = new ArrayList<BoolVar<?>>();
		
		for (DEFeature feature : features) {
			BoolVar<?> featureVariable = featureToFeatureVariableMapping.get(feature);
			featureVariables.add(featureVariable);
		}
		
		return featureVariables.toArray(new BoolVar[0]);
	}
	
	protected DEFeature getFeatureForVariable(BoolVar<?> featureVariable) {
		Set<Entry<DEFeature, BoolVar<?>>> entries = featureToFeatureVariableMapping.entrySet();
		
		for (Entry<DEFeature, BoolVar<?>> entry : entries) {
			BoolVar<?> variable = entry.getValue();
			
			if (variable == featureVariable) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	public void printCurrentConfiguration() {
		String output = "";
		Variable<?>[] variables = solver.getVars();
		
		boolean isFirst = true;
		
		for (Variable<?> variable : variables) {
			if (variable instanceof BoolVar<?>) {
				BoolVar<?> booleanVariable = (BoolVar<?>) variable;
				DEFeature feature = getFeatureForVariable(booleanVariable);
				
				if (feature != null) {
					int value = booleanVariable.getValue();
					
					if (value == 1) {
						if (!isFirst) {
							output += ", ";
						}
						
						output += feature.getName();
						isFirst = false;
					}
				}
			}
		}
		
		System.out.println("Configuration: " + output);
	}
}
