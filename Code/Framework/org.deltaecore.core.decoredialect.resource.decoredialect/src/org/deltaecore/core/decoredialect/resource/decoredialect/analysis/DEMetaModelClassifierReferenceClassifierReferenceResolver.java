/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.analysis;

import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEMetaModelClassifierReferenceClassifierReferenceResolver implements org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolver<org.deltaecore.core.decorebase.DEMetaModelClassifierReference, org.eclipse.emf.ecore.EClassifier> {
	
	private org.deltaecore.core.decorebase.resource.decorebase.analysis.DEMetaModelClassifierReferenceClassifierReferenceResolver delegate = new org.deltaecore.core.decorebase.resource.decorebase.analysis.DEMetaModelClassifierReferenceClassifierReferenceResolver();
	
	@Override
	public void resolve(String identifier, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectReferenceResolveResult<org.eclipse.emf.ecore.EClassifier> result) {
		DEDeltaDialect dialect = (DEDeltaDialect) EcoreUtil.getRootContainer(container);
		EPackage domainPackage = dialect.getDomainPackage();
		
		EClassifier eClassifier = DEDEcoreBaseUtil.resolveEClassifierIdentifierInMetaModel(identifier, domainPackage);
		
		if (eClassifier != null) {
			result.addMapping(identifier, eClassifier);
		}		
	}
	
	public String deResolve(org.eclipse.emf.ecore.EClassifier element, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
