/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.suite.mapping.resource.demapping.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DemappingQUOTED_60_62TokenResolver implements org.deltaecore.suite.mapping.resource.demapping.IDemappingTokenResolver {
	
	private org.deltaecore.suite.mapping.resource.demapping.analysis.DemappingDefaultTokenResolver defaultTokenResolver = new org.deltaecore.suite.mapping.resource.demapping.analysis.DemappingDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, "<", ">", null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.suite.mapping.resource.demapping.IDemappingTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, "<", ">", null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
