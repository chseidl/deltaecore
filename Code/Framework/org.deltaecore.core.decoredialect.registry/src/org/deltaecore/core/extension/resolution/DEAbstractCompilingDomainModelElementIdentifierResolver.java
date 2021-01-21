package org.deltaecore.core.extension.resolution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public abstract class DEAbstractCompilingDomainModelElementIdentifierResolver<T> extends DEAbstractDomainModelElementIdentifierResolver<T> {
	
	@Override
	public List<EObject> resolveDomainModelElementIdentifier(String rawIdentifier, List<EObject> referencableModels) {
		List<EObject> resolvedElements = new ArrayList<EObject>();
		
		if (rawIdentifier == null) {
			return resolvedElements;
		}
		
		T identifier = compileIdentifier(rawIdentifier);
		resolveIdentifier(identifier, referencableModels, resolvedElements);
		
		return resolvedElements;
	}

	protected abstract T compileIdentifier(String rawIdentifier);
	protected abstract void resolveIdentifier(T identifier, List<EObject> referencableModels, List<EObject> resolvedElements);
}
