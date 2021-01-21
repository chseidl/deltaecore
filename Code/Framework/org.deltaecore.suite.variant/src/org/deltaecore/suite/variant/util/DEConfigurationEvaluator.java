package org.deltaecore.suite.variant.util;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.suite.mapping.DEMapping;
import org.deltaecore.suite.mapping.DEMappingModel;
import org.deltaecore.suite.mapping.util.DEMappingEvaluator;
import org.deltaecore.suite.mapping.util.DEMappingIOUtil;

public class DEConfigurationEvaluator extends DEMappingEvaluator {
	private DEConfiguration configuration;
	
	private static DEMappingModel findMappingModelForConfiguration(DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		if (configurationArtifacts.isEmpty()) {
			throw new UnsupportedOperationException("Configuration is empty.");
		}
	
		DEFeatureModel featureModel = configuration.getFeatureModel();
		return DEMappingIOUtil.loadAccompanyingMappingModel(featureModel);
	}
	
	public List<DEDelta> evaluateConfiguration(DEConfiguration configuration) {
		DEMappingModel mappingModel = findMappingModelForConfiguration(configuration);
		
		if (mappingModel == null) {
			throw new UnsupportedOperationException("Accompanying mapping model could not be found or is invalid.");
		}
		
		return evaluateConfiguration(configuration, mappingModel);
	}

	public List<DEDelta> evaluateConfiguration(DEConfiguration configuration, DEMappingModel mappingModel) {
		this.configuration = configuration;
		
		List<DEDelta> allDeltas = new ArrayList<DEDelta>();
		
		if (mappingModel == null) {
			return allDeltas;
		}
		
		List<DEMapping> mappings = mappingModel.getMappings();
		
		for (DEMapping mapping : mappings) {
			List<DEDelta> matchingDeltas = evaluateMapping(mapping);
			
			for (DEDelta matchingDelta : matchingDeltas) {
				if (!allDeltas.contains(matchingDelta)) {
					allDeltas.add(matchingDelta);
				}
			}
		}
		
		return allDeltas;
	}

	@Override
	protected boolean isFeaturePresent(DEFeature searchedFeature) {
		return DEConfigurationUtil.configurationContains(configuration, searchedFeature);
	}
	
	@Override
	protected boolean isVersionPresent(DEVersion searchedVersion) {
		return DEConfigurationUtil.configurationContains(configuration, searchedVersion);
	}
	
	protected boolean isConfigurationArtifactPresent(DEConfigurationArtifact configurationArtifact) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		return configurationArtifacts.contains(configurationArtifact);
	}
	
	public static List<DEDelta> getDeltasListForConfiguration(DEConfiguration configuration) {
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		if (configurationArtifacts.isEmpty()) {
			throw new UnsupportedOperationException("Configuration contains no elements.");
		}
		
		DEConfigurationEvaluator evaluator = new DEConfigurationEvaluator();
		return evaluator.evaluateConfiguration(configuration);
	}	
	
//	public static List<DEDelta> getDeltasListForConfiguration(DEConfiguration configuration, DEMappingModel mapping) {
//		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
//		
//		if (configurationArtifacts.isEmpty()) {
//			throw new UnsupportedOperationException("Configuration contains no elements.");
//		}
//		
//		DEConfigurationEvaluator evaluator = new DEConfigurationEvaluator();
//		return evaluator.evaluateConfiguration(configuration, mapping);
//	}
}
