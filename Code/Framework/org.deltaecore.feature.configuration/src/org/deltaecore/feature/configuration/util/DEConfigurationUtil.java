package org.deltaecore.feature.configuration.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.util.DEVersionUtil;

public class DEConfigurationUtil {
	public static List<DEFeature> getSelectedFeatures(DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		List<DEFeature> selectedFeatures = new ArrayList<DEFeature>();
		
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) configurationArtifact;
				DEFeature feature = featureSelection.getFeature();
				
				if (feature != null && !selectedFeatures.contains(feature)) {
					selectedFeatures.add(feature);
				}
			}
			
			if (configurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
				DEVersion version = versionSelection.getVersion();
				DEFeature feature = version.getFeature();
				
				if (feature != null && !selectedFeatures.contains(feature)) {
					selectedFeatures.add(feature);
				}
			}
		}
		
		return selectedFeatures;
	}
	
	public static List<DEVersion> getSelectedVersions(DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		List<DEVersion> selectedVersions = new ArrayList<DEVersion>();
		
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
				DEVersion version = versionSelection.getVersion();
				
				if (version != null && !selectedVersions.contains(version)) {
					selectedVersions.add(version);
				}
			}
		}
		
		return selectedVersions;
	}
	
	public static List<DEVersion> getSelectedVersionsForFeature(DEConfiguration configuration, DEFeature searchedFeature) {
		List<DEVersionSelection> versionSelections = getVersionSelectionsForFeature(configuration, searchedFeature);
		List<DEVersion> selectedVersions = new ArrayList<DEVersion>();
		
		
		for (DEVersionSelection versionSelection : versionSelections) {
			DEVersion version = versionSelection.getVersion();
			selectedVersions.add(version);
		}
		
		return selectedVersions;
	}
	
	public static List<DEVersionSelection> getVersionSelectionsForFeature(DEConfiguration configuration, DEFeature searchedFeature) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		List<DEVersionSelection> versionSelections = new ArrayList<DEVersionSelection>();
		
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
				DEVersion version = versionSelection.getVersion();
				DEFeature feature = version.getFeature();
				
				if (feature == searchedFeature) {
					if (version != null) {
						versionSelections.add(versionSelection);
					}
				}
			}
		}
		
		return versionSelections;
	}
	
	public static DEVersion getSelectedVersionForFeature(DEConfiguration configuration, DEFeature searchedFeature) {
		List<DEVersion> selectedVersions = getSelectedVersionsForFeature(configuration, searchedFeature);
		
		if (selectedVersions.isEmpty()) {
			return null;
		}
		
		return selectedVersions.get(0);
	}
	
	public static boolean configurationContains(DEConfiguration configuration, DEFeature searchedFeature) {
		List<DEFeature> selectedFeatures = getSelectedFeatures(configuration);
		
		return selectedFeatures.contains(searchedFeature);
	}
	
	public static boolean configurationContains(DEConfiguration configuration, DEVersion searchedVersion) {
		List<DEVersion> selectedVersions = getSelectedVersions(configuration);
		return selectedVersions.contains(searchedVersion);
	}
	
	public static void addFeatureToConfiguration(DEFeature feature, DEConfiguration configuration, boolean automaticallyAddParents) {
		if (!configurationContains(configuration, feature)) {
			List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
			DEFeatureSelection featureSelection = DEConfigurationFactory.eINSTANCE.createDEFeatureSelection();
			featureSelection.setFeature(feature);
			configurationArtifacts.add(featureSelection);
		}
		
		if (automaticallyAddParents) {
			automaticallyAddParentsToConfiguration(feature, configuration);
		}
	}
	
	public static void addFeatureAtInitialVersionToConfiguration(DEFeature feature, DEConfiguration configuration, boolean automaticallyAddParents) {
		if (!configurationContains(configuration, feature)) {
			//Also add a version
			DEVersion version = DEVersionUtil.getRootVersion(feature);
			
			if (version != null) {
				addVersionToConfiguration(version, configuration, automaticallyAddParents);
			} else {
				addFeatureToConfiguration(feature, configuration, automaticallyAddParents);
			}
		}
	}
	
	private static void automaticallyAddParentsToConfiguration(DEFeature feature, DEConfiguration configuration) {
		DEGroup parentGroup = feature.getParentOfFeature();
		
		if (parentGroup != null) {
			DEFeature parentFeature = parentGroup.getParentOfGroup();
			
			if (parentFeature != null) {
				addFeatureAtInitialVersionToConfiguration(parentFeature, configuration, true);
			}
		}
	}
	
	public static void removeFeatureFromConfiguration(DEFeature searchedFeature, DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		Iterator<DEConfigurationArtifact> iterator = configurationArtifacts.iterator();
		
		while(iterator.hasNext()) {
			DEConfigurationArtifact configurationArtifact = iterator.next();
			
			if (configurationArtifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) configurationArtifact;
				DEFeature feature = featureSelection.getFeature();
				
				if (feature == searchedFeature) {
					iterator.remove();
				}
			}
		}
		
		//Also remove versions of feature
		removeVersionSelectionsFromConfiguration(searchedFeature, configuration);
		
		//TODO: Maybe remove child features later on?
	}
	
	
	public static void addVersionToConfiguration(DEVersion version, DEConfiguration configuration, boolean automaticallyAddParents) {
		DEFeature feature = version.getFeature();
		
		if (!configurationContains(configuration, version)) {
			List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
			
			addFeatureToConfiguration(feature, configuration, false);
			
			//Remove old version selections first.
			removeVersionSelectionsFromConfiguration(feature, configuration);
			
			DEVersionSelection versionSelection = DEConfigurationFactory.eINSTANCE.createDEVersionSelection();
			versionSelection.setVersion(version);
			configurationArtifacts.add(versionSelection);
		}
		
		if (automaticallyAddParents) {
			automaticallyAddParentsToConfiguration(feature, configuration);
		}
	}
	
	public static void removeVersionSelectionsFromConfiguration(DEFeature feature, DEConfiguration configuration) {
		List<DEVersionSelection> versionSelections = getVersionSelectionsForFeature(configuration, feature);
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		configurationArtifacts.removeAll(versionSelections);
	}
	
	public static void removeVersionFromConfiguration(DEVersion version, DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		configurationArtifacts.remove(version);
	}
	
	public static void transferAndReplaceConfigurationArtifacts(DEConfiguration sourceConfiguration, DEConfiguration targetConfiguration) {
		List<DEConfigurationArtifact> sourceConfigurationArtifacts = sourceConfiguration.getConfigurationArtifacts();
		List<DEConfigurationArtifact> targetConfigurationArtifacts = targetConfiguration.getConfigurationArtifacts();

		targetConfigurationArtifacts.clear();
		targetConfigurationArtifacts.addAll(sourceConfigurationArtifacts);

		sourceConfigurationArtifacts.clear();
	}
	
	public static String printConfiguration(DEConfiguration configuration) {
		List<DEFeature> features = new ArrayList<DEFeature>();
		Map<DEFeature, DEVersion> featureToVersionMap = new HashMap<DEFeature, DEVersion>();
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		//Collect features and versions
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) configurationArtifact;
				DEFeature feature = featureSelection.getFeature();
				
				features.add(feature);
			}
			
			if (configurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
				DEVersion version = versionSelection.getVersion();
				DEFeature feature = version.getFeature();
				
				featureToVersionMap.put(feature, version);
			}
		}
		
		
		//Sort by feature name
		Collections.sort(features, new Comparator<DEFeature>() {
			@Override
			public int compare(DEFeature feature1, DEFeature feature2) {
				String name1 = feature1.getName();
				String name2 = feature2.getName();
				
				return name1.compareTo(name2);
			}
			
		});
		
		//Print
		String output = "";
		
		for (DEFeature feature : features) {
			
			output += feature.getName();
			
			if (featureToVersionMap.containsKey(feature)) {
				DEVersion version = featureToVersionMap.get(feature);
				output += "@" + version.getNumber();
			}
			
			output += System.lineSeparator();
		}
		
		return output;
	}
}
