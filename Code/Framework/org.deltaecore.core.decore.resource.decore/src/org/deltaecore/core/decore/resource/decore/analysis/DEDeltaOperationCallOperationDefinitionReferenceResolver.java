/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.analysis;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;

public class DEDeltaOperationCallOperationDefinitionReferenceResolver implements org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolver<org.deltaecore.core.decore.DEDeltaOperationCall, org.deltaecore.core.decoredialect.DEDeltaOperationDefinition> {
	
	@Override
	public void resolve(String identifier, org.deltaecore.core.decore.DEDeltaOperationCall container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.core.decore.resource.decore.IDecoreReferenceResolveResult<org.deltaecore.core.decoredialect.DEDeltaOperationDefinition> result) {
		DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(container);
		DEDeltaDialect deltaDialect = block.getDeltaDialect();
		
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		
		for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {
			String name = deltaOperationDefinition.getName();
			
			if (identifier.equals(name)) {
				result.addMapping(identifier, deltaOperationDefinition);
			}
		}
	}
	
	@Override
	public String deResolve(org.deltaecore.core.decoredialect.DEDeltaOperationDefinition element, org.deltaecore.core.decore.DEDeltaOperationCall container, org.eclipse.emf.ecore.EReference reference) {
		return element.getName();
	}
	
	@Override
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
}
