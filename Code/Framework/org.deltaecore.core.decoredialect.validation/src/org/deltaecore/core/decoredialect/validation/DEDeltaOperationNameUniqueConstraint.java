package org.deltaecore.core.decoredialect.validation;

import java.util.List;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEDeltaOperationNameUniqueConstraint extends AbstractConstraint<DEDeltaOperationDefinition> {

	@Override
	protected IStatus doValidate(DEDeltaOperationDefinition deltaOperationDefinition) {
		String deltaOperationName = deltaOperationDefinition.getName();
		DEDeltaDialect deltaDialect = (DEDeltaDialect) EcoreUtil.getRootContainer(deltaOperationDefinition);
		
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		
		for (DEDeltaOperationDefinition otherDeltaOperationDefinition : deltaOperationDefinitions) {
			
			//Pointer equality intended!
			if (deltaOperationDefinition == otherDeltaOperationDefinition) {
				//If we hit the delta operation definition itself, then nothing _before_ it
				//had the same name. The delta operation definitions that follow and have the
				//same name will be picked up when checking them.
				return createSuccessStatus();
			}
			
			String otherDeltaOperationName = otherDeltaOperationDefinition.getName();
			
			if (deltaOperationName.equals(otherDeltaOperationName)) {
				String message = "The delta operation \"" + deltaOperationName + "\" is already declared in this scope.";
				return createErrorStatus(message, deltaOperationDefinition);
			}
		}
		
		return createSuccessStatus();
	}
}
