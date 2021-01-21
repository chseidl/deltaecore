/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEDataTypeInitializerDataTypeReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEDataTypeInitializer, org.eclipse.emf.ecore.EDataType> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEDataTypeInitializer container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.eclipse.emf.ecore.EDataType> result) {
		DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(container);
		EDataType eDataType = DEDEcoreResolverUtil.resolveEDataType(identifier, delta);
		
		if (eDataType != null) {
			result.addMapping(identifier, eDataType);
		}
	}
	
	public String deResolve(org.eclipse.emf.ecore.EDataType element, org.deltaecore.core.decore.DEDataTypeInitializer container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
