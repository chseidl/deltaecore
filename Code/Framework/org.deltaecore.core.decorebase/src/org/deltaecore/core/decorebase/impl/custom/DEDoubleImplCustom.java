package org.deltaecore.core.decorebase.impl.custom;

import org.deltaecore.core.decorebase.impl.DEDoubleImpl;

public class DEDoubleImplCustom extends DEDoubleImpl {
	@Override
	public Class<?> getValueType() {
		return Double.class;
	}
}
