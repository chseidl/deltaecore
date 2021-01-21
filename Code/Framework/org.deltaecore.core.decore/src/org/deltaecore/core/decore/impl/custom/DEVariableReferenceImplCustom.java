package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.impl.DEVariableReferenceImpl;
import org.deltaecore.core.decorebase.DEType;

public class DEVariableReferenceImplCustom extends DEVariableReferenceImpl {
	@Override
	public Object getValue() {
		DEVariableDeclaration variable = getVariable();
		
		if (variable != null) {
			return variable.getValue();
		}
		
		return null;
	}

	@Override
	public Class<?> getValueJavaClass() {
		//Value type can even be determined if there is no value set.
		DEVariableDeclaration variable = getVariable();
		
		if (variable != null) {
			DEType type = variable.getType();
			
			if (type != null) {
				return type.getValueType();
			}
		}
		
		return null;
	}

}
