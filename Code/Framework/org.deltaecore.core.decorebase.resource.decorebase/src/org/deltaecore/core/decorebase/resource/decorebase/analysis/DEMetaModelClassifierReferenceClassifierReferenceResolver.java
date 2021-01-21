/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decorebase.resource.decorebase.analysis;

public class DEMetaModelClassifierReferenceClassifierReferenceResolver implements org.deltaecore.core.decorebase.resource.decorebase.IDecorebaseReferenceResolver<org.deltaecore.core.decorebase.DEMetaModelClassifierReference, org.eclipse.emf.ecore.EClassifier> {
	
	public void resolve(String identifier, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decorebase.resource.decorebase.IDecorebaseReferenceResolveResult<org.eclipse.emf.ecore.EClassifier> result) {
		//Implemented by resolvers of concrete sub-syntaxes. 
	}
	
	public String deResolve(org.eclipse.emf.ecore.EClassifier element, org.deltaecore.core.decorebase.DEMetaModelClassifierReference container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
}
