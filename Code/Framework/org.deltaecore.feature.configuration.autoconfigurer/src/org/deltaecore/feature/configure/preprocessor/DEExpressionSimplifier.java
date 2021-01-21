package org.deltaecore.feature.configure.preprocessor;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEAtomicExpression;
import org.deltaecore.feature.expression.DEBinaryExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEEquivalenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEExpressionFactory;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEImpliesExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.DEUnaryExpression;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DEExpressionSimplifier {
	public static DEExpression simplifyExpression(DEExpression expression) {
		if (expression instanceof DEAtomicExpression) {
			//Replace conditional feature reference expressions
			if (expression instanceof DEConditionalFeatureReferenceExpression) {
				DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression = (DEConditionalFeatureReferenceExpression) expression;
				return replaceConditionalFeatureReferenceExpression(conditionalFeatureReferenceExpression);
			}
		}
		
		if (expression instanceof DEUnaryExpression) {
			DEUnaryExpression unaryExpression = (DEUnaryExpression) expression;
			
			DEExpression operand = unaryExpression.getOperand();
			DEExpression newOperand = simplifyExpression(operand);
			unaryExpression.setOperand(newOperand);
		}
		
		if (expression instanceof DEBinaryExpression) {
			DEBinaryExpression binaryExpression = (DEBinaryExpression) expression;
			
			DEExpression operand1 = binaryExpression.getOperand1();
			DEExpression newOperand1 = simplifyExpression(operand1);
			binaryExpression.setOperand1(newOperand1);
			
			DEExpression operand2 = binaryExpression.getOperand2();
			DEExpression newOperand2 = simplifyExpression(operand2);
			binaryExpression.setOperand2(newOperand2);
			
			//Replace equivalence
			if (expression instanceof DEEquivalenceExpression) {
				DEEquivalenceExpression equivalenceExpression = (DEEquivalenceExpression) expression;
				return replaceEquivalenceExpression(equivalenceExpression);
			}
			
			//Replace implication
			if (expression instanceof DEImpliesExpression) {
				DEImpliesExpression impliesExpression = (DEImpliesExpression) expression;
				return replaceImpliesExpression(impliesExpression);
			}
		}
		
		return expression;
	}
	
	protected static DEExpression replaceConditionalFeatureReferenceExpression(DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression) {
		//?F [op V] = (F -> F [op V]) = (!F || F [op V])
		DEFeature feature = conditionalFeatureReferenceExpression.getFeature();
		DEVersionRestriction versionRestriction = conditionalFeatureReferenceExpression.getVersionRestriction();
		
		DEFeatureReferenceExpression featureReferenceExpression1 = DEExpressionFactory.eINSTANCE.createDEFeatureReferenceExpression();
		featureReferenceExpression1.setFeature(feature);
		
		DEFeatureReferenceExpression featureReferenceExpression2 = DEExpressionFactory.eINSTANCE.createDEFeatureReferenceExpression();
		featureReferenceExpression2.setFeature(feature);
		featureReferenceExpression2.setVersionRestriction(versionRestriction);
		
		DENotExpression notExpression = DEExpressionFactory.eINSTANCE.createDENotExpression();
		notExpression.setOperand(featureReferenceExpression1);
		
		DEOrExpression orExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
		orExpression.setOperand1(notExpression);
		orExpression.setOperand2(featureReferenceExpression2);
		
		return orExpression;
	}
	
	protected static DEExpression replaceImpliesExpression(DEImpliesExpression impliesExpression) {
		//a -> b = !a || b
		DEExpression operand1 = impliesExpression.getOperand1();
		DEExpression operand2 = impliesExpression.getOperand2();
		
		DENotExpression notExpression = DEExpressionFactory.eINSTANCE.createDENotExpression();
		notExpression.setOperand(operand1);
		
		DEOrExpression orExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
		orExpression.setOperand1(notExpression);
		orExpression.setOperand2(operand2);
		
		return orExpression;
	}
	
	protected static DEExpression replaceEquivalenceExpression(DEEquivalenceExpression equivalenceExpression) {
		//a == b = (a && b) || (!a && !b)
		DEExpression operand1 = equivalenceExpression.getOperand1();
		DEExpression operand2 = equivalenceExpression.getOperand2();
		
		DEExpression copiedOperand1 = EcoreUtil.copy(operand1);
		DEExpression copiedOperand2 = EcoreUtil.copy(operand2);
		
		
		DEAndExpression andExpression1 = DEExpressionFactory.eINSTANCE.createDEAndExpression();
		andExpression1.setOperand1(operand1);
		andExpression1.setOperand2(operand2);
		
		
		DENotExpression notExpression1 = DEExpressionFactory.eINSTANCE.createDENotExpression();
		notExpression1.setOperand(copiedOperand1);
		
		DENotExpression notExpression2 = DEExpressionFactory.eINSTANCE.createDENotExpression();
		notExpression2.setOperand(copiedOperand2);
		
		
		DEAndExpression andExpression2 = DEExpressionFactory.eINSTANCE.createDEAndExpression();
		andExpression2.setOperand1(notExpression1);
		andExpression2.setOperand2(notExpression2);
		
		
		DEOrExpression orExpression = DEExpressionFactory.eINSTANCE.createDEOrExpression();
		orExpression.setOperand1(andExpression1);
		orExpression.setOperand2(andExpression2);
		
		return orExpression;
	}
}
