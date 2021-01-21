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

public class DecorebaseIDENTIFIER_TOKENTokenResolver implements org.deltaecore.core.decore.resource.decore.IDecoreTokenResolver {
	
	private org.deltaecore.core.decorebase.resource.decorebase.analysis.DecorebaseIDENTIFIER_TOKENTokenResolver importedResolver = new org.deltaecore.core.decorebase.resource.decorebase.analysis.DecorebaseIDENTIFIER_TOKENTokenResolver();
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		String result = importedResolver.deResolve(value, feature, container);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, final org.deltaecore.core.decore.resource.decore.IDecoreTokenResolveResult result) {
		importedResolver.resolve(lexem, feature, new org.deltaecore.core.decorebase.resource.decorebase.IDecorebaseTokenResolveResult() {
			public String getErrorMessage() {
				return result.getErrorMessage();
			}
			
			public Object getResolvedToken() {
				return result.getResolvedToken();
			}
			
			public void setErrorMessage(String message) {
				result.setErrorMessage(message);
			}
			
			public void setResolvedToken(Object resolvedToken) {
				result.setResolvedToken(resolvedToken);
			}
			
		});
	}
	
	public void setOptions(Map<?,?> options) {
		importedResolver.setOptions(options);
	}
	
}
