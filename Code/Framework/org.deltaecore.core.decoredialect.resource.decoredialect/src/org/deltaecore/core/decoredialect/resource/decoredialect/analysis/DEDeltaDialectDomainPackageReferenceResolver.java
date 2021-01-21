/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.analysis;

import org.eclipse.emf.ecore.EPackage;

import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEDeltaDialectDomainPackageReferenceResolver implements org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolver<org.deltaecore.core.decoredialect.DEDeltaDialect, org.eclipse.emf.ecore.EPackage> {
	
	private org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEDeltaDialect, org.eclipse.emf.ecore.EPackage> delegate = new org.deltaecore.core.decoredialect.resource.decoredialect.analysis.DecoredialectDefaultResolverDelegate<org.deltaecore.core.decoredialect.DEDeltaDialect, org.eclipse.emf.ecore.EPackage>();
	
	public void resolve(String identifier, org.deltaecore.core.decoredialect.DEDeltaDialect container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolveResult<org.eclipse.emf.ecore.EPackage> result) {
		EPackage ePackage = EcoreResolverUtil.resolveEPackageFromRegistry(identifier);
		
		if (ePackage != null) {
			result.addMapping(identifier, ePackage);
		}
		
//		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public String deResolve(org.eclipse.emf.ecore.EPackage element, org.deltaecore.core.decoredialect.DEDeltaDialect container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
