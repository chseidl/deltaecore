package org.deltaecore.feature.generator;

import java.io.File;
import java.util.List;

import org.deltaecore.debug.DEDebug;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureFactory;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.generator.string.DEFeatureNameGenerator;
import org.deltaecore.feature.generator.string.DEVersionNumberGenerator;

import de.christophseidl.util.ecore.EcoreIOUtilJavaFile;
import de.christophseidl.util.java.Random2;

//TODO: Cross-tree constraints

public class DEFeatureModelGenerator {
	private static final DEFeatureFactory factory = DEFeatureFactory.eINSTANCE;
	private static final File featureModelSaveFile = new File("resources/Generated.defeaturemodel");
	
	//Parameters
	private boolean specialRootFeatureNameEnabled = false;
	private String specialRootFeatureName = "Root";
	
	private int childFeatureFactorAverage = 4; //per group
	private int childFeatureFactorVariance = 2;
	private double childFeatureFactorVarianceProbability = 0.8;
	
	private int childGroupFactorAverage = 1;
	private int childGroupFactorVariance = 0;
	private double childGroupFactorVarianceProbability = 1.0;
	
	private int featureTreeDepthAverage = 5;
	private int featureTreeDepthVariance = 2;
	private double featureTreeDepthVarianceProbability = 0.6;
	
	
	private int versionTreeBranchingFactorAverage = 1;
	private int versionTreeBranchingFactorVariance = 1;
	private double versionTreeBranchingFactorVarianceProbability = 0.15;
	
	private int versionTreeDepthAverage = 3;
	private int versionTreeDepthVariance = 2;
	private double versionTreeDepthVarianceProbability = 0.7;
	
	//Internal
	private DEFeatureNameGenerator featureNameGenerator;
	private DEVersionNumberGenerator versionNumberGenerator;
	
	private Random2 random;
	
	public DEFeatureModelGenerator() {
		featureNameGenerator = new DEFeatureNameGenerator();
		versionNumberGenerator = new DEVersionNumberGenerator();
		
		random = new Random2();
	}
	
	public void generateAndSaveFeatureModel() {
		DEFeatureModel featureModel = generateFeatureModel();
		
		DEDebug.println("Saving feature model.");

		//Currently, this is configured for standalone use, not within an Eclipse plugin.
		EcoreIOUtilJavaFile.saveModelAs(featureModel, featureModelSaveFile);
		
		DEDebug.println("Saved feature model as \"" + featureModelSaveFile.getPath() + "\"!");
	}
	
	public DEFeatureModel generateFeatureModel() {
		DEDebug.println("Generating feature model.");
		
		featureNameGenerator.reset();
		
		DEFeatureModel featureModel = factory.createDEFeatureModel();
		
		DEFeature rootFeature = generateFeature(1);
		
		//Assure that the root feature is mandatory.
		rootFeature.setMinCardinality(1);
		rootFeature.setMaxCardinality(1);
		
		featureModel.setRootFeature(rootFeature);
		
		DEDebug.println("Generated feature model!");
		
		return featureModel;
	}
	
	private DEFeature generateFeature(int currentFeatureTreeDepth) {
		DEFeature feature = factory.createDEFeature();

		setFeatureName(feature, currentFeatureTreeDepth);
		setFeatureCardinality(feature, currentFeatureTreeDepth);
		
		//Generate initial and possible further versions.
		versionNumberGenerator.reset();
		generateVersion(feature, null, 1);
		
		//See if the a (random) desired tree depth was met
		int desiredTreeDepth = calculateValue(featureTreeDepthAverage, featureTreeDepthVariance, featureTreeDepthVarianceProbability);
		
		if (desiredTreeDepth > currentFeatureTreeDepth) {
			//See how many groups should be present at this level
			int desiredNumberOfGroups = calculateValue(childGroupFactorAverage, childGroupFactorVariance, childGroupFactorVarianceProbability);
			List<DEGroup> groups = feature.getGroups();
			
			for (int i = 0; i < desiredNumberOfGroups; i++) {
				int desiredNumberOfFeatures = calculateValue(childFeatureFactorAverage, childFeatureFactorVariance, childFeatureFactorVarianceProbability);
				
				if (desiredNumberOfFeatures > 0) {
					//Generate a group
					DEGroup group = factory.createDEGroup();
					groups.add(group);
					
					List<DEFeature> childFeatures = group.getFeatures();
	
					//Generate child features
					for (int j = 0; j < desiredNumberOfFeatures; j++) {
						DEFeature childFeature = generateFeature(currentFeatureTreeDepth + 1);
						childFeatures.add(childFeature);
					}
					
					//Only after all child features have been added
					setGroupCardinality(group);
				}
			}
		}
		
		return feature;
	}
	
