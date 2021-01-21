package org.deltaecore.core.decoredialect.validation;

import org.deltaecore.core.decorebase.DEInteger;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decoredialect.DEInsertDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DENamedParameter;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEInsertDeltaOperationIndexParameterIntegerConstraint extends AbstractConstraint<DEInsertDeltaOperationDefinition> {

	@Override
	protected IStatus doValidate(DEInsertDeltaOperationDefinition insertDeltaOperationDefinition) {
		DENamedParameter indexParameter = insertDeltaOperationDefinition.getIndex();
		DEType indexParameterType = indexParameter.getType();
		
		if (!(indexParameterType instanceof DEInteger)) {
			String message = "The index parameter of an insert operation must be of type Integer.";
			return createErrorStatus(message, indexParameter);
		}
		
		return createSuccessStatus();
	}
}
