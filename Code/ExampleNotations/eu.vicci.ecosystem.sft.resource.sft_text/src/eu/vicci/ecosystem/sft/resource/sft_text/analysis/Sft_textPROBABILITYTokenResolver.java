/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package eu.vicci.ecosystem.sft.resource.sft_text.analysis;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Sft_textPROBABILITYTokenResolver implements eu.vicci.ecosystem.sft.resource.sft_text.ISft_textTokenResolver {
	
	private eu.vicci.ecosystem.sft.resource.sft_text.analysis.Sft_textDefaultTokenResolver defaultTokenResolver = new eu.vicci.ecosystem.sft.resource.sft_text.analysis.Sft_textDefaultTokenResolver(true);
	
	public String deResolve(Object value, org.eclipse.emf.ecore.EStructuralFeature feature, org.eclipse.emf.ecore.EObject container) {
		// By default token de-resolving is delegated to the DefaultTokenResolver.
//		String result = defaultTokenResolver.deResolve(value, feature, container, null, null, null);
//		return result;

		//Make sure that the value is printed as x.xxxxxx and not something like 3E01
		if (value instanceof Double) {
			Double doubleValue = (Double) value;
			
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
			decimalFormatSymbols.setDecimalSeparator('.');
			decimalFormatSymbols.setGroupingSeparator(',');
			DecimalFormat decimalFormat = new DecimalFormat("#.#############################", decimalFormatSymbols);
			return  decimalFormat.format(doubleValue);
		}
		
		System.err.println("Failed to print probability.");
		return "";
	}
	
	public void resolve(String lexem, org.eclipse.emf.ecore.EStructuralFeature feature, eu.vicci.ecosystem.sft.resource.sft_text.ISft_textTokenResolveResult result) {
		// By default token resolving is delegated to the DefaultTokenResolver.
		defaultTokenResolver.resolve(lexem, feature, result, null, null, null);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		defaultTokenResolver.setOptions(options);
	}
	
}
