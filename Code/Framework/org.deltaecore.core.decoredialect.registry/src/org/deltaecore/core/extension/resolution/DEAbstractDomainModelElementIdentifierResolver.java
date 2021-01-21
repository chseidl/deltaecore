package org.deltaecore.core.extension.resolution;

import java.util.LinkedList;
import java.util.List;

import org.deltaecore.extension.resolution.DEAmbiguousDomainModelElementIdentifierException;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public abstract class DEAbstractDomainModelElementIdentifierResolver<T> implements DEDomainModelElementIdentifierResolver {
	
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromResources(String requestedIdentifier, List<Resource> referencableResources) throws DEAmbiguousDomainModelElementIdentifierException {
		// Use the models (in their current state) that were originally marked as being modified in the block and, thus, are used to resolve identifiers.
		List<EObject> models = new LinkedList<EObject>();
		
		for (Resource resource : referencableResources) {
			List<EObject> contents = resource.getContents();
			models.addAll(contents);
		}
		
		return resolveDomainModelElementIdentifierToSingleEObjectFromModels(requestedIdentifier, models);
	}
	
	@Override
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromModels(String requestedIdentifier, List<EObject> referencableModels) throws DEAmbiguousDomainModelElementIdentifierException {
		List<EObject> resolvedElements = resolveDomainModelElementIdentifier(requestedIdentifier, referencableModels);
		
		if (resolvedElements.isEmpty()) {
			return null;
		}
		
		if (resolvedElements.size() > 1) {
			throw new DEAmbiguousDomainModelElementIdentifierException(requestedIdentifier, resolvedElements);
		}
	
		return resolvedElements.get(0);
	}

	protected boolean matchesById(String requestedIdentifier, EObject modelElement) {
		String id = EcoreUtil.getID(modelElement);
		
		if (id == null) {
			//An EObject does not necessarily have an attribute marked "id". 
			return false;
		}
		
		if (requestedIdentifier.equals(id)) {
			return true;
		}
		
		return false;
	}
}