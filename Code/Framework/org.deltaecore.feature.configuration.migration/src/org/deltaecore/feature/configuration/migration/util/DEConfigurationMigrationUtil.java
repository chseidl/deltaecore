package org.deltaecore.feature.configuration.migration.util;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationChange;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigration;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureActivation;
import org.deltaecore.feature.configuration.configurationmigration.DEFeatureDeactivation;
import org.deltaecore.feature.configuration.configurationmigration.DEVersionMigration;

public class DEConfigurationMigrationUtil {
	public static String printConfigurationMigration(DEConfigurationMigration configurationMigration) {
		String output = "";
		
		List<DEConfigurationChange> changes = configurationMigration.getChanges();
		
		for (DEConfigurationChange change : changes) {
			if (change instanceof DEVersionMigration) {
				DEVersionMigration versionMigration = (DEVersionMigration) change;
				DEFeature feature = versionMigration.getFeature();
				DEVersion oldVersion = versionMigration.getOldVersion();
				DEVersion newVersion = versionMigration.getNewVersion();
				
				output += "Migrate feature \"" + feature.getName() + "\" from version \"" + oldVersion.getNumber() + "\" to version \"" + newVersion.getNumber() + "\".";
				output += System.lineSeparator();
			}
			
			if (change instanceof DEFeatureActivation) {
				DEFeatureActivation featureActivation = (DEFeatureActivation) change;
				DEFeature feature = featureActivation.getFeature();
				DEVersion version = featureActivation.getVersion();
				
				output += "Activate feature \"" + feature.getName() + "\" with version \"" + version.getNumber() + "\".";
				output += System.lineSeparator();
			}
			
			if (change instanceof DEFeatureDeactivation) {
				DEFeatureDeactivation featureDeactivation = (DEFeatureDeactivation) change;
				DEFeature feature = featureDeactivation.getFeature();
				
				output += "Deactivate feature \"" + feature.getName() + "\".";
				output += System.lineSeparator();
			}
		}
		
		return output;
	}
}
