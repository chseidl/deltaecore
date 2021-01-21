package org.deltaecore.core.decore.impl.custom;

import java.util.List;

import org.deltaecore.core.decore.DEArgument;
import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decore.impl.DEArgumentImpl;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEParameter;

public class DEArgumentImplCustom extends DEArgumentImpl {
	@Override
	public Class<?> getExpectedJavaClass() {
		DEDeltaOperationCall deltaOperationCall = getDeltaOperationCall();
		List<DEArgument> arguments = deltaOperationCall.getArguments();
		
		int argumentIndex = arguments.indexOf(this);
		
		DEDeltaOperationDefinition deltaOperationDefinition = deltaOperationCall.getOperationDefinition();
		
		//If EMFText cannot find the name, it leaves behind an unresolved proxy.
		if (deltaOperationDefinition == null || deltaOperationDefinition.eIsProxy()) {
			return null;
		}
		
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
		
		if (argumentIndex >= parameters.size()) {
			//This is an error but it is picked up by a constraint.
			return null;
		}
		
		DEParameter parameter = parameters.get(argumentIndex);
		DEType parameterType = parameter.getType();
		return parameterType.getValueType();
	}
}
