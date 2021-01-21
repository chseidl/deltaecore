package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEArgument;
import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEParameter;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEValidDeltaOperationCallArgumentsConstraint extends AbstractConstraint<DEDeltaOperationCall> {

	@Override
	protected IStatus doValidate(DEDeltaOperationCall deltaOperationCall) {
		DEDeltaOperationDefinition deltaOperationDefinition = deltaOperationCall.getOperationDefinition();
		
		if (deltaOperationDefinition == null) {
			String message = "Unknown delta operation definition.";
			return createErrorStatus(message, deltaOperationCall);
		}
		
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
		List<DEArgument> arguments = deltaOperationCall.getArguments();
		
		final int numberOfParameters = parameters.size();
		final int numberOfArguments = arguments.size();
		
		if (numberOfParameters != numberOfArguments) {
			String message = "The delta operation \"" + deltaOperationDefinition.getName() + "\" requires " + numberOfParameters + " parameter(s) but " + numberOfArguments + " argument(s) were/was supplied.";
			return createErrorStatus(message, deltaOperationCall);
		}
		
		//NOTE: Type of arguments is checked in separate constraint.
		
		return createSuccessStatus();
	}
}
