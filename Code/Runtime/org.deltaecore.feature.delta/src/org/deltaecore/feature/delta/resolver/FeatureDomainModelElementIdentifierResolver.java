package org.deltaecore.feature.delta.resolver;

import java.util.List;

import org.deltaecore.core.extension.resolution.DEAbstractCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;

public class FeatureDomainModelElementIdentifierResolver extends DEAbstractCompilingDomainModelElementIdentifierResolver<FeatureIdentifier> {
	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		return FeatureIdentifier.deresolve(domainModelElement);
	}

	@Override
	protected FeatureIdentifier compileIdentifier(String rawIdentifier) {
		return new FeatureIdentifier(rawIdentifier);
	}

	@Override
	protected void resolveIdentifier(FeatureIdentifier identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		for (EObject referencableModel : referencableModels) {
			EObject resolvedElement = identifier.resolve(referencableModel);
			
			if (resolvedElement != null) {
				resolvedElements.add(resolvedElement);
			}
		}
	}

}
