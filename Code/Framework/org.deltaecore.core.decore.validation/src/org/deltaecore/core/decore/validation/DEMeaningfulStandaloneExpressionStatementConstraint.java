package org.deltaecore.core.decore.validation;

import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEStandaloneExpressionStatement;
import org.deltaecore.core.decore.DEValue;
import org.deltaecore.core.decore.DEVariableReference;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.model.ConstraintSeverity;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEMeaningfulStandaloneExpressionStatementConstraint extends AbstractConstraint<DEStandaloneExpressionStatement> {
	@Override
	protected ConstraintSeverity getSeverity() {
		return ConstraintSeverity.WARNING;
	}
	
	@Override
	protected IStatus doValidate(DEStandaloneExpressionStatement standaloneExpressionStatement) {
		DEExpression2 expression = standaloneExpressionStatement.getExpression();
		
		//Opt-out: variable references and values make no sense when used standalone
		if ((expression instanceof DEVariableReference) || (expression instanceof DEValue)) {
			return createErrorStatus("The expression has no effect when used on its own.", standaloneExpressionStatement);
		}
		
		return createSuccessStatus();
	}
}
