package org.deltaecore.feature.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureFactory;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEFeatureUtil {
	private static final DEFeatureFactory factory = DEFeatureFactory.eINSTANCE;
	
	
	//Create
	public static DEFeatureModel createFeatureModel() {
		DEFeatureModel featureModel = factory.createDEFeatureModel();
		
		return featureModel;
	}
	
	public static DEFeature createFeature(String name) {
		DEFeature feature = factory.createDEFeature();
		feature.setName(name);
		return feature;
	}
	
	public static DEGroup createGroup() {
		return createGroup(0, 0);
	}
	
	public static DEGroup createGroup(int minCardinality, int maxCardinality) {
		DEGroup group = factory.createDEGroup();
		group.setMinCardinality(minCardinality);
		group.setMaxCardinality(maxCardinality);
		return group;
	}
	
	
	//Modify
	public static void makeOptional(DEFeature feature) {
		feature.setMinCardinality(0);
		feature.setMaxCardinality(1);
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			makeAnd(parentGroup);
		}
	}
	
	public static void makeMandatory(DEFeature feature) {
		feature.setMinCardinality(1);
		feature.setMaxCardinality(1);
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			makeAnd(parentGroup);
		}
	}
	
	public static void makeAlternative(DEGroup group) {
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			makeOptional(feature);
		}
		
		group.setMinCardinality(1);
		group.setMaxCardinality(1);
	}
	
	public static void makeAnd(DEGroup group) {
		int optionalFeatures = 0;
		int mandatoryFeatures = 0;
		
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			if (feature.isOptional()) {
				optionalFeatures++;
			} else if (feature.isMandatory()) {
				mandatoryFeatures++;
			}
		}
		
		group.setMinCardinality(mandatoryFeatures);
		group.setMaxCardinality(mandatoryFeatures + optionalFeatures);
	}
	
	public static void makeOr(DEGroup group) {
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			makeOptional(feature);
		}
		
		int numberOfFeatures = features.size();
		
		group.setMinCardinality(1);
		group.setMaxCardinality(numberOfFeatures);
	}
	
	public static boolean isRootFeature(DEFeature feature) {
		if (feature == null) {
			return false;
		}
		
		EObject container = feature.eContainer();
		return (container instanceof DEFeatureModel);
	}
	
	public static boolean isLeafFeature(DEFeature feature) {
		List<DEGroup> groups = feature.getGroups();
		return groups.isEmpty();
	}
	
	public static boolean isOptional(DEFeature feature) {
		return feature.getMinCardinality() == 0;
	}
	
	public static boolean isMandatory(DEFeature feature) {
		return feature.getMinCardinality() == 1;
	}
	
	public static boolean hasVersions(DEFeature feature) {
		if (feature == null) {
			return false;
		}
		
		List<DEVersion> versions = feature.getVersions();
		return !versions.isEmpty();
	}
	
	public static List<DEFeature> findFeaturesOnSameTreeLevel(DEFeature referenceFeature) {
		int referenceTreeLevel = getTreeLevelOfFeature(referenceFeature);
		List<DEFeature> featuresOnSameTreeLevel = new ArrayList<DEFeature>();
		
		EObject rootContainer = EcoreUtil.getRootContainer(referenceFeature);
		
		//NOTE: If a feature is detached, the determined root container is the feature itself.
		if (rootContainer instanceof DEFeatureModel) {
			DEFeatureModel featureModel = (DEFeatureModel) rootContainer;
			
			Iterator<EObject> iterator = featureModel.eAllContents();
			
			while(iterator.hasNext()) {
				EObject element = iterator.next();
				
				if (element instanceof DEFeature) {
					DEFeature feature = (DEFeature) element;
					
					if (feature != referenceFeature) {
						int featureTreeLevel = getTreeLevelOfFeature(feature);
						
						if (referenceTreeLevel == featureTreeLevel) {
							featuresOnSameTreeLevel.add(feature);
						}
					}
				}
			}
		}
		
		return featuresOnSameTreeLevel;
	}
	
	private static int getTreeLevelOfFeature(DEFeature feature) {
		if (feature == null) {
			return -1;
		}
		
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup == null) {
			//Root feature is considered level 0.
			return 0;
		}
		
		DEFeature parentFeature = parentGroup.getParentOfGroup();
		return getTreeLevelOfFeature(parentFeature) + 1;
	}
	
	public static int getNumberOfMandatoryFeatures(List<DEFeature> features) {
		int numberOfMandatoryFeatures = 0;
		
		for (DEFeature feature : features) {
			if (feature.isMandatory()) {
				numberOfMandatoryFeatures++;
			}
		}
		
		return numberOfMandatoryFeatures;
	}
	
	
	/**
	 * This does not take constraints into account.
	 */
