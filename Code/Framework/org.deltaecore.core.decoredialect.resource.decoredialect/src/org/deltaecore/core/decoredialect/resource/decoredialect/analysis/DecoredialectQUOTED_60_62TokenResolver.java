/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DecoredialectQUOTED_60_62TokenResolver implements org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectTokenResolver {
	
	private org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultTokenResolver defaultTokenResolver = new org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, "<", ">", null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, "<", ">", null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
