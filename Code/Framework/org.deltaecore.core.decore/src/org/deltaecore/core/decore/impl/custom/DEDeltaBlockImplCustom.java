package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEDeltaBlockImpl;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.emf.ecore.EPackage;

public class DEDeltaBlockImplCustom extends DEDeltaBlockImpl {
	@Override
	public EPackage getDomainPackage() {
		DEDeltaDialect deltaDialect = getDeltaDialect();
		return deltaDialect.getDomainPackage();
	}
}
