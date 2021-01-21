package eu.vicci.ecosystem.componentfaultdiagram.cfd.delta.resolver;

import java.util.List;

import org.deltaecore.core.extension.resolution.DEAbstractCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDObject;

public class CfdDomainModelElementIdentifierResolver extends DEAbstractCompilingDomainModelElementIdentifierResolver<CFDIdentifier> {
	@Override
	protected CFDIdentifier compileIdentifier(String rawIdentifier) {
		return new CFDIdentifier(rawIdentifier);
	}

	@Override
	protected void resolveIdentifier(CFDIdentifier identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		for (EObject referencableModel : referencableModels) {
			if (referencableModel instanceof CFDDiagram) {
				CFDDiagram diagram = (CFDDiagram) referencableModel;
				CFDObject matchingObject = identifier.findMatch(diagram);
				
				if (matchingObject != null) {
					resolvedElements.add(matchingObject);
				}
			}
		}
	}

	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		if (domainModelElement instanceof CFDObject) {
			CFDObject object = (CFDObject) domainModelElement;
			CFDIdentifier identifier = new CFDIdentifier(object);
			
			return identifier.toString();
		}
		
		return null;
	}
}
