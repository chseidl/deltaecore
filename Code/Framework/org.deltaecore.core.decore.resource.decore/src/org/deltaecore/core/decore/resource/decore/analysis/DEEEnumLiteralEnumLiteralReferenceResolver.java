/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;

public class DEEEnumLiteralEnumLiteralReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEEEnumLiteral, org.eclipse.emf.ecore.EEnumLiteral> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEEEnumLiteral container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.eclipse.emf.ecore.EEnumLiteral> result) {
		EEnum eEnum = container.getEnum();
		
		if (eEnum != null) {
			EEnumLiteral literal = DEDEcoreResolverUtil.resolveEnumLiteral(identifier, eEnum);
			
			if (literal != null) {
				result.addMapping(identifier, literal);
			}
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EEnumLiteral element, org.deltaecore.core.decore.DEEEnumLiteral container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