	private DEVersion generateVersion(DEFeature feature, DEVersion predecessor, int currentVersionTreeDepth) {
		DEVersion version = factory.createDEVersion();
		version.setFeature(feature);
		
		if (predecessor != null) {
			version.setSupersededVersion(predecessor);
		}
		
		//Only after the superseded version was set!
		setVersionNumber(version);
		
		
		int desiredVersionTreeDepth = calculateValue(versionTreeDepthAverage, versionTreeDepthVariance, versionTreeDepthVarianceProbability);
		
		if (desiredVersionTreeDepth > currentVersionTreeDepth) {
			int desiredChildVersions = calculateValue(versionTreeBranchingFactorAverage, versionTreeBranchingFactorVariance, versionTreeBranchingFactorVarianceProbability);
			
			if (desiredChildVersions == 0) {
				desiredChildVersions = 1;
			}
			
			for (int i = 0; i < desiredChildVersions; i++) {
				generateVersion(feature, version, currentVersionTreeDepth + 1);
			}
		}
		
		return version;
	}
	
	
	private void setFeatureName(DEFeature feature, int currentFeatureTreeDepth) {
		if (currentFeatureTreeDepth == 0 && specialRootFeatureNameEnabled) {
			feature.setName(specialRootFeatureName);
			return;
		}
		
		String name = featureNameGenerator.generateString(feature);
		feature.setName(name);
	}
	
	private void setFeatureCardinality(DEFeature feature, int currentFeatureTreeDepth) {
		if (currentFeatureTreeDepth == 0) {
			//Root feature is always mandatory.
			feature.setMinCardinality(1);
			feature.setMaxCardinality(1);
			return;
		}
		
		//Currently, just equal distribution between optional/mandatory features
		int groupType = random.nextInt(2);
		
		switch (groupType) {
			case 0:
				makeFeatureOptional(feature);
				break;
			case 1:
				makeFeatureMandatory(feature);
				break;
		}
	}
	
	private void makeFeatureOptional(DEFeature feature) {
		feature.setMinCardinality(0);
		feature.setMaxCardinality(1);
	}
	
	private void makeFeatureMandatory(DEFeature feature) {
		feature.setMinCardinality(1);
		feature.setMaxCardinality(1);
	}
	
	
	private void setGroupCardinality(DEGroup group) {
		//Currently, just equal distribution between and/or/alternative groups
		int groupType = random.nextInt(3);
		
		switch(groupType) {
			case 0:
				makeGroupAlternative(group);
				break;
			case 1:
				makeGroupOr(group);
				break;
			case 2:
				makeGroupAnd(group);
				break;
		}
	}
	
	private void makeGroupAlternative(DEGroup group) {
		List<DEFeature> childFeatures = group.getFeatures();
		
		for (DEFeature childFeature : childFeatures) {
			makeFeatureOptional(childFeature);
		}
		
		//alternative
		group.setMinCardinality(1);
		group.setMaxCardinality(1);
	}
	
	private void makeGroupOr(DEGroup group) {
		List<DEFeature> childFeatures = group.getFeatures();
		
		int numberOfChildFeatures = childFeatures.size();
		
		for (DEFeature childFeature : childFeatures) {
			makeFeatureOptional(childFeature);
		}
		
		//or
		group.setMinCardinality(1);
		group.setMaxCardinality(numberOfChildFeatures);
	}
	
	private void makeGroupAnd(DEGroup group) {
		List<DEFeature> childFeatures = group.getFeatures();
		
		int numberOfMandatoryFeatures = 0;
		
		for (DEFeature childFeature : childFeatures) {
			if (childFeature.isMandatory()) {
				numberOfMandatoryFeatures++;
			}
		}
		
		int numberOfChildFeatures = childFeatures.size();
		
		group.setMinCardinality(numberOfMandatoryFeatures);
		group.setMaxCardinality(numberOfChildFeatures);	
	}

	private void setVersionNumber(DEVersion version) {
		String number = versionNumberGenerator.generateString(version);
		version.setNumber(number);
	}
	
	
//	private int calculateValue(int average, int variance) {
//		return calculateValue(average, variance, 1.0);
//	}
	
	private int calculateValue(int average, int variance, double varianceProbability) {
		if (random.percentualChance(varianceProbability)) {
			int lower = Math.max(0, average - variance);
			int effectiveVariance = Math.max(0,  2 * variance);
			
			return lower + random.nextInt(effectiveVariance + 1);
		} else {
			return average;
		}
	}
	
	
	//Stand-alone exceution
	public static final void main(String[] args) {
		DEFeatureModelGenerator generator = new DEFeatureModelGenerator();
		
		generator.generateAndSaveFeatureModel();
	}

	
	public boolean getSpecialRootFeatureNameEnabled() {
		return specialRootFeatureNameEnabled;
	}

	public void setSpecialRootFeatureNameEnabled(boolean specialRootFeatureNameEnabled) {
		this.specialRootFeatureNameEnabled = specialRootFeatureNameEnabled;
	}

