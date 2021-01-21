package org.deltaecore.core.variant.util;

import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.generation.DEIdentifierNameBuilder;

public class DEVariableNameBuilder extends DEIdentifierNameBuilder {
	public void createUniqueVariableNameIfNecessary(DEVariableDeclaration variableDeclaration) {
		String variableName = variableDeclaration.getName();
		
		initializeVariableNameIfNecessary(variableDeclaration);
		
		variableName = makeIdentifierNameValidAndUniqueIfNecessary(variableName);
		
		variableDeclaration.setName(variableName);
		registerIdentifierName(variableName);
	}
	
	private void initializeVariableNameIfNecessary(DEVariableDeclaration variableDeclaration) {
		String variableName = variableDeclaration.getName();
		
		if (variableName == null || variableName.isEmpty()) {
			//Create initial variable name
			DEExpression2 expression = variableDeclaration.getExpression();
			variableName = createInitialVariableName(expression);
			
			variableDeclaration.setName(variableName);
		}
	}
	
	//TODO: Enhance this, once the rest works.
	private String createInitialVariableName(DEExpression2 expression) {
		
//		if (expression instanceof DEValue) {
//			DEValue value = (DEValue) expression;
//		}
		
		//ModelReference
		//Literal
		//VariableReference (nonsense but may happen)
		//VirtualConstructorCall
		
		return "variable";
	}
}
