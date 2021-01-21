package org.eclipse.emf.ecore.delta.resolver;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.extension.resolution.DEAbstractNonCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.ecoreid.EcoreID;
import org.emftext.language.ecoreid.EcoreIDList;
import org.emftext.language.ecoreid.resource.ecoreid.util.EcoreidResourceUtil;
import org.emftext.language.ecoreid.util.EcoreIDResolverUtil;

public class EcoreIDResolver extends DEAbstractNonCompilingDomainModelElementIdentifierResolver {
	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void resolveIdentifier(String identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		EcoreIDList ecoreIDList = EcoreidResourceUtil.getResourceContent(identifier);
		List<EcoreID> ecoreIDs = ecoreIDList.getEcoreIDs();
		if (ecoreIDs.isEmpty()) {
			return;
		}
		
		EcoreID ecoreID = ecoreIDs.get(0);
		
		if (ecoreID == null) {
			return;
		}
		
		
		List<ENamedElement> resolutionContexts = new ArrayList<ENamedElement>();
		
		for (EObject referencableModel : referencableModels) {
			if (referencableModel instanceof ENamedElement) {
				ENamedElement eNamedElement = (ENamedElement) referencableModel;
				resolutionContexts.add(eNamedElement);
			}
		}
		
		List<ENamedElement> modelElements = EcoreIDResolverUtil.rawResolveEcoreID(ecoreID, resolutionContexts, null);
		
		resolvedElements.addAll(modelElements);
	}
}
