/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;

public class DEVariableReferenceVariableReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEVariableReference, org.deltaecore.core.decore.DEVariableDeclaration> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEVariableReference container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.deltaecore.core.decore.DEVariableDeclaration> result) {
		DEVariableDeclaration variableDeclaration = DEDEcoreResolverUtil.resolveVariableReference(identifier, container);
		
		if (variableDeclaration != null) {
			result.addMapping(identifier, variableDeclaration);
		}
	}
	
	public String deResolve(org.deltaecore.core.decore.DEVariableDeclaration element, org.deltaecore.core.decore.DEVariableReference container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
