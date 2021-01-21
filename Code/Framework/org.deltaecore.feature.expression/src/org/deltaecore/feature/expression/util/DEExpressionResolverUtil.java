package org.deltaecore.feature.expression.util;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EObject;

public class DEExpressionResolverUtil {
	
	public static DEFeature resolveFeature(String identifier, EObject elementFromAccompanyingResource) {
		DEFeatureModel featureModel = DEFeatureIOUtil.loadAccompanyingFeatureModel(elementFromAccompanyingResource);

		if (identifier == null) {
			return null;
		}
		
		return DEFeatureResolverUtil.resolveFeature(identifier, featureModel);
	}
	
	public static DEVersion resolveVersion(String identifier, DEVersionRestriction container) {
		DEAbstractFeatureReferenceExpression featureReference = container.getRestrictedFeatureReferenceExpression();
		DEFeature feature = featureReference.getFeature();
		
		return DEFeatureResolverUtil.resolveVersion(identifier, feature);
	}
}
