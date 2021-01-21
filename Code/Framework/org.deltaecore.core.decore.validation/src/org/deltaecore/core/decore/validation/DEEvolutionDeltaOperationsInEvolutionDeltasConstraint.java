package org.deltaecore.core.decore.validation;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decorebase.DEModificationType;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEEvolutionDeltaOperationsInEvolutionDeltasConstraint extends AbstractConstraint<DEDeltaOperationCall> {

	@Override
	protected IStatus doValidate(DEDeltaOperationCall deltaOperationCall) {
		
		DEDeltaOperationDefinition deltaOperationDefinition = deltaOperationCall.getOperationDefinition();
		DEModificationType deltaOperationModificationType = deltaOperationDefinition.getModificationType();
		
		if (deltaOperationModificationType == DEModificationType.EVOLUTION) {
			EObject container = deltaOperationCall.eContainer();
			
			if (container instanceof DEDelta) {
				DEDelta delta = (DEDelta) container;
				
				DEModificationType deltaModificationType = delta.getModificationType();
				
				//Evolution delta operations may only be used within evolution delta modules.
				if (deltaModificationType != DEModificationType.EVOLUTION) {
					return createErrorStatus("Evolution delta operations may only be used within evolution delta modules.", deltaOperationCall);
				}
			}
		}
		
		return createSuccessStatus();
	}
}
