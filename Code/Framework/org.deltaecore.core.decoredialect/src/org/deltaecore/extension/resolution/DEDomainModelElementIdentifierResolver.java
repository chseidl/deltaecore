package org.deltaecore.extension.resolution;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public interface DEDomainModelElementIdentifierResolver {
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromResources(String requestedIdentifier, List<Resource> referencableResources) throws DEAmbiguousDomainModelElementIdentifierException;
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromModels(String requestedIdentifier, List<EObject> referencableModels) throws DEAmbiguousDomainModelElementIdentifierException;
	public List<EObject> resolveDomainModelElementIdentifier(String requestedIdentifier, List<EObject> referencableModels);
	
	public String deresolveDomainModelElement(EObject domainModelElement);
}