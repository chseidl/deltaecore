package org.deltaecore.core.extension.resolution;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public abstract class DEAbstractIteratingDomainModelElementIdentifierResolver extends DEAbstractNonCompilingDomainModelElementIdentifierResolver {

	@Override
	protected void resolveIdentifier(String identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		for (EObject referencableModel : referencableModels) {
			//Don't forget to check the model itself for it may match as well.
			if (matches(identifier, referencableModel)) {
				resolvedElements.add(referencableModel);
			}
			
			Iterator<EObject> iterator = referencableModel.eAllContents();
			
			while(iterator.hasNext()) {
				EObject element = iterator.next();
				
				if (matches(identifier, element)) {
					resolvedElements.add(element);
				}
	
			}
		}
	}

	//Allows customization of resolution process for model identifiers.
	protected abstract boolean matches(String identifier, EObject domainModelElement);
}
