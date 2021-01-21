package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEDoubleLiteralImpl;

public class DEDoubleLiteralImplCustom extends DEDoubleLiteralImpl {
	@Override
	public Class<?> getValueJavaClass() {
		return Double.class;
	}
}
