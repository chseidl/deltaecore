/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.analysis;

import org.deltaecore.core.decoredialect.util.DEDEcoreDialectResolverUtil;
import org.eclipse.emf.ecore.EAttribute;

public class DEModelElementWithAttributeParameterAttributeReferenceResolver implements org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolver<org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter, org.eclipse.emf.ecore.EAttribute> {
	
//	private org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter, org.eclipse.emf.ecore.EAttribute> delegate = new org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter, org.eclipse.emf.ecore.EAttribute>();
	
	public void resolve(String identifier, org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolveResult<org.eclipse.emf.ecore.EAttribute> result) {
//		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
		
		EAttribute attribute = DEDEcoreDialectResolverUtil.resolveAttributeInModelElementWithAttributeParameter(identifier, container);
		
		if (attribute != null) {
			result.addMapping(identifier, attribute);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EAttribute element, org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter container, org.eclipse.emf.ecore.EReference reference) {
//		return delegate.deResolve(element, container, reference);
		
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
