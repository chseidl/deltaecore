package org.deltaecore.feature.configure.preprocessor;

import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENestedExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.util.DEExpressionUtil;

public class DEExpressionContractor {
	public static DEExpression contractExpression(DEExpression expression) {
		if (expression instanceof DEAtomicExpression) {
			//Nothing to do here.
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			DEExpression operand = contractExpression(unaryExpression.getOperand());
			unaryExpression.setOperand(operand);
			
			Boolean operandValue = DEExpressionUtil.getBooleanValue(operand);
			
			if (operandValue != null) {
				if (expression instanceof DENotExpression) {
					return DEExpressionUtil.booleanLiteral(!operandValue);
				}
				
				if (expression instanceof DENestedExpression) {
					return DEExpressionUtil.booleanLiteral(operandValue);
				}
			}
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			DEExpression operand1 = contractExpression(binaryExpression.getOperand1());
			DEExpression operand2 = contractExpression(binaryExpression.getOperand2());
			
			binaryExpression.setOperand1(operand1);
			binaryExpression.setOperand2(operand2);
			
			if (expression instanceof DEAndExpression) {
				DEAndExpression andExpression = (DEAndExpression) binaryExpression;
				return contractAndExpression(andExpression);
			} else if (expression instanceof DEOrExpression) {
				DEOrExpression orExpression = (DEOrExpression) binaryExpression;
				return contractOrExpression(orExpression);
			} else if (expression instanceof DEImpliesExpression) {
				DEImpliesExpression impliesExpression = (DEImpliesExpression) binaryExpression;
				return contractImpliesExpression(impliesExpression);
			} else if (expression instanceof DEEquivalenceExpression) {
				DEEquivalenceExpression equivalenceExpression = (DEEquivalenceExpression) binaryExpression;
				return contractEquivalenceExpression(equivalenceExpression);
			}
		}
		
		return expression;
	}
	
	protected static DEExpression contractAndExpression(DEAndExpression expression) {
		DEExpression operand1 = expression.getOperand1();
		Boolean operand1Value = DEExpressionUtil.getBooleanValue(operand1);
		
		DEExpression operand2 = expression.getOperand2();
		Boolean operand2Value = DEExpressionUtil.getBooleanValue(operand2);
		
		if (operand1Value == null) {
			if (operand2Value == null) {
				//Nothing to do here
				return expression;
			} else if (operand2Value) {
				return operand1;
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			}
		} else if (operand1Value) {
			if (operand2Value == null) {
				return operand2;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
				
			}
		} else if (!operand1Value) {
			if (operand2Value == null) {
				return DEExpressionUtil.booleanLiteral(false);
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			}
		}
		
		return expression;
	}

	protected static DEExpression contractOrExpression(DEOrExpression expression) {
		DEExpression operand1 = expression.getOperand1();
		Boolean operand1Value = DEExpressionUtil.getBooleanValue(operand1);
		
		DEExpression operand2 = expression.getOperand2();
		Boolean operand2Value = DEExpressionUtil.getBooleanValue(operand2);
		
		if (operand1Value == null) {
			if (operand2Value == null) {
				return expression;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return operand1;
			}
		} else if (operand1Value) {
			if (operand2Value == null) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			}
		} else if (!operand1Value) {
			if (operand2Value == null) {
				return operand2;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			}
		}
		
		return expression;
	}
	
	protected static DEExpression contractImpliesExpression(DEImpliesExpression expression) {
		DEExpression operand1 = expression.getOperand1();
		Boolean operand1Value = DEExpressionUtil.getBooleanValue(operand1);
		
		DEExpression operand2 = expression.getOperand2();
		Boolean operand2Value = DEExpressionUtil.getBooleanValue(operand2);
		
		if (operand1Value == null) {
			if (operand2Value == null) {
				return expression;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.not(operand1);
			}
		} else if (operand1Value) {
			if (operand2Value == null) {
				return operand2;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			}
		} else if (!operand1Value) {
			if (operand2Value == null) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			}
		}
		
		return expression;
	}
	
	protected static DEExpression contractEquivalenceExpression(DEEquivalenceExpression expression) {
		DEExpression operand1 = expression.getOperand1();
		Boolean operand1Value = DEExpressionUtil.getBooleanValue(operand1);
		
		DEExpression operand2 = expression.getOperand2();
		Boolean operand2Value = DEExpressionUtil.getBooleanValue(operand2);
		
		if (operand1Value == null) {
			if (operand2Value == null) {
				return expression;
			} else if (operand2Value) {
				return operand1;
			} else if (!operand2Value) {
				return DEExpressionUtil.not(operand1);
			}
		} else if (operand1Value) {
			if (operand2Value == null) {
				return operand2;
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			}
		} else if (!operand1Value) {
			if (operand2Value == null) {
				return DEExpressionUtil.not(operand2);
			} else if (operand2Value) {
				return DEExpressionUtil.booleanLiteral(false);
			} else if (!operand2Value) {
				return DEExpressionUtil.booleanLiteral(true);
			}
		}
		
		return expression;
	}
}
