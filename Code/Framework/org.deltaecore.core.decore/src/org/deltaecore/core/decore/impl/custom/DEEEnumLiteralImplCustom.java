package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEEEnumLiteralImpl;
import org.eclipse.emf.ecore.EEnumLiteral;

public class DEEEnumLiteralImplCustom extends DEEEnumLiteralImpl {
	@Override
	public Object getValue() {
		EEnumLiteral enumLiteral = getEnumLiteral();
		
		if (enumLiteral == null) {
			return null;
		}
		
		return enumLiteral.getInstance();
	}
}
