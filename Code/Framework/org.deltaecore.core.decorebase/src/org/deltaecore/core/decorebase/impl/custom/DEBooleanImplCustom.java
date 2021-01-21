package org.deltaecore.core.decorebase.impl.custom;

import org.deltaecore.core.decorebase.impl.DEBooleanImpl;

public class DEBooleanImplCustom extends DEBooleanImpl {

	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}
}
