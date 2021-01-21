package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEStatement;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEVariableNameUniqueInScopeConstraint extends AbstractConstraint<DEVariableDeclaration> {

	@Override
	protected IStatus doValidate(DEVariableDeclaration variableDeclaration) {
		String variableName = variableDeclaration.getName();
		DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(variableDeclaration);
		
//		DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(variableDeclaration);
		
		List<DEStatement> statements = block.getStatements();
		
		for (DEStatement statement : statements) {
			if (statement instanceof DEVariableDeclaration) {
				DEVariableDeclaration otherVariableDeclaration = (DEVariableDeclaration) statement;
				
				//Pointer equality intended!
				if (variableDeclaration == otherVariableDeclaration) {
					//If we hit the variable declaration itself, then nothing _before_ it
					//had the same name. The variable declarations that follow and have the
					//same name will be picked up when checking them.
					return createSuccessStatus();
				}
				
				String otherVariableName = otherVariableDeclaration.getName();
				
				if (variableName.equals(otherVariableName)) {
					String message = "The variable \"" + variableName + "\" is already declared in this scope.";
					return createErrorStatus(message, variableDeclaration);
				}
			}
		}
		return createSuccessStatus();
	}
}
