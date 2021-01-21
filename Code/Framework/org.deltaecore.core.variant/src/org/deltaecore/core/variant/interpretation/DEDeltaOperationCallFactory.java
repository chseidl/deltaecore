package org.deltaecore.core.variant.interpretation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.core.decore.DEArgument;
import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEStructuralFeatureReference;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.DEVariableReference;
import org.deltaecore.core.decore.DEVirtualConstructorCall;
import org.deltaecore.core.decore.DEcoreFactory;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

//TODO: This is highly experimental
public abstract class DEDeltaOperationCallFactory {
	private DEDeltaDialect deltaDialect;
	private Map<EObject, DEVariableDeclaration> elementToVariableDeclarationMap;
	
	public DEDeltaOperationCallFactory(String namespaceURI) {
		deltaDialect = DEDeltaDialectExtensionRegistry.getDeltaDialect(namespaceURI);
		elementToVariableDeclarationMap = new HashMap<EObject, DEVariableDeclaration>();
	}
	
	protected DEDeltaOperationDefinition findDeltaOperationDefinition(String searchedOperationName) {
		if (searchedOperationName == null) {
			return null;
		}
		
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		
		for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {
			String operationName = deltaOperationDefinition.getName();
			
			if (searchedOperationName.equals(operationName)) {
				return deltaOperationDefinition;
			}
		}
		
		return null;
	}
	
	protected DEArgument createDeltaOperationCallArgument(EObject element) {
		//Make sure that model elements are properly declared and contained within a variable.
		DEVariableDeclaration variableDeclaration = getOrCreateVariableDeclaration(element);
		
		//Replace the argument's original expression with a reference to the variable.
		DEVariableReference variableReference = DEcoreFactory.eINSTANCE.createDEVariableReference();
		variableReference.setVariable(variableDeclaration);
		
		DEArgument argument = DEcoreFactory.eINSTANCE.createDEArgument();
		argument.setExpression(variableReference);
		return argument;
	}
	
	private DEVariableDeclaration getOrCreateVariableDeclaration(EObject element) {
		//Check if variable has already been declared
		if (elementToVariableDeclarationMap.containsValue(element)) {
			return elementToVariableDeclarationMap.get(element);
		}
		
		//Create constants for all model elements
		DEVariableDeclaration variableDeclaration = DEcoreFactory.eINSTANCE.createDEVariableDeclaration();

		//Type
		variableDeclaration.setType(DEDEcoreBaseUtil.createTypeFromEObject(element));
		
		//NOTE: Right now, no name is assigned to variables as it is superfluous for the regular case
		//of simply executing a series of delta operation calls.
		
		//Declaring expression
		DEExpression2 declaringExpression = createDeclaringExpressionForElement(element);
		variableDeclaration.setExpression(declaringExpression);
		
		elementToVariableDeclarationMap.put(element, variableDeclaration);
		
		return variableDeclaration;
	}
	
	private DEExpression2 createDeclaringExpressionForElement(EObject element) {
		boolean isNewElement = (element.eContainer() != null);
		
		if (isNewElement) {
			//"New" elements need to be created using constructors
			return createVirtualConstructorCallForElement(element);
		} else {
			//"Existing" elements need to be referenced.
			//TODO: complete this
//			return createModelReferenceForElement(element);
			return null;
		}
	}
	
	protected DEVirtualConstructorCall createVirtualConstructorCallForElement(EObject element) {
		//TODO: In theory, the created element can be arbitrarily complex as it comes
		//from an unknown source (such as a third party editor). This means that it
		//might need more than just one virtual constructor call to create this element,
		//E.g., it might need a constructor call for a child element, another constructor
		//call for the original element and an add operation to add the child to the parent
		//(where the operation might or might not exist).
		
		//For now, keep this simple and assume that only simple objects are to be created.
		
		DEVirtualConstructorCall virtualConstructorCall = DEcoreFactory.eINSTANCE.createDEVirtualConstructorCall();
		virtualConstructorCall.setType(DEDEcoreBaseUtil.createTypeFromEObject(element));
		
		List<DEStructuralFeatureReference> namedArguments = virtualConstructorCall.getNamedArguments();
		
		EClass elementClass = element.eClass();
		List<EStructuralFeature> structuralFeatures = elementClass.getEAllStructuralFeatures();
		
		for (EStructuralFeature structuralFeature : structuralFeatures) {
			if (element.eIsSet(structuralFeature)) {
				if (structuralFeature instanceof EReference) {
					//If there are references set, we have a problem!
					System.err.println("WARNING: Recording references of newly created elements is not supported. Data loss WILL occur!");
				}
				
				if (structuralFeature instanceof EAttribute) {
					//Add structural feature reference for all set attributes.
					DEStructuralFeatureReference structuralFeatureReference = DEcoreFactory.eINSTANCE.createDEStructuralFeatureReference();
					structuralFeatureReference.setStructuralFeature(structuralFeature);
					namedArguments.add(structuralFeatureReference);
				}
			}
		}

		return null;
	}
	
//	protected DEModelReference createModelReferenceForElement(EObject element) {
//		DEModelReference modelReference = DEcoreFactory.eINSTANCE.createDEModelReference();
//		modelReference.setValue(element);
//		return modelReference;
//	}
}
