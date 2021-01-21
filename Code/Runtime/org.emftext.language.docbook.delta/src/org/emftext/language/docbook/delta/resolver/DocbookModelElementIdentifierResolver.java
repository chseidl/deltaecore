package org.emftext.language.docbook.delta.resolver;

import java.util.List;

import org.deltaecore.extension.resolution.DEAmbiguousDomainModelElementIdentifierException;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

//TODO: Implement reference resolver that uses the IDAttribute for the containing AttributeContainer
public class DocbookModelElementIdentifierResolver implements DEDomainModelElementIdentifierResolver {

	@Override
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromResources(String requestedIdentifier, List<Resource> referencableResources) throws DEAmbiguousDomainModelElementIdentifierException {
		return null;
	}

	@Override
	public EObject resolveDomainModelElementIdentifierToSingleEObjectFromModels(String requestedIdentifier, List<EObject> referencableModels) throws DEAmbiguousDomainModelElementIdentifierException {
		return null;
	}

	@Override
	public List<EObject> resolveDomainModelElementIdentifier(String requestedIdentifier, List<EObject> referencableModels) {
		return null;
	}

	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		return null;
	}
}
