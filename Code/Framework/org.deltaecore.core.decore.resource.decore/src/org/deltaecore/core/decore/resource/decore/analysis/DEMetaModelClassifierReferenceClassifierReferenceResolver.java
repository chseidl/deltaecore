/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

public class DEMetaModelClassifierReferenceClassifierReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decorebase.DEMetaModelClassifierReference, org.eclipse.emf.ecore.EClassifier> {
	
	public void resolve(String identifier, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.eclipse.emf.ecore.EClassifier> result) {
		DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(container);
		EPackage domainPackage = block.getDomainPackage();
		
		EClassifier eClassifier = DEDEcoreBaseUtil.resolveEClassifierIdentifierInMetaModel(identifier, domainPackage);
		
		if (eClassifier != null) {
			result.addMapping(identifier, eClassifier);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EClassifier element, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
