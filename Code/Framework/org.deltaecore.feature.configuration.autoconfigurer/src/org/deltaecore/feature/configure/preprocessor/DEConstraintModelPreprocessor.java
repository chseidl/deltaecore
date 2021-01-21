package org.deltaecore.feature.configure.preprocessor;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.deltaecore.feature.expression.util.DEExpressionUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEConstraintModelPreprocessor {
	public static DEConstraintModel preprocessConstraintModel(DEConstraintModel constraintModel, DEConfiguration configuration) {
		if (constraintModel == null) {
			return null;
		}
		
		DEConstraintModel copiedConstraintModel = EcoreUtil.copy(constraintModel);
		
		List<DEConstraint> constraints = copiedConstraintModel.getConstraints();
		List<DEConstraint> newConstraints = new ArrayList<DEConstraint>();
		
		for (DEConstraint constraint : constraints) {
			DEExpression rootExpression = constraint.getRootExpression();
			
			//Simplify expression in constraint
			DEExpression newRootExpression = DEExpressionSimplifier.simplifyExpression(rootExpression);
			
			//Substitute boolean values for presence conditions
			newRootExpression = DEExpressionSubstituter.substitutePresenceValues(newRootExpression, configuration, true);
			
			//Contract expression in constraint
			newRootExpression = DEExpressionContractor.contractExpression(newRootExpression);
			constraint.setRootExpression(newRootExpression);
			
			boolean dependsOnVersion = dependsOnVersion(newRootExpression);
			
			//As we are trying to find suitable versions and assume a valid partial
			//configuration with regard to features as input, only those constraints
			//are of relevance that depend on versions.
			if (dependsOnVersion) {
				Boolean constraintValue = DEExpressionUtil.getBooleanValue(newRootExpression);
				
				//Either constraint is not yet evaluated or evaluates to false.
				//Constraints evaluating to true can be skipped as they do not have ot be evaluated anymore.
				if (constraintValue == null || !constraintValue) {
					newConstraints.add(constraint);
				}
			}
		}
		
		constraints.clear();
		constraints.addAll(newConstraints);
		
		return copiedConstraintModel;
	}
	
	protected static boolean dependsOnVersion(DEExpression expression) {
		if (expression instanceof DEAtomicExpression) {
			if (expression instanceof DEAbstractFeatureReferenceExpression) {
				DEAbstractFeatureReferenceExpression abstractFeatureReferenceExpression = (DEAbstractFeatureReferenceExpression) expression;
				DEVersionRestriction versionRestriction = abstractFeatureReferenceExpression.getVersionRestriction();
						
				return (versionRestriction != null);
			}
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			DEExpression operand = unaryExpression.getOperand();
			return dependsOnVersion(operand);
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			DEExpression operand1 = binaryExpression.getOperand1();
			DEExpression operand2 = binaryExpression.getOperand2();
			
			return dependsOnVersion(operand1) || dependsOnVersion(operand2);
		}
		
		return true;
	}
}
