package org.deltaecore.core.decore.resource.decore.postprocessor;

import java.util.Iterator;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDoubleLiteral;
import org.deltaecore.core.decore.DEExpressionContainer;
import org.deltaecore.core.decore.DEIntegerLiteral;
import org.deltaecore.core.decore.DEcoreFactory;
import org.eclipse.emf.ecore.EObject;

public class DEDecoreDoubleTokenFixer implements IDEDecorePostProcessor {
	//Replaces erroneously detected integer tokens with doubles where necessary.
	public void process(DEDelta delta) {
		Iterator<EObject> iterator = delta.eAllContents();
		
		while (iterator.hasNext()) {
			EObject eObject = iterator.next();
			
			//In cases where a Double is expected but an Integer is provided,
			//the provided Integer is changed to a Double with the same value.
			if (eObject instanceof DEIntegerLiteral) {
				DEIntegerLiteral integerLiteral = (DEIntegerLiteral) eObject;
				Integer integerValue = integerLiteral.getValue();
				
				DEExpressionContainer expressionContainer = integerLiteral.getExpressionContainer();
				Class<?> expectedJavaClass = expressionContainer.getExpectedJavaClass();
				
				if (expectedJavaClass != null && expectedJavaClass.equals(Double.class)) {
					DEDoubleLiteral doubleLiteral = DEcoreFactory.eINSTANCE.createDEDoubleLiteral();
					double doubleValue = integerValue;
					doubleLiteral.setValue(doubleValue);
					
					expressionContainer.setExpression(doubleLiteral);
				}
			}
		}
	}
}