//	public static int calculateNumberOfPossibleConfigurations(DEFeatureModel featureModel) {
//		DEFeature rootFeature = featureModel.getRootFeature();
//		
//		return doCalculateNumberOfPossibleConfigurations(rootFeature);
//	}
//	
//	private static int doCalculateNumberOfPossibleConfigurations(DECardinalityBasedElement cardinalityBasedElement) {
//		boolean includeVersions = true;
//		
//		if (cardinalityBasedElement instanceof DEGroup) {
//			DEGroup group = (DEGroup) cardinalityBasedElement;
//			
//			int minCardinality = group.getMaxCardinality();
//			int maxCardinality = group.getMaxCardinality();
//			
//			List<DEFeature> features = group.getFeatures();
//			int numberOfFeatures = features.size();
//			int numberOfMandatoryFeatures = getNumberOfMandatoryFeatures(features);
//			
//			int effectiveMinCardinality = Math.min(minCardinality, numberOfMandatoryFeatures);
//			int effectiveMaxCardinality = Math.min(maxCardinality, numberOfFeatures);
//			
//			if (effectiveMinCardinality > effectiveMaxCardinality) {
//				return 0;
//			}
//			
//			if (effectiveMinCardinality == effectiveMaxCardinality) {
//				return effectiveMinCardinality;
//			}
//			
//			
//			int k = numberOfFeatures - numberOfMandatoryFeatures;
//			int numberOfPossibilities = 0;
//			
//			for (int n = effectiveMinCardinality; n <= effectiveMaxCardinality; n++) {
//				int subBranchPossibilities = 1;
//				//TODO: The concrete combination has to consider how many options the sub branch has
//				numberOfPossibilities += binomialCoefficient(n, k);
//			}
//			
//			return numberOfPossibilities;
//		}
//		
//		
//		if (cardinalityBasedElement instanceof DEFeature) {
//			DEFeature feature = (DEFeature) cardinalityBasedElement;
//			
//			int numberOfPossibilities = 1;
//			
//			if (includeVersions) {
//				List<DEVersion> versions = feature.getVersions();
//				
//				if (!versions.isEmpty()) {
//					//Add one less than all versions as selecting the plain
//					//feature (without any version) is no longer possible.
//					numberOfPossibilities += versions.size() - 1;
//				}
//			}
//			
//			List<DEGroup> groups = feature.getGroups();
//			
//			for (DEGroup group : groups) {
//				numberOfPossibilities *= doCalculateNumberOfPossibleConfigurations(group);
//			}
//
//			//Has to be last as deselecting the feature does not
//			//entail any selection of child groups/features.
//			if (feature.isOptional()) {
//				numberOfPossibilities += 1;
//			}
//			
//			return numberOfPossibilities;
//		}
//		
//		throw new UnsupportedOperationException();
//	}
//	
//	private static long binomialCoefficient(int n, int k) {
//		long coefficient = 1;
//		
//		for (int i = n - k + 1; i <= n; i++) {
//			coefficient *= i;
//		}
//		
//		for (int i = 1; i <= k; i++) {
//			coefficient /= i;
//		}
//		
//		return coefficient;
//	}
	
	
	
//	public static void printConfiguration(List<? extends DEConfigurationArtifact> configuration) {
//		DEDebug.println(formatConfiguration(configuration));
//	}
//	
//	public static String formatConfiguration(List<? extends DEConfigurationArtifact> configuration) {
//		String output = "";
//		boolean isFirst = true;
//		
//		for (DEConfigurationArtifact configurationArtifact : configuration) {
//			if (!isFirst) {
//				output += ", ";
//			}
//			
//			if (configurationArtifact instanceof DEFeature) {
//				DEFeature feature = (DEFeature) configurationArtifact;
//				
//				output += feature.getName();
//			}
//			
//			if (configurationArtifact instanceof DEVersion) {
//				
//				DEVersion version = (DEVersion) configurationArtifact;
//				DEFeature feature = version.getFeature();
//				
//				output += feature.getName() + " @ " + version.getNumber();
//			}
//			
//			isFirst = false;
//		}
//		
//		return output;
//	}
	
}
