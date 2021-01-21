package org.eclipse.uml2.uml.delta.resolver;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.extension.resolution.DEAbstractNonCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;
import org.emftext.language.umlid.UmlID;
import org.emftext.language.umlid.UmlIDList;
import org.emftext.language.umlid.resource.umlid.util.UmlidResourceUtil;

public class UmlIDResolver extends DEAbstractNonCompilingDomainModelElementIdentifierResolver {
	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void resolveIdentifier(String identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		UmlIDList umlIDList = UmlidResourceUtil.getResourceContent(identifier);
		List<UmlID> umlIDs = umlIDList.getIds();
		if (umlIDs.isEmpty()) {
			return;
		}
		
		UmlID umlID = umlIDs.get(0);
		
		if (umlID == null) {
			return;
		}
		
		
		List<Model> resolutionContexts = new ArrayList<Model>();
		
		//Resolve within UML models only
		for (EObject referencableModel : referencableModels) {
			if (referencableModel instanceof Model) {
				Model model = (Model) referencableModel;
				resolutionContexts.add(model);
			}
		}
		
//		try {
//			NamedElement modelElement = UmlIDResolverUtil.resolveUmlID(umlID, resolutionContexts);
//			
//			if (modelElement != null) {
//				resolvedElements.add(modelElement);
//			}
//		} catch(Exception e) {
////			e.printStackTrace();
//		}
	}
}
