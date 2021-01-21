/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DEStructuralFeatureReferenceStructuralFeatureReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEStructuralFeatureReference, org.eclipse.emf.ecore.EStructuralFeature> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEStructuralFeatureReference container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.eclipse.emf.ecore.EStructuralFeature> result) {
		EStructuralFeature structuralFeature = DEDEcoreResolverUtil.resolveStructuralFeatureInNamedArgument(identifier, container);
		
		if (structuralFeature != null) {
			result.addMapping(identifier, structuralFeature);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EStructuralFeature element, org.deltaecore.core.decore.DEStructuralFeatureReference container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
