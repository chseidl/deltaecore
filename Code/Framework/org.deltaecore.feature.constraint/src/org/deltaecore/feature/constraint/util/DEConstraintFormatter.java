package org.deltaecore.feature.constraint.util;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEBooleanValueExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENestedExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DERelativeVersionRestriction;
import org.deltaecore.feature.expression.DERelativeVersionRestrictionOperator;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRangeRestriction;
import org.deltaecore.feature.expression.DEVersionRestriction;

//NOTE: printing might not adequately reflect operator precedence, introduce verbose parentheses if necessary
public class DEConstraintFormatter {
	private static final String endl = "\n";
	
	public static String formatConstraintModel(DEConstraintModel constraintModel) {
		if (constraintModel == null) {
			return "";
		}
		
		String output = "";
		
		List<DEConstraint> constraints = constraintModel.getConstraints();
		
		for (DEConstraint constraint : constraints) {
			output += formatConstraint(constraint) + endl;
		}
		
		return output;
	}

	public static String formatConstraint(DEConstraint constraint) {
		String output = "";
		
		DEExpression rootExpression = constraint.getRootExpression();
		output += formatExpression(rootExpression);
		
		return output;
	}
	
	//TODO: This could be lifted as it applies to expressions in general.
	public static String formatExpression(DEExpression expression) {
		if (expression instanceof DEAtomicExpression) {
			if (expression instanceof DEBooleanValueExpression) {
				DEBooleanValueExpression booleanValueExpression = (DEBooleanValueExpression) expression;
				boolean value = booleanValueExpression.isValue();
				
				return value ? "true" : "false";
			}
			
			if (expression instanceof DEAbstractFeatureReferenceExpression) {
				DEAbstractFeatureReferenceExpression abstractFeatureReferenceExpression = (DEAbstractFeatureReferenceExpression) expression;
				
				DEFeature feature = abstractFeatureReferenceExpression.getFeature();
				DEVersionRestriction versionRestriction = abstractFeatureReferenceExpression.getVersionRestriction();
				
				String output = "";
				
				if (expression instanceof DEConditionalFeatureReferenceExpression) {
					output += "?";
				}
				
				output += feature.getName();
				
				if (versionRestriction != null) {
					if (versionRestriction instanceof DEVersionRangeRestriction) {
						DEVersionRangeRestriction versionRangeRestriction = (DEVersionRangeRestriction) versionRestriction;
						
						DEVersion lowerVersion = versionRangeRestriction.getLowerVersion();
						DEVersion upperVersion = versionRangeRestriction.getUpperVersion();
						
						boolean isLowerIncluded = versionRangeRestriction.isLowerIncluded();
						boolean isUpperIncluded = versionRangeRestriction.isLowerIncluded();
						
						output += " [";
						
						if (!isLowerIncluded) {
							output += "^";
						}
						
						output += lowerVersion.getNumber();
						output += " - ";
						
						if (!isUpperIncluded) {
							output += "^";
						}
						
						output += upperVersion.getNumber();
						output += "]";
					} else if (versionRestriction instanceof DERelativeVersionRestriction) {
						DERelativeVersionRestriction relativeVersionRestriction = (DERelativeVersionRestriction) versionRestriction;
						DERelativeVersionRestrictionOperator operator = relativeVersionRestriction.getOperator();
						DEVersion version = relativeVersionRestriction.getVersion();
						
						output += " [";

						switch(operator.getValue()) {
							case DERelativeVersionRestrictionOperator.LESS_THAN_VALUE:
								output += "< ";
								break;
							case DERelativeVersionRestrictionOperator.LESS_THAN_OR_EQUAL_VALUE:
								output += "<= ";
								break;
							case DERelativeVersionRestrictionOperator.EQUAL_VALUE:
								output += "= ";
								break;
							case DERelativeVersionRestrictionOperator.IMPLICIT_EQUAL_VALUE:
								break;
							case DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL_VALUE:
								output += ">= ";
								break;
							case DERelativeVersionRestrictionOperator.GREATER_THAN_VALUE:
								output += "> ";
								break;
							default:
								throw new UnsupportedOperationException("Unable to format relative version restriction operator " + operator);
						}
						
						output += version.getNumber();
						output += "]";
					}
				}
				
				return output;
			}
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			DEExpression operand = unaryExpression.getOperand();
			
			String output = "{";
			
			if (unaryExpression instanceof DENotExpression) {
				output += "!";
			} else if (unaryExpression instanceof DENestedExpression) {
				output += "(";
			}
			
			output += formatExpression(operand);
			
			if (unaryExpression instanceof DENestedExpression) {
				output += ")";
			}
			
			output += "}";
			
			return output;
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			DEExpression operand1 = binaryExpression.getOperand1();
			DEExpression operand2 = binaryExpression.getOperand2();
			
			String output = "{";
			
			output += formatExpression(operand1);
			
			if (expression instanceof DEAndExpression) {
				output += " && ";
			} else if (expression instanceof DEOrExpression) {
				output += " || ";
			} else if (expression instanceof DEImpliesExpression) {
				output += " -> ";
			} else if (expression instanceof DEEquivalenceExpression) {
				output += " <-> ";
			}
			
			output += formatExpression(operand2);
			
			output += "}";
			
			return output;
		}
		
		throw new UnsupportedOperationException("Unable to format expression of type " + expression.getClass() + ".");
	}
}
