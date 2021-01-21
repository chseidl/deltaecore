package org.deltaecore.feature.expression.util;

import java.security.InvalidParameterException;

import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENestedExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEExpressionToCNFConverter {
	public static DEExpression convertToCNF(DEExpression expression) {
		DEExpression convertedExpression = EcoreUtil.copy(expression);
		
		convertedExpression = removeNestedExpressions(convertedExpression);
		convertedExpression = replaceEquivalences(convertedExpression);
		convertedExpression = replaceImplications(convertedExpression);
		convertedExpression = moveNegationsInwards(convertedExpression);
		convertedExpression = distributeDisjunctions(convertedExpression);

		return convertedExpression;
	}
	
	protected static DEExpression removeNestedExpressions(DEExpression expression) {
		if (expression instanceof DENestedExpression) {
			DENestedExpression nestedExpression = (DENestedExpression) expression;
			return removeNestedExpressions(nestedExpression.getOperand());
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			binaryExpression.setOperand1(removeNestedExpressions(binaryExpression.getOperand1()));
			binaryExpression.setOperand2(removeNestedExpressions(binaryExpression.getOperand2()));
			
			return binaryExpression;
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			unaryExpression.setOperand(removeNestedExpressions(unaryExpression.getOperand()));
			
			return unaryExpression;
		}
		
		//All atoms can stay as they are.
		return expression;
	}
	
	protected static DEExpression replaceEquivalences(DEExpression expression) {
		if (expression instanceof DEEquivalenceExpression) {
			//lhs <-> rhs === lhs -> rhs && rhs -> lhs
			DEEquivalenceExpression equivalence = (DEEquivalenceExpression) expression;
			
			DEExpression lhs = replaceEquivalences(equivalence.getOperand1());
			DEExpression rhs = replaceEquivalences(equivalence.getOperand2());
			
			DEImpliesExpression implies1 = DEExpressionUtil.implies(lhs, rhs);
			DEImpliesExpression implies2 = DEExpressionUtil.implies(EcoreUtil.copy(rhs), EcoreUtil.copy(lhs));

			return DEExpressionUtil.and(implies1, implies2);
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			binaryExpression.setOperand1(replaceEquivalences(binaryExpression.getOperand1()));
			binaryExpression.setOperand2(replaceEquivalences(binaryExpression.getOperand2()));
			
			return binaryExpression;
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			unaryExpression.setOperand(replaceEquivalences(unaryExpression.getOperand()));
			
			return unaryExpression;
		}
		
		//All atoms can stay as they are.
		return expression;
	}
	
	protected static DEExpression replaceImplications(DEExpression expression) {
		//Assumption: no more equivalences in input
		if (expression instanceof DEEquivalenceExpression) {
			throw new InvalidParameterException();
		}
		
		if (expression instanceof DEImpliesExpression) {
			//lhs -> rhs === !lhs || rhs
			DEImpliesExpression implies = (DEImpliesExpression) expression;
			
			DEExpression lhs = replaceImplications(implies.getOperand1());
			DEExpression rhs = replaceImplications(implies.getOperand2());
			
			return DEExpressionUtil.or(DEExpressionUtil.not(lhs), rhs);
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			binaryExpression.setOperand1(replaceImplications(binaryExpression.getOperand1()));
			binaryExpression.setOperand2(replaceImplications(binaryExpression.getOperand2()));
			
			return binaryExpression;
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			unaryExpression.setOperand(replaceImplications(unaryExpression.getOperand()));
			
			return unaryExpression;
		}
		
		//All atoms can stay as they are.
		return expression;
	}
	
	protected static DEExpression moveNegationsInwards(DEExpression expression) {
		//Assumption: no more equivalences or implications in input
		if (expression instanceof DEEquivalenceExpression || expression instanceof DEImpliesExpression) {
			throw new InvalidParameterException();
		}
		
		if (expression instanceof DENotExpression) {
			DENotExpression not = (DENotExpression) expression;
		
			DEExpression operand = not.getOperand();
			
			if (operand instanceof DENotExpression) {
				//Eliminate double negation
				DENotExpression notOperand = (DENotExpression) operand;
				DEExpression notOperandOperand = notOperand.getOperand();
				
				return moveNegationsInwards(notOperandOperand);
			}
			
			if (operand instanceof DEBinaryExpression) {
				DEBinaryExpression binaryOperand = (DEBinaryExpression) operand;
				
				DEExpression operandOperand1 = binaryOperand.getOperand1();
				DEExpression operandOperand2 = binaryOperand.getOperand2();
			
				//De Morgan rules
				if (operand instanceof DEAndExpression) {
					return DEExpressionUtil.or(moveNegationsInwards(DEExpressionUtil.not(operandOperand1)), moveNegationsInwards(DEExpressionUtil.not(operandOperand2)));
				}
				
				if (operand instanceof DEOrExpression) {
					return DEExpressionUtil.and(moveNegationsInwards(DEExpressionUtil.not(operandOperand1)), moveNegationsInwards(DEExpressionUtil.not(operandOperand2)));
				}
				
				throw new InvalidParameterException();
			}
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			binaryExpression.setOperand1(moveNegationsInwards(binaryExpression.getOperand1()));
			binaryExpression.setOperand2(moveNegationsInwards(binaryExpression.getOperand2()));
			
			return binaryExpression;
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			unaryExpression.setOperand(moveNegationsInwards(unaryExpression.getOperand()));
			
			return unaryExpression;
		}
		
		//All atoms can stay as they are.
		return expression;
	}
	
	protected static DEExpression distributeDisjunctions(DEExpression expression) {
		if (expression instanceof DEOrExpression) {
			DEOrExpression orExpression = (DEOrExpression) expression;
		
			DEExpression operand1 = orExpression.getOperand1();
			DEExpression operand2 = orExpression.getOperand2();
			
			//There should be no nesting of conjunctions into disjunctions in CNF.
			//If it appears, distribute disjunction.
			if (operand1 instanceof DEAndExpression) {
				return doDistributeDisjunction(orExpression, true);
			} else if (operand2 instanceof DEAndExpression) {
				return doDistributeDisjunction(orExpression, false);
			}
			
			//If neither operand is an and expression, continue with normal processing.
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			binaryExpression.setOperand1(distributeDisjunctions(binaryExpression.getOperand1()));
			binaryExpression.setOperand2(distributeDisjunctions(binaryExpression.getOperand2()));
			
			return binaryExpression;
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			unaryExpression.setOperand(distributeDisjunctions(unaryExpression.getOperand()));
			
			return unaryExpression;
		}
		
		//All atoms can stay as they are.
		return expression;
	}
	
	protected static DEExpression doDistributeDisjunction(DEOrExpression orExpression, boolean operand1IsThis) {
		DEExpression operand1 = orExpression.getOperand1();
		DEExpression operand2 = orExpression.getOperand2();
		
		DEAndExpression thisOperand = (DEAndExpression) (operand1IsThis ? operand1 : operand2);
		DEExpression thatOperand = operand1IsThis ? operand2 : operand1;
		DEExpression copiedThatOperand = EcoreUtil.copy(thatOperand);
		
		DEExpression thisOperandOperand1 = thisOperand.getOperand1();
		DEExpression thisOperandOperand2 = thisOperand.getOperand2();
		
		
		//Keep original order
		DEExpression or1Operand1 = operand1IsThis ? thisOperandOperand1 : thatOperand;
		DEExpression or1Operand2 = operand1IsThis ? thatOperand : thisOperandOperand1;
		
		DEExpression or2Operand1 = operand1IsThis ? thisOperandOperand2 : copiedThatOperand;
		DEExpression or2Operand2 = operand1IsThis ? copiedThatOperand : thisOperandOperand2;
		
		
		DEExpression resultOperand1 = distributeDisjunctions(DEExpressionUtil.or(or1Operand1, or1Operand2));
		DEExpression resultOperand2 = distributeDisjunctions(DEExpressionUtil.or(or2Operand1, or2Operand2));
		
		return DEExpressionUtil.and(resultOperand1, resultOperand2);
	}
}