	public String getSpecialRootFeatureName() {
		return specialRootFeatureName;
	}

	public void setSpecialRootFeatureName(String specialRootFeatureName) {
		this.specialRootFeatureName = specialRootFeatureName;
	}

	public int getChildFeatureFactorAverage() {
		return childFeatureFactorAverage;
	}

	public void setChildFeatureFactorAverage(int childFeatureFactorAverage) {
		this.childFeatureFactorAverage = childFeatureFactorAverage;
	}

	public int getChildFeatureFactorVariance() {
		return childFeatureFactorVariance;
	}

	public void setChildFeatureFactorVariance(int childFeatureFactorVariance) {
		this.childFeatureFactorVariance = childFeatureFactorVariance;
	}

	public double getChildFeatureFactorVarianceProbability() {
		return childFeatureFactorVarianceProbability;
	}

	public void setChildFeatureFactorVarianceProbability(double childFeatureFactorVarianceProbability) {
		this.childFeatureFactorVarianceProbability = childFeatureFactorVarianceProbability;
	}

	public int getChildGroupFactorAverage() {
		return childGroupFactorAverage;
	}

	public void setChildGroupFactorAverage(int childGroupFactorAverage) {
		this.childGroupFactorAverage = childGroupFactorAverage;
	}

	public int getChildGroupFactorVariance() {
		return childGroupFactorVariance;
	}

	public void setChildGroupFactorVariance(int childGroupFactorVariance) {
		this.childGroupFactorVariance = childGroupFactorVariance;
	}

	public double getChildGroupFactorVarianceProbability() {
		return childGroupFactorVarianceProbability;
	}

	public void setChildGroupFactorVarianceProbability(
			double childGroupFactorVarianceProbability) {
		this.childGroupFactorVarianceProbability = childGroupFactorVarianceProbability;
	}

	public int getFeatureTreeDepthAverage() {
		return featureTreeDepthAverage;
	}

	public void setFeatureTreeDepthAverage(int featureTreeDepthAverage) {
		this.featureTreeDepthAverage = featureTreeDepthAverage;
	}

	public int getFeatureTreeDepthVariance() {
		return featureTreeDepthVariance;
	}

	public void setFeatureTreeDepthVariance(int featureTreeDepthVariance) {
		this.featureTreeDepthVariance = featureTreeDepthVariance;
	}

	public double getFeatureTreeDepthVarianceProbability() {
		return featureTreeDepthVarianceProbability;
	}

	public void setFeatureTreeDepthVarianceProbability(double featureTreeDepthVarianceProbability) {
		this.featureTreeDepthVarianceProbability = featureTreeDepthVarianceProbability;
	}

	public int getVersionTreeBranchingFactorAverage() {
		return versionTreeBranchingFactorAverage;
	}

	public void setVersionTreeBranchingFactorAverage(int versionTreeBranchingFactorAverage) {
		this.versionTreeBranchingFactorAverage = versionTreeBranchingFactorAverage;
	}

	public int getVersionTreeBranchingFactorVariance() {
		return versionTreeBranchingFactorVariance;
	}

	public void setVersionTreeBranchingFactorVariance(int versionTreeBranchingFactorVariance) {
		this.versionTreeBranchingFactorVariance = versionTreeBranchingFactorVariance;
	}

	public double getVersionTreeBranchingFactorVarianceProbability() {
		return versionTreeBranchingFactorVarianceProbability;
	}

	public void setVersionTreeBranchingFactorVarianceProbability(double versionTreeBranchingFactorVarianceProbability) {
		this.versionTreeBranchingFactorVarianceProbability = versionTreeBranchingFactorVarianceProbability;
	}

	public int getVersionTreeDepthAverage() {
		return versionTreeDepthAverage;
	}

	public void setVersionTreeDepthAverage(int versionTreeDepthAverage) {
		this.versionTreeDepthAverage = versionTreeDepthAverage;
	}

	public int getVersionTreeDepthVariance() {
		return versionTreeDepthVariance;
	}

	public void setVersionTreeDepthVariance(int versionTreeDepthVariance) {
		this.versionTreeDepthVariance = versionTreeDepthVariance;
	}

	public double getVersionTreeDepthVarianceProbability() {
		return versionTreeDepthVarianceProbability;
	}

	public void setVersionTreeDepthVarianceProbability(double versionTreeDepthVarianceProbability) {
		this.versionTreeDepthVarianceProbability = versionTreeDepthVarianceProbability;
	}

	
	
	
	protected DEFeatureNameGenerator getFeatureNameGenerator() {
		return featureNameGenerator;
	}

	protected DEVersionNumberGenerator getVersionNumberGenerator() {
		return versionNumberGenerator;
	}
	
	protected Random2 getRandom() {
		return random;
	}
}
