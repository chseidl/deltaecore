package org.deltaecore.feature.configuration.util;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEConfigurationResolverUtil {
	public static DEFeature resolveFeature(String identifier, EObject elementFromConfiguration) {
		EObject root = EcoreUtil.getRootContainer(elementFromConfiguration);
		
		if (root instanceof DEConfiguration) {
			DEConfiguration configuration = (DEConfiguration) root;
			
			DEFeatureModel featureModel = configuration.getFeatureModel();
			
			if (featureModel != null) {
				DEFeature feature = DEFeatureResolverUtil.resolveFeature(identifier, featureModel);
				return feature;
			}
		}
		
		return null;
	}
}
