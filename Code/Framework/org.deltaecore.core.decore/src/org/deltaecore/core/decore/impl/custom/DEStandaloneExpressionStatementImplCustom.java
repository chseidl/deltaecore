package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEStandaloneExpressionStatementImpl;

public class DEStandaloneExpressionStatementImplCustom extends DEStandaloneExpressionStatementImpl {
	@Override
	public Class<?> getExpectedJavaClass() {
		//Allow any kind of value as the value is discarded anyways.
		return null;
	}
}
