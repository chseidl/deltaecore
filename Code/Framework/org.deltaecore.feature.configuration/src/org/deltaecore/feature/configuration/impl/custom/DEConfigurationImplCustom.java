package org.deltaecore.feature.configuration.impl.custom;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.configuration.impl.DEConfigurationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEConfigurationImplCustom extends DEConfigurationImpl {

	@Override
	public DEFeatureModel getFeatureModel() {
		DEFeatureModel featureModel = super.getFeatureModel();
		
		if (featureModel == null) {
			List<DEConfigurationArtifact> configurationArtifacts = getConfigurationArtifacts();
			
			if (!configurationArtifacts.isEmpty()) {
				DEConfigurationArtifact configurationArtifact = configurationArtifacts.get(0);
				
				if (configurationArtifact instanceof DEFeatureSelection) {
					DEFeatureSelection featureSelection = (DEFeatureSelection) configurationArtifact;
					DEFeature feature = featureSelection.getFeature();
					
					return (DEFeatureModel) EcoreUtil.getRootContainer(feature);
				}
				
				if (configurationArtifact instanceof DEVersionSelection) {
					DEVersionSelection versionSelection = (DEVersionSelection) configurationArtifact;
					DEVersion version = versionSelection.getVersion();
					
					return (DEFeatureModel) EcoreUtil.getRootContainer(version);
				}
			}
		}
		
		return featureModel;
	}

}
