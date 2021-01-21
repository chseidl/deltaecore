package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEBooleanLiteralImpl;

public class DEBooleanLiteralImplCustom extends DEBooleanLiteralImpl {
	@Override
	public Class<?> getValueJavaClass() {
		return Boolean.class;
	}
}
