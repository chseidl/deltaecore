/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DecoreBOOLEAN_LITERAL_TOKENTokenResolver implements org.deltaecore.core.decore.resource.decore.IDecoreTokenResolver {
	
	private org.deltaecore.core.decore.resource.decore.analysis.DecoreDefaultTokenResolver defaultTokenResolver = new org.deltaecore.core.decore.resource.decore.analysis.DecoreDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.core.decore.resource.decore.IDecoreTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
