/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.emftext.language.ecoreid.resource.ecoreid.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EcoreidTEXTTokenResolver implements org.emftext.language.ecoreid.resource.ecoreid.IEcoreidTokenResolver {
	
	private org.emftext.language.ecoreid.resource.ecoreid.analysis.EcoreidDefaultTokenResolver defaultTokenResolver = new org.emftext.language.ecoreid.resource.ecoreid.analysis.EcoreidDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.emftext.language.ecoreid.resource.ecoreid.IEcoreidTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
