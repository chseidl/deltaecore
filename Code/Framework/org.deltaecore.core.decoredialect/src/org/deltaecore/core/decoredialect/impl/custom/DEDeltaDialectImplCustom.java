package org.deltaecore.core.decoredialect.impl.custom;

import org.deltaecore.core.decoredialect.impl.DEDeltaDialectImpl;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

public class DEDeltaDialectImplCustom extends DEDeltaDialectImpl {
	@Override
	public EFactory getDomainFactory() {
		//Get the registered factory instance (not a dynamic one - would create dynamic EObjects)
		EPackage domainPackage = getDomainPackage();
		return domainPackage.getEFactoryInstance();
	}
}
