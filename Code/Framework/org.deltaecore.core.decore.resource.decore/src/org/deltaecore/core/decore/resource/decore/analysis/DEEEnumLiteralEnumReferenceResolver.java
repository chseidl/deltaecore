/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEEEnumLiteralEnumReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEEEnumLiteral, org.eclipse.emf.ecore.EEnum> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEEEnumLiteral container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.eclipse.emf.ecore.EEnum> result) {
		DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(container);
		EEnum eEnum = DEDEcoreResolverUtil.resolveEEnum(identifier, delta);
		
		if (eEnum != null) {
			result.addMapping(identifier, eEnum);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EEnum element, org.deltaecore.core.decore.DEEEnumLiteral container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
