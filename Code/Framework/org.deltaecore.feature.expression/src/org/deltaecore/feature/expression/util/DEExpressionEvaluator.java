package org.deltaecore.feature.expression.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEBooleanValueExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENestedExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DERelativeVersionRestriction;
import org.deltaecore.feature.expression.DERelativeVersionRestrictionOperator;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRangeRestriction;
import org.deltaecore.feature.expression.DEVersionRestriction;

public abstract class DEExpressionEvaluator {
	protected boolean evaluateExpression(DEExpression expression) {
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			return evaluateBinaryExpression(binaryExpression);
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			return evaluateUnaryExpression(unaryExpression);
		}
		
		if (expression instanceof DEAtomicExpression) {
			DEAtomicExpression atomicExpression = (DEAtomicExpression) expression;
			return evaluateAtomicExpression(atomicExpression);
		}
		
		return true;
	}

	protected boolean evaluateBinaryExpression(DEBinaryExpression binaryExpression) {
		DEExpression operand1 = binaryExpression.getOperand1();
		DEExpression operand2 = binaryExpression.getOperand2();
		
		boolean operand1Result = evaluateExpression(operand1);
		boolean operand2Result = evaluateExpression(operand2);
		
		if (binaryExpression instanceof DEAndExpression) {
			return operand1Result && operand2Result;
		}
		
		if (binaryExpression instanceof DEOrExpression) {
			return operand1Result || operand2Result;
		}
		
		if (binaryExpression instanceof DEImpliesExpression) {
			//A -> B = !A || B
			return !operand1Result || operand2Result;
		}
		
		if (binaryExpression instanceof DEEquivalenceExpression) {
			//A <-> B = (A && B) || (!A && !B)
			return (operand1Result && operand2Result) || (!operand1Result && !operand2Result);
		}
		
		throw new UnsupportedOperationException(binaryExpression.getClass().getCanonicalName());
	}
	
	protected boolean evaluateUnaryExpression(DEUnaryExpression unaryExpression) {
		DEExpression operand = unaryExpression.getOperand();
		boolean operandResult = evaluateExpression(operand);
		
		if (unaryExpression instanceof DENotExpression) {
			return !operandResult;
		}
		
		if (unaryExpression instanceof DENestedExpression) {
			//Just forward result.
			return operandResult;
		}
		
		throw new UnsupportedOperationException(unaryExpression.getClass().getCanonicalName());
	}
	
	protected boolean evaluateAtomicExpression(DEAtomicExpression atomicExpression) {
		if (atomicExpression instanceof DEBooleanValueExpression) {
			DEBooleanValueExpression booleanValueExpression = (DEBooleanValueExpression) atomicExpression;
			
			return booleanValueExpression.isValue();
		}
		
		if (atomicExpression instanceof DEFeatureReferenceExpression) {
			DEFeatureReferenceExpression featureReferenceExpression = (DEFeatureReferenceExpression) atomicExpression;

			DEFeature feature = featureReferenceExpression.getFeature();
			
			if (!isFeaturePresent(feature)) {
				return false;
			}
			
			DEVersionRestriction versionRestriction = featureReferenceExpression.getVersionRestriction();
			return evaluateVersionRestriction(versionRestriction);
		}
		
		if (atomicExpression instanceof DEConditionalFeatureReferenceExpression) {
			DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression = (DEConditionalFeatureReferenceExpression) atomicExpression;
			
			DEFeature feature = conditionalFeatureReferenceExpression.getFeature();
			
			if (!isFeaturePresent(feature)) {
				return true;
			}
			
			DEVersionRestriction versionRestriction = conditionalFeatureReferenceExpression.getVersionRestriction();
			return evaluateVersionRestriction(versionRestriction);
		}
		
		throw new UnsupportedOperationException(atomicExpression.getClass().getCanonicalName());
	}
	
	protected boolean evaluateVersionRestriction(DEVersionRestriction versionRestriction) {
		if (versionRestriction == null) {
			//No restriction, no need to satisfy anything.
			return true;
		}
		
		if (versionRestriction instanceof DEVersionRangeRestriction) {
			DEVersionRangeRestriction versionRangeRestriction = (DEVersionRangeRestriction) versionRestriction;
			
			DEVersion lowerVersion = versionRangeRestriction.getLowerVersion();
			DEVersion upperVersion = versionRangeRestriction.getUpperVersion();

			DEVersion currentVersion = upperVersion;

			while(currentVersion != null) {
				if (isVersionPresent(currentVersion)) {
					return true;
				}
				
				if (currentVersion == lowerVersion) {
					break;
				}
				
				currentVersion = currentVersion.getSupersededVersion();
			}
			
			return false;
		}
		
		if (versionRestriction instanceof DERelativeVersionRestriction) {
			DERelativeVersionRestriction relativeVersionRestriction = (DERelativeVersionRestriction) versionRestriction;
			
			//Naive implementation that _will_ cause problems with scalability. Take care!
			List<DEVersion> satisfyingVersions = getSatisfyingVersions(relativeVersionRestriction);
			
			for (DEVersion satisfyingVersion : satisfyingVersions) {
				if (isVersionPresent(satisfyingVersion)) {
					return true;
				}
			}
			
			return false;
		}
		
		throw new UnsupportedOperationException(versionRestriction.getClass().getCanonicalName());
	}
	
	protected List<DEVersion> getSatisfyingVersions(DERelativeVersionRestriction relativeVersionRestriction) {
		DERelativeVersionRestrictionOperator operator = relativeVersionRestriction.getOperator();
		DEVersion version = relativeVersionRestriction.getVersion();
		
		return doGetSatisfyingVersions(operator, version);
	}
	
	protected List<DEVersion> doGetSatisfyingVersions(DERelativeVersionRestrictionOperator operator, DEVersion version) {
		if (operator == DERelativeVersionRestrictionOperator.LESS_THAN) {
			List<DEVersion> satisfyingVersions = new ArrayList<DEVersion>();
			DEVersion currentVersion = version;
			
			while(currentVersion != null) {
				satisfyingVersions.add(currentVersion);
				
				currentVersion = currentVersion.getSupersededVersion();
			}
			
			return satisfyingVersions;
		}
		
		if (operator == DERelativeVersionRestrictionOperator.LESS_THAN_OR_EQUAL) {
			List<DEVersion> satisfyingVersions = doGetSatisfyingVersions(DERelativeVersionRestrictionOperator.LESS_THAN, version);
			satisfyingVersions.add(version);
			return satisfyingVersions;
		}
		
		if (operator == DERelativeVersionRestrictionOperator.EQUAL || operator == DERelativeVersionRestrictionOperator.IMPLICIT_EQUAL) {
			return Collections.singletonList(version);
		}
		
		if (operator == DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL) {
			List<DEVersion> satisfyingVersions = doGetSatisfyingVersions(DERelativeVersionRestrictionOperator.GREATER_THAN, version);
			satisfyingVersions.add(version);
			return satisfyingVersions;
		}
		
		if (operator == DERelativeVersionRestrictionOperator.GREATER_THAN) {
			List<DEVersion> satisfyingVersions = new ArrayList<DEVersion>();
			
			List<DEVersion> supersedingVersions = version.getSupersedingVersions();
			satisfyingVersions.addAll(supersedingVersions);
			
			for (DEVersion supersedingVersion : supersedingVersions) {
				List<DEVersion> furtherSupersedingVersions = doGetSatisfyingVersions(DERelativeVersionRestrictionOperator.GREATER_THAN, supersedingVersion);
				
				satisfyingVersions.addAll(furtherSupersedingVersions);
			}
			
			return satisfyingVersions;
		}
		
		throw new UnsupportedOperationException(operator.getClass().getCanonicalName());
	}
	
	protected abstract boolean isFeaturePresent(DEFeature feature);
	protected abstract boolean isVersionPresent(DEVersion version);
}
