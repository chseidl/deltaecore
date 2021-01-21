package org.deltaecore.core.extension.resolution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public abstract class DEAbstractNonCompilingDomainModelElementIdentifierResolver extends DEAbstractDomainModelElementIdentifierResolver<String> {
	@Override
	public List<EObject> resolveDomainModelElementIdentifier(String identifier, List<EObject> referencableModels) {
		List<EObject> resolvedElements = new ArrayList<EObject>();
		
		if (identifier == null) {
			return resolvedElements;
		}
		
		resolveIdentifier(identifier, referencableModels, resolvedElements);
		
		return resolvedElements;
	}

	protected abstract void resolveIdentifier(String identifier, List<EObject> referencableModels, List<EObject> resolvedElements);
}
