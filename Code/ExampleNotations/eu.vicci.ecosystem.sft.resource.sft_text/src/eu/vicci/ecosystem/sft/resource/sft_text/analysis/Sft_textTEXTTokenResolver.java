/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package eu.vicci.ecosystem.sft.resource.sft_text.analysis;

import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class Sft_textTEXTTokenResolver implements eu.vicci.ecosystem.sft.resource.sft_text.ISft_textTokenResolver {
	
	private eu.vicci.ecosystem.sft.resource.sft_text.analysis.Sft_textDefaultTokenResolver defaultTokenResolver = new eu.vicci.ecosystem.sft.resource.sft_text.analysis.Sft_textDefaultTokenResolver(true);
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, eu.vicci.ecosystem.sft.resource.sft_text.ISft_textTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
