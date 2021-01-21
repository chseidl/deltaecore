package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEIntegerLiteralImpl;

public class DEIntegerLiteralImplCustom extends DEIntegerLiteralImpl {
	@Override
	public Class<?> getValueJavaClass() {
		return Integer.class;
	}
}
