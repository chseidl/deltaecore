package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DENullLiteralImpl;

public class DENullLiteralImplCustom extends DENullLiteralImpl {

	@Override
	public Class<?> getValueJavaClass() {
		return null;
	}

	@Override
	public Object getValue() {
		return null;
	}
}
