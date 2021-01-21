package org.deltaecore.feature.configure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.eclipse.emf.ecore.EObject;

public class DEConfigurationScorer {
	public static List<DEConfiguration> sortConfigurationsByScore(List<DEConfiguration> configurations) {
		final Map<DEConfiguration, Integer> configurationScores = new HashMap<DEConfiguration, Integer>();
		
		for (DEConfiguration configuration : configurations) {
			int score = scoreConfiguration(configuration);
			configurationScores.put(configuration, score);
		}
		
		List<DEConfiguration> scoredConfigurations = new ArrayList<DEConfiguration>();
		scoredConfigurations.addAll(configurations);
		
		Collections.sort(scoredConfigurations, new Comparator<DEConfiguration>() {
			@Override
			public int compare(DEConfiguration configuration1, DEConfiguration configuration2) {
				int score1 = configurationScores.get(configuration1);
				int score2 = configurationScores.get(configuration2);
				
				//The smaller the better.
				return Integer.compare(score1, score2);
			}
		});
		
		return scoredConfigurations;
	}
	
	protected static int scoreConfiguration(DEConfiguration configuration) {
		int configurationScore = 0;
		
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact artifact : configurationArtifacts) {
			//Selection status of features has no relevance.
			if (artifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) artifact;
				
				DEVersion version = versionSelection.getVersion();
				DEFeature feature = version.getFeature();
			
				int importance = getDepthInFeatureModel(feature);
				int novelty = getPathLengthToMostRecentVersion(version);
				
				//Simple scoring but does the trick for now.
				int versionScore = (importance + 1) * novelty;
				
				configurationScore += versionScore;
			}
		}
		
//		DEDebug.println(DEFeatureUtil.formatConfiguration(configuration) + " scores " + configurationScore);
		
		return configurationScore;
	}
	
	protected static int getDepthInFeatureModel(DEFeature feature) {
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
	
	protected static int getPathLengthToMostRecentVersion(DEVersion version) {
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
