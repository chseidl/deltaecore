package org.deltaecore.core.extension.resolution;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEDefaultDomainModelElementIdentifierResolver extends DEAbstractIteratingDomainModelElementIdentifierResolver {

	/**
	 * Default resolution strategy interprets the requested identifier as id of a model element and compares it to
	 * the respective field of the element flagged as "id".
	 */
	@Override
	protected boolean matches(String identifier, EObject modelElement) {
		return matchesById(identifier, modelElement);
	}

	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		if (domainModelElement == null) {
			return null;
		}
		
		return EcoreUtil.getID(domainModelElement);
	}
}
