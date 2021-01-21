package org.deltaecore.feature.configuration.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.eclipse.emf.ecore.util.EcoreUtil;

//TODO: Rename this to not confuse it with version completion

//Add features for all selected versions.
public class DEConfigurationCompleter {
	public static DEConfiguration completeConfiguration(DEConfiguration configuration) {
		Set<DEFeature> selectedFeatures = new HashSet<DEFeature>();
		Set<DEFeature> requiredFeatures = new HashSet<DEFeature>();
		
		List<DEConfigurationArtifact> configurationArtifacts = configuration.getConfigurationArtifacts();
		
		for (DEConfigurationArtifact configurationArtifact : configurationArtifacts) {
			if (configurationArtifact instanceof DEFeatureSelection) {
				DEFeatureSelection featureSelection = (DEFeatureSelection) configurationArtifact;
				DEFeature feature = featureSelection.getFeature();
				selectedFeatures.add(feature);
			}
			
			if (configurationArtifact instanceof DEVersionSelection) {
				DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
				DEVersion version = versionSelection.getVersion();
				DEFeature feature = version.getFeature();
				requiredFeatures.add(feature);
			}
		}
		
		Set<DEFeature> missingFeatures = new HashSet<DEFeature>();
		missingFeatures.addAll(requiredFeatures);
		missingFeatures.removeAll(selectedFeatures);
		
		DEConfiguration completedConfiguration = EcoreUtil.copy(configuration);
		List<DEConfigurationArtifact> completedConfigurationArtifacts = completedConfiguration.getConfigurationArtifacts();
		
		for (DEFeature missingFeature : missingFeatures) {
			DEFeatureSelection featureSelection = DEConfigurationFactory.eINSTANCE.createDEFeatureSelection();
			featureSelection.setFeature(missingFeature);
			completedConfigurationArtifacts.add(featureSelection);
		}
		
		return completedConfiguration;
	}
}
