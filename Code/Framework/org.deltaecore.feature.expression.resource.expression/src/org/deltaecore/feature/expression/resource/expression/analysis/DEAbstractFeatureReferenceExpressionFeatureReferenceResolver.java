/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.expression.resource.expression.analysis;

import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.expression.util.DEExpressionResolverUtil;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EReference;

public class DEAbstractFeatureReferenceExpressionFeatureReferenceResolver implements org.deltaecore.feature.expression.resource.expression.IExpressionReferenceResolver<org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression, org.deltaecore.feature.DEFeature> {
	
	public void resolve(String identifier, org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.feature.expression.resource.expression.IExpressionReferenceResolveResult<org.deltaecore.feature.DEFeature> result) {
		DEFeature feature = DEExpressionResolverUtil.resolveFeature(identifier, container);
		
		if (feature != null) {
			result.addMapping(identifier, feature);
		}
	}
	
	public String deResolve(org.deltaecore.feature.DEFeature element, org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression container, EReference reference) {
		return DEFeatureResolverUtil.deresolveFeature(element);
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
