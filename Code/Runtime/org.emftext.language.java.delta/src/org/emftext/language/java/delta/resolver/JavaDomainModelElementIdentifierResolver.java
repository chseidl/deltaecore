package org.emftext.language.java.delta.resolver;

import java.util.List;

import org.deltaecore.extension.resolution.DEAbstractCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.ecore.EObject;

public class JavaDomainModelElementIdentifierResolver extends DEAbstractCompilingDomainModelElementIdentifierResolver<JavaIdentifier> /*DEDefaultDomainModelElementIdentifierResolver*/ {

	@Override
	protected JavaIdentifier compileIdentifier(String rawIdentifier) {
		return new JavaIdentifier(rawIdentifier);
	}

	@Override
	protected void resolveIdentifier(JavaIdentifier identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		for (EObject referencableModel : referencableModels) {
			EObject resolvedElement = identifier.resolve(referencableModel);
			
			if (resolvedElement != null) {
				resolvedElements.add(resolvedElement);
			}
		}
	}

	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		return JavaIdentifier.deresolve(domainModelElement);
	}
}
