package org.deltaecore.feature.configuration.migration;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationChange;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigration;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureActivation;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureDeactivation;
import org.deltaecore.feature.configuration.configurationmigration.DEVersionMigration;

//Abstract base class for concrete implementations that react to changes in a configuration
//migration scheme (e.g., to reconfigure solution space assets according to the configuration migration). 
public abstract class DEConfigurationMigrationInterpreter {
	public void interpretConfigurationMigration(DEConfigurationMigration configurationMigration) {
		List<DEConfigurationChange> changes = configurationMigration.getChanges();
		
		for (DEConfigurationChange change : changes) {
			if (change instanceof DEVersionMigration) {
				DEVersionMigration versionMigration = (DEVersionMigration) change;
				DEFeature feature = versionMigration.getFeature();
				DEVersion oldVersion = versionMigration.getOldVersion();
				DEVersion newVersion = versionMigration.getNewVersion();
				
				interpretVersionMigration(feature, oldVersion, newVersion);
			}
			
			if (change instanceof DEFeatureActivation) {
				DEFeatureActivation featureActivation = (DEFeatureActivation) change;
				DEFeature feature = featureActivation.getFeature();
				DEVersion version = featureActivation.getVersion();
				
				interpretFeatureActivation(feature, version);
			}
			
			if (change instanceof DEFeatureDeactivation) {
				DEFeatureDeactivation featureDeactivation = (DEFeatureDeactivation) change;
				DEFeature feature = featureDeactivation.getFeature();
				
				interpretFeatureDeactivation(feature);
			}
		}
	}
	
	protected abstract void interpretVersionMigration(DEFeature feature, DEVersion oldVersion, DEVersion newVersion);
	protected abstract void interpretFeatureActivation(DEFeature feature, DEVersion version);
	protected abstract void interpretFeatureDeactivation(DEFeature feature);
}
