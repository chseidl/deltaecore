/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.expression.resource.expression.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ExpressionIDENTIFIER_TOKENTokenResolver implements org.deltaecore.feature.expression.resource.expression.IExpressionTokenResolver {
	
	private org.deltaecore.feature.expression.resource.expression.analysis.ExpressionDefaultTokenResolver defaultTokenResolver = new org.deltaecore.feature.expression.resource.expression.analysis.ExpressionDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.feature.expression.resource.expression.IExpressionTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
