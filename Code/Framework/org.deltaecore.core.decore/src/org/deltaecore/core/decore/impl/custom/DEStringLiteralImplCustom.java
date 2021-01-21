package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEStringLiteralImpl;

public class DEStringLiteralImplCustom extends DEStringLiteralImpl {
	@Override
	public Class<?> getValueJavaClass() {
		return String.class;
	}
}
