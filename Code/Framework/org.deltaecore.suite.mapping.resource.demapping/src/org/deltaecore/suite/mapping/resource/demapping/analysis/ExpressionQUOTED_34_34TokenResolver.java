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

public class ExpressionQUOTED_34_34TokenResolver implements org.deltaecore.suite.mapping.resource.demapping.IDemappingTokenResolver {
	
	private org.deltaecore.feature.expression.resource.expression.analysis.ExpressionQUOTED_34_34TokenResolver importedResolver = new org.deltaecore.feature.expression.resource.expression.analysis.ExpressionQUOTED_34_34TokenResolver();
	
	public String deResolve(Object value, EStructuralFeature feature, EObject container) {
		String result = importedResolver.deResolve(value, feature, container);
		return result;
	}
	
	public void resolve(String lexem, EStructuralFeature feature, final org.deltaecore.suite.mapping.resource.demapping.IDemappingTokenResolveResult result) {
		importedResolver.resolve(lexem, feature, new org.deltaecore.feature.expression.resource.expression.IExpressionTokenResolveResult() {
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
