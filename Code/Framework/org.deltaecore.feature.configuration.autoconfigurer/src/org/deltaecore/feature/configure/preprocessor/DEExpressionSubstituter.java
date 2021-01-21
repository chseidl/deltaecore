package org.deltaecore.feature.configure.preprocessor;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.constraint.util.DEConstraintUtil;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEBooleanValueExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.deltaecore.feature.expression.util.DEExpressionUtil;

public class DEExpressionSubstituter {
	public static DEExpression substitutePresenceValues(DEExpression expression, DEConfiguration configuration, boolean isPartialConfiguration) {
		if (expression instanceof DEAtomicExpression) {
			if (expression instanceof DEAbstractFeatureReferenceExpression) {
				DEAbstractFeatureReferenceExpression abstractFeatureReferenceExpression = (DEAbstractFeatureReferenceExpression) expression;
				DEFeature feature = abstractFeatureReferenceExpression.getFeature();
				DEVersionRestriction versionRestriction = abstractFeatureReferenceExpression.getVersionRestriction();
				
				if (versionRestriction == null) {
					//Only feature needs to be present in configuration
					DEBooleanValueExpression booleanValueExpression = substituteFeaturePresence(feature, configuration, isPartialConfiguration);
					
					if (booleanValueExpression != null) {
						return booleanValueExpression;
					}
				} else {
					boolean featureIsPresent = DEConfigurationUtil.configurationContains(configuration, feature);
					
					if (!featureIsPresent) {
						if (expression instanceof DEConditionalFeatureReferenceExpression) {
							//If it is a conditional feature reference, the feature has to be present
							//in the configuration for the constraint to be evaluated. Hence, if the feature is
							//not present, the constraint evaluates to true...
							return DEExpressionUtil.booleanLiteral(true);
						} else {
							//... in all other cases, the version restriction cannot be satisfied if not even the
							//feature is part of the configuration.
							return DEExpressionUtil.booleanLiteral(false);
						}
					}
					
					//Version restriction has to be satisfied
					DEVersion version = DEConfigurationUtil.getSelectedVersionForFeature(configuration, feature);
					
					if (version == null && !isPartialConfiguration) {
						return DEExpressionUtil.booleanLiteral(false);
					}
					
					if (version != null) {
						boolean versionSatisfies = DEConstraintUtil.versionSatisfiesVersionRestriction(version, versionRestriction);
						
						return DEExpressionUtil.booleanLiteral(versionSatisfies);
					}
				}
			}
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			DEExpression operand = unaryExpression.getOperand();
			DEExpression newOperand = substitutePresenceValues(operand, configuration, isPartialConfiguration);
			unaryExpression.setOperand(newOperand);
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			DEExpression operand1 = binaryExpression.getOperand1();
			DEExpression newOperand1 = substitutePresenceValues(operand1, configuration, isPartialConfiguration);
			binaryExpression.setOperand1(newOperand1);
			
			DEExpression operand2 = binaryExpression.getOperand2();
			DEExpression newOperand2 = substitutePresenceValues(operand2, configuration, isPartialConfiguration);
			binaryExpression.setOperand2(newOperand2);
		}
		
		return expression;
	}

	private static DEBooleanValueExpression substituteFeaturePresence(DEFeature feature, DEConfiguration configuration, boolean isPartialConfiguration) {
		boolean featureIsPresent = DEConfigurationUtil.configurationContains(configuration, feature);
		
		if (featureIsPresent) {
			return DEExpressionUtil.booleanLiteral(true);
		}
		
		//Partial configuration means versions, not features.
		//if (!featureIsPresent && !isPartialConfiguration) {
		if (!featureIsPresent) {
			return DEExpressionUtil.booleanLiteral(false);
		}
		
		return null;
	}
}
