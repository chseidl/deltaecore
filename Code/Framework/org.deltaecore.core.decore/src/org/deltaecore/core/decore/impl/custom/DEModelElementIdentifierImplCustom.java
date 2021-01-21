package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEModelElementIdentifierImpl;
import org.eclipse.emf.ecore.EObject;

public class DEModelElementIdentifierImplCustom extends DEModelElementIdentifierImpl {

	private EObject cachedDomainModelElement;
	
	public EObject getCachedDomainModelElement() {
		return cachedDomainModelElement;
	}

	public void setCachedDomainModelElement(EObject cachedDomainModelElement) {
		this.cachedDomainModelElement = cachedDomainModelElement;
	}
	
	@Override
	public Object getValue() {
		return getCachedDomainModelElement();
	}
}
