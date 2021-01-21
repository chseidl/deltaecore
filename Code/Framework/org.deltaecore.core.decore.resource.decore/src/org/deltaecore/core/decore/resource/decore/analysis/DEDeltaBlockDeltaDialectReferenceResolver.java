/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import java.util.Map;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

public class DEDeltaBlockDeltaDialectReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEDeltaBlock, org.deltaecore.core.decoredialect.DEDeltaDialect> {
	
	public void resolve(String identifier, org.deltaecore.core.decore.DEDeltaBlock container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.deltaecore.core.decoredialect.DEDeltaDialect> result) {
		//Resolve from registry.
		DEDeltaDialect dialect = DEDeltaDialectExtensionRegistry.getDeltaDialect(identifier);
		
		if (dialect != null) {
			result.addMapping(identifier, dialect);
		}
	}
	
	public String deResolve(org.deltaecore.core.decoredialect.DEDeltaDialect element, org.deltaecore.core.decore.DEDeltaBlock container, EReference reference) {
		EPackage ePackage = element.getDomainPackage();
		
		if (ePackage == null) {
			return "";
		}
		
		return ePackage.getNsURI();
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
