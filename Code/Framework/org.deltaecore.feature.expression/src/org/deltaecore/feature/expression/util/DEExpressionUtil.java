package org.deltaecore.feature.expression.util;

import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEBooleanValueExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEExpressionFactory;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;

public class DEExpressionUtil {
	public static Boolean getBooleanValue(DEExpression expression) {
		if (expression instanceof DEBooleanValueExpression) {
			DEBooleanValueExpression booleanValueExpression = (DEBooleanValueExpression) expression;
			
			return booleanValueExpression.isValue();
		}

		return null;
	}
	
	public static DEBooleanValueExpression booleanLiteral(boolean value) {
		DEBooleanValueExpression booleanValueExpression = DEExpressionFactory.eINSTANCE.createDEBooleanValueExpression();
		
		booleanValueExpression.setValue(value);
		
		return booleanValueExpression;
	}
	
	public static DEAndExpression and(DEExpression operand1, DEExpression operand2) {
		DEAndExpression andExpression = DEExpressionFactory.eINSTANCE.createDEAndExpression();
		
		andExpression.setOperand1(operand1);
		andExpression.setOperand2(operand2);
		
		return andExpression;
	}
	
	public static DEOrExpression or(DEExpression operand1, DEExpression operand2) {
		DEOrExpression orExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
		
		orExpression.setOperand1(operand1);
		orExpression.setOperand2(operand2);
		
		return orExpression;
	}
	
	public static DEImpliesExpression implies(DEExpression operand1, DEExpression operand2) {
		DEImpliesExpression impliesExpression = DEExpressionFactory.eINSTANCE.createDEImpliesExpression();
		
		impliesExpression.setOperand1(operand1);
		impliesExpression.setOperand2(operand2);
		
		return impliesExpression;
	}
	
	public static DEEquivalenceExpression equivalence(DEExpression operand1, DEExpression operand2) {
		DEEquivalenceExpression equivalenceExpression = DEExpressionFactory.eINSTANCE.createDEEquivalenceExpression();
		
		equivalenceExpression.setOperand1(operand1);
		equivalenceExpression.setOperand2(operand2);
		
		return equivalenceExpression;
	}
	
	public static DENotExpression not(DEExpression operand) {
		DENotExpression notExpression = DEExpressionFactory.eINSTANCE.createDENotExpression();
		notExpression.setOperand(operand);
		return notExpression;
	}
}
