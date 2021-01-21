package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.impl.DEVariableDeclarationImpl;
import org.deltaecore.core.decorebase.DEType;

public class DEVariableDeclarationImplCustom extends DEVariableDeclarationImpl {

	@Override
	public Object getValue() {
		// TODO: Temporary. Variables should have a separate value that can be set.
		DEExpression2 expression = getExpression();
		
		if (expression != null) {
			return expression.getValue();
		}

		return null;
	}

	@Override
	public Class<?> getExpectedJavaClass() {
		DEType type = getType();
		return type.getValueType();
	}
}
