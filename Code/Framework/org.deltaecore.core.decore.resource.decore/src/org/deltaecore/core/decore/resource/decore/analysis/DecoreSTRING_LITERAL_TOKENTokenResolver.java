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

public class DecoreSTRING_LITERAL_TOKENTokenResolver implements org.deltaecore.core.decore.resource.decore.IDecoreTokenResolver {
	
	public String deResolve(Object rawValue, EStructuralFeature feature, EObject container) {
		if (rawValue == null) {
			return null;
		}
		
		String value = rawValue.toString();
		value = value.replaceAll("\"", "\\\\\"");
		return "\"" + value + "\"";
	}
	
	public void resolve(String lexem, EStructuralFeature feature, org.deltaecore.core.decore.resource.decore.IDecoreTokenResolveResult result) {
		String value = lexem;
		
		if (value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		}
		
		value = value.replaceAll("\\\\\"", "\"");
		result.setResolvedToken(value);
	}
	
	public void setOptions(Map<?,?> options) {
	}
	
}
