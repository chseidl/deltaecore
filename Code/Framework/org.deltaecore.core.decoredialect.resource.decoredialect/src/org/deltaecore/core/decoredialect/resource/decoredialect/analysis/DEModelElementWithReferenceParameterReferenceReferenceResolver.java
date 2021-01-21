/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.analysis;

import org.deltaecore.core.decoredialect.util.DEDEcoreDialectResolverUtil;
import org.eclipse.emf.ecore.EReference;

public class DEModelElementWithReferenceParameterReferenceReferenceResolver implements org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolver<org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter, org.eclipse.emf.ecore.EReference> {
	
//	private org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter, org.eclipse.emf.ecore.EReference> delegate = new org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter, org.eclipse.emf.ecore.EReference>();
	
	public void resolve(String identifier, org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolveResult<org.eclipse.emf.ecore.EReference> result) {
//		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
		
		EReference eReference = DEDEcoreDialectResolverUtil.resolveReferenceInModelElementWithReferenceParameter(identifier, container);
		
		if (eReference != null) {
			result.addMapping(identifier, eReference);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EReference element, org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter container, org.eclipse.emf.ecore.EReference reference) {
//		return delegate.deResolve(element, container, reference);
		
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
