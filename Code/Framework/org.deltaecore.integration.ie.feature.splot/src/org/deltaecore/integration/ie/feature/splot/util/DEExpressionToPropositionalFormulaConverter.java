package org.deltaecore.integration.ie.feature.splot.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.expression.DEAndExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DEFeatureReferenceExpression;
import org.deltaecore.feature.expression.DENotExpression;
import org.deltaecore.feature.expression.DEOrExpression;
import org.deltaecore.feature.expression.util.DEExpressionToCNFConverter;

import constraints.BooleanVariable;
import constraints.BooleanVariableInterface;
import constraints.CNFClause;
import constraints.CNFLiteral;
import constraints.PropositionalFormula;

public class DEExpressionToPropositionalFormulaConverter {
	private Map<DEFeature, String> featureToIdMapping;
	private int constraintCount;
	private Map<DEFeature, BooleanVariable> featureToVariableMapping = new HashMap<DEFeature, BooleanVariable>();
	
	public DEExpressionToPropositionalFormulaConverter() {
		reset();
	}
	
	public void reset() {
		constraintCount = 0;
		featureToVariableMapping.clear();
	}
	
	public PropositionalFormula convertToPropositionalFormula(DEExpression expression, String baseFormulaName, Map<DEFeature, String> featureToIdMapping) {
		this.featureToIdMapping = featureToIdMapping;
		DEExpression expressionInCNF = DEExpressionToCNFConverter.convertToCNF(expression);
		
		//NOTE: Would be great to build a propositional formula from the clauses.
		//However, the creators of PropositionalFormula do no allow this so that
		//the clauses are printed to a string which is then parsed back in again instead.
		List<CNFClause> clauses = flatten(expressionInCNF);
		
		String printedFormula = "";
		boolean isFirstClause = true;
		
		for (CNFClause clause : clauses) {
			//TODO; What is the separator here?
			if (!isFirstClause) {
				printedFormula += " and ";
			}
			
			List<CNFLiteral> literals = clause.getLiterals();
			boolean isFirstLiteral = true;
			
			for (CNFLiteral literal : literals) {
				if (!isFirstLiteral) {
					printedFormula += " or ";
				}
				
				BooleanVariableInterface variable = literal.getVariable();
				
				if (!literal.isPositive()) {
					printedFormula += "~";
				}
				printedFormula += variable.getID();
				
				isFirstLiteral = false;
			}
			
			isFirstClause = false;
		}
		
		try {
			constraintCount++;
			String name = baseFormulaName + constraintCount;
			
			return new PropositionalFormula(name, printedFormula);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	protected List<CNFClause> flatten(DEExpression expression) {
		pendingLiterals.clear();
		pendingClauses.clear();
		
		flattenA(expression);
		flushClause();

		return new ArrayList<CNFClause>(pendingClauses);
	}
	
	protected void flattenA(DEExpression expression) {
		if (expression instanceof DEAndExpression) {
			DEAndExpression andExpression = (DEAndExpression) expression;
			
			DEExpression operand1 = andExpression.getOperand1();
			DEExpression operand2 = andExpression.getOperand2();
			
			flattenA(operand1);
			
			//If right operand is not an and, then flush clause.
			if (!(operand2 instanceof DEAndExpression)) {
				flushClause();
			}
			
			flattenA(operand2);
			return;
		}
		
		flattenB(expression);
	}
	
	protected void flattenB(DEExpression expression) {
		if (expression instanceof DEOrExpression) {
			DEOrExpression orExpression = (DEOrExpression) expression;
			
			DEExpression operand1 = orExpression.getOperand1();
			DEExpression operand2 = orExpression.getOperand2();
			
			flattenB(operand1);
			flattenB(operand2);
			
			return;
		}
		
		if (expression instanceof DENotExpression) {
			DENotExpression notExpression = (DENotExpression) expression;
			flushLiteral(notExpression);
			return;
		}
		
		if (expression instanceof DEFeatureReferenceExpression) {
			DEFeatureReferenceExpression featureReferenceExpression = (DEFeatureReferenceExpression) expression;
			flushLiteral(featureReferenceExpression);
			return;
		}
		
		if (expression instanceof DEConditionalFeatureReferenceExpression) {
			DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression = (DEConditionalFeatureReferenceExpression) expression;
			flushLiteral(conditionalFeatureReferenceExpression);
			return;
		}
		
		throw new InvalidParameterException(); 
	}
	
	protected void flushLiteral(DENotExpression notExpression) {
		DEExpression operand = notExpression.getOperand();
		
		if (operand instanceof DEFeatureReferenceExpression) {
			DEFeatureReferenceExpression featureReference = (DEFeatureReferenceExpression) operand;
			DEFeature feature = featureReference.getFeature();
			doFlushLiteral(feature, false);
			return;
		}

		if (operand instanceof DEConditionalFeatureReferenceExpression) {
			DEConditionalFeatureReferenceExpression conditionalFeatureReference = (DEConditionalFeatureReferenceExpression) operand;
			DEFeature feature = conditionalFeatureReference.getFeature();
			doFlushLiteral(feature, false);
			return;
		}
		
		throw new InvalidParameterException();
	}
	
	protected void flushLiteral(DEFeatureReferenceExpression featureReference) {
		DEFeature feature = featureReference.getFeature();
		doFlushLiteral(feature, true);
	}

	protected void flushLiteral(DEConditionalFeatureReferenceExpression conditionalFeatureReference) {
		DEFeature feature = conditionalFeatureReference.getFeature();
		doFlushLiteral(feature, true);
	}
	
	protected void doFlushLiteral(DEFeature feature, boolean isPositive) {
		BooleanVariable variable = getBooleanVariableForFeature(feature);
		CNFLiteral literal = new CNFLiteral(variable, isPositive);
		pendingLiterals.add(literal);
	}
	
	
	private List<CNFClause> pendingClauses = new LinkedList<CNFClause>();
	private List<CNFLiteral> pendingLiterals = new LinkedList<CNFLiteral>();
	
	protected void flushClause() {
		CNFClause clause = new CNFClause();
	
		for (CNFLiteral literal : pendingLiterals) {
			clause.addLiteral(literal);
		}
		
		pendingLiterals.clear();
		pendingClauses.add(clause);
	}
	
	protected BooleanVariable getBooleanVariableForFeature(DEFeature feature) {
		if (!featureToVariableMapping.containsKey(feature)) {
			String id = featureToIdMapping.get(feature);
			BooleanVariable variable = new BooleanVariable(id);
			featureToVariableMapping.put(feature, variable);
		}
		
		return featureToVariableMapping.get(feature);
	}
}
