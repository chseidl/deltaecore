/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decorebase.resource.decorebase.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DecorebaseQUOTED_60_62TokenResolver implements org.deltaecore.core.decorebase.resource.decorebase.IDecorebaseTokenResolver {
	
	private org.deltaecore.core.decorebase.resource.decorebase.analysis.DecorebaseDefaultTokenResolver defaultTokenResolver = new org.deltaecore.core.decorebase.resource.decorebase.analysis.DecorebaseDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, "<", ">", null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.core.decorebase.resource.decorebase.IDecorebaseTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, "<", ">", null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
