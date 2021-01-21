/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.suite.mapping.resource.demapping.analysis;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EReference;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEDeltaInvokationDeltaReferenceResolver implements org.deltaecore.suite.mapping.resource.demapping.IDemappingReferenceResolver<org.deltaecore.core.decore.DEDeltaInvokation, org.deltaecore.core.decore.DEDelta> {
	
	private org.deltaecore.suite.mapping.resource.demapping.analysis.DemappingDefaultResolverDelegate<org.deltaecore.core.decore.DEDeltaInvokation, org.deltaecore.core.decore.DEDelta> delegate = new org.deltaecore.suite.mapping.resource.demapping.analysis.DemappingDefaultResolverDelegate<org.deltaecore.core.decore.DEDeltaInvokation, org.deltaecore.core.decore.DEDelta>();
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEDeltaInvokation container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.suite.mapping.resource.demapping.IDemappingReferenceResolveResult<org.deltaecore.core.decore.DEDelta> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public String deResolve(org.deltaecore.core.decore.DEDelta element, org.deltaecore.core.decore.DEDeltaInvokation container, EReference reference) {
		IFile mappingFile = EcoreResolverUtil.resolveRelativeFileFromEObject(container);
		IFile deltaFile = EcoreResolverUtil.resolveRelativeFileFromEObject(element);
		IPath relativePath = ResourceUtil.makeResourcePathRelativeToFile(deltaFile, mappingFile);
		
		return relativePath.toString();
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
