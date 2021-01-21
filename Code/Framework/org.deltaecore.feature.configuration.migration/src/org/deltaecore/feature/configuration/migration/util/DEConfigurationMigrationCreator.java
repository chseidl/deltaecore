package org.deltaecore.feature.configuration.migration.util;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationChange;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigration;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigrationFactory;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureActivation;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureDeactivation;
import org.deltaecore.feature.configuration.configurationmigration.DEVersionMigration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;

public class DEConfigurationMigrationCreator {
	public static DEConfigurationMigration determineConfigurationMigration(DEConfiguration oldConfiguration, DEConfiguration newConfiguration) {
		DEConfigurationMigration configurationMigration = createConfigurationMigration(oldConfiguration, newConfiguration);
		
		reorderConfigurationChanges(configurationMigration);
		
		return configurationMigration;
	}
	
	private static DEConfigurationMigration createConfigurationMigration(DEConfiguration oldConfiguration, DEConfiguration newConfiguration) {
		DEConfigurationMigration configurationMigration = DEConfigurationMigrationFactory.eINSTANCE.createDEConfigurationMigration();
		List<DEConfigurationChange> changes = configurationMigration.getChanges();
		
		List<DEConfigurationArtifact> oldConfigurationArtifacts = oldConfiguration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact oldConfigurationArtifact : oldConfigurationArtifacts) {
			
			//Find version migrations
			if (oldConfigurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection oldVersionSelection = (DEVersionSelection) oldConfigurationArtifact;
				DEVersion oldVersion = oldVersionSelection.getVersion();
				DEVersion newVersion = getOtherVersionOfVersionFromConfiguration(oldVersion, newConfiguration);
				
				if (newVersion != null && newVersion != oldVersion) {
					DEVersionMigration versionMigration = DEConfigurationMigrationFactory.eINSTANCE.createDEVersionMigration();
					DEFeature feature = oldVersion.getFeature();
					
					versionMigration.setFeature(feature);
					versionMigration.setNewVersion(newVersion);
					versionMigration.setOldVersion(oldVersion);
					
					changes.add(versionMigration);
				}
			}
			
			//Find feature deactivations
			if (oldConfigurationArtifact instanceof DEFeatureSelection) {
				DEFeatureSelection oldFeatureSelection = (DEFeatureSelection) oldConfigurationArtifact;
				DEFeature oldFeature = oldFeatureSelection.getFeature();
				
				if (!DEConfigurationUtil.configurationContains(newConfiguration, oldFeature)) {
					DEFeatureDeactivation featureDeactivation = DEConfigurationMigrationFactory.eINSTANCE.createDEFeatureDeactivation();
					featureDeactivation.setFeature(oldFeature);
					
					changes.add(featureDeactivation);
				}
			}
			
		}
		
		//Find feature activations
		List<DEConfigurationArtifact> newConfigurationArtifacts = newConfiguration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact newConfigurationArtifact : newConfigurationArtifacts) {
			
			if (newConfigurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection newVersionSelection = (DEVersionSelection) newConfigurationArtifact;
				DEVersion newVersion = newVersionSelection.getVersion();
				DEFeature newFeature = newVersion.getFeature();
				
				if (!DEConfigurationUtil.configurationContains(oldConfiguration, newFeature)) {
					DEFeatureActivation featureActivation = DEConfigurationMigrationFactory.eINSTANCE.createDEFeatureActivation();
					featureActivation.setFeature(newFeature);
					featureActivation.setVersion(newVersion);
					
					changes.add(featureActivation);
				}
			}
			
			//TODO: DEFeatureSelection if reenabled
//			if (newConfigurationArtifact instanceof DEFeature) {
//				DEFeature newFeature = (DEFeature) newConfigurationArtifact;
//				
//				if (!DEConfigurationUtil.configurationContainsFeature(oldConfiguration, newFeature)) {
//					DEFeatureActivation featureActivation = DEConfigurationMigrationFactory.eINSTANCE.createDEFeatureActivation();
//					featureActivation.setFeature(newFeature);
//					
//					changes.add(featureActivation);
//				}
//			}
			
		}
		
		return configurationMigration;
	}
	
	private static DEVersion getOtherVersionOfVersionFromConfiguration(DEVersion searchedVersion, DEConfiguration configuration) {
		DEFeature searchedFeature = searchedVersion.getFeature();
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEVersion) {
				DEVersion version = (DEVersion) configurationArtifact;
				DEFeature feature = version.getFeature();
				
				if (feature == searchedFeature) {
					if (version != searchedVersion) {
						return version;
					} else {
						//Version was not migrated. Hence, there is no _other_ version.
						return null;
					}
				}
			}
		}
		
		return null;
	}
	
	private static void reorderConfigurationChanges(DEConfigurationMigration configurationMigration) {
		//TODO: Sort configuration changes in a way that maximally preserves feature model integrity (each step is a valid configuration if possible)		
	}
}
