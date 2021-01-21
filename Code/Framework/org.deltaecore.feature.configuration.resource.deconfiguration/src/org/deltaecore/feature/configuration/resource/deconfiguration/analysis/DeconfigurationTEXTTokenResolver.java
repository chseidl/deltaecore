/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.configuration.resource.deconfiguration.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DeconfigurationTEXTTokenResolver implements org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationTokenResolver {
	
	private org.deltaecore.feature.configuration.resource.deconfiguration.analysis.DeconfigurationDefaultTokenResolver defaultTokenResolver = new org.deltaecore.feature.configuration.resource.deconfiguration.analysis.DeconfigurationDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
