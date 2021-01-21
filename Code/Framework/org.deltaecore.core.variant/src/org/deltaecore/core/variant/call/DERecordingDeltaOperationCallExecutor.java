package org.deltaecore.core.variant.call;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deltaecore.core.decore.DEArgument;
import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEModelElementIdentifier;
import org.deltaecore.core.decore.DEStatement;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.DEVariableReference;
import org.deltaecore.core.decore.DEcoreFactory;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.deltaecore.core.variant.exception.DEDeltaInterpretationException;
import org.deltaecore.core.variant.util.DEVariableNameBuilder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

//EXPERIMENTAL
public class DERecordingDeltaOperationCallExecutor extends DEDeltaOperationCallExecutor {
	private List<DEDeltaOperationCall> recordedDeltaOperationCalls;
	private boolean isRecording;
	
	public DERecordingDeltaOperationCallExecutor(String dialectNamespaceURI) {
		super(dialectNamespaceURI);
		
		recordedDeltaOperationCalls = new ArrayList<DEDeltaOperationCall>();
		isRecording = false;
	}

	public void startRecording() {
		recordedDeltaOperationCalls.clear();
		isRecording = true;
	}
	
	@Override
	public void interpretDeltaOperationCall(DEDeltaOperationCall deltaOperationCall) throws DEDeltaInterpretationException {
		if (isRecording) {
			recordedDeltaOperationCalls.add(deltaOperationCall);
		}
		
		super.interpretDeltaOperationCall(deltaOperationCall);
	}
	
	public DEDelta stopRecording() {
		isRecording = false;
		return createRecordedDelta();
	}
	
	//TODO: Think about copying the recorded statements and all referenced values as
	//multiple recording sessions might reuse variable declaration objects, which would place them
	//into two different models (which effectively removes them from the first model).
	private DEDelta createRecordedDelta() {
		DEDelta delta = DEcoreFactory.eINSTANCE.createDEDelta();
		
		delta.setName("Recorded Delta");

		//Just one block
		DEDeltaBlock block = DEcoreFactory.eINSTANCE.createDEDeltaBlock();
		delta.getBlocks().add(block);
		
		//Set delta dialect
		String dialectNamespaceURI = getDialectNamespaceURI();
		DEDeltaDialect deltaDialect = DEDeltaDialectExtensionRegistry.getDeltaDialect(dialectNamespaceURI);
		block.setDeltaDialect(deltaDialect);
		
		//Collect additional information
		List<EObject> affectedModels = new ArrayList<EObject>();
		List<DEStatement> newStatements = new ArrayList<DEStatement>();
		Set<DEVariableDeclaration> declaredVariables = new HashSet<DEVariableDeclaration>();
		DEVariableNameBuilder variableNameBuilder = new DEVariableNameBuilder();
				
		for (DEDeltaOperationCall deltaOperationCall : recordedDeltaOperationCalls) {
			List<DEArgument> arguments = deltaOperationCall.getArguments();
			
			for (DEArgument argument : arguments) {
				DEExpression2 expression = argument.getExpression();
				
				//Collect affected models
				if (expression instanceof DEModelElementIdentifier) {
					DEModelElementIdentifier modelElementIdentifier = (DEModelElementIdentifier) expression;
					
					EObject element = (EObject) modelElementIdentifier.getValue();
					EObject model = EcoreUtil.getRootContainer(element);
					
					if (!affectedModels.contains(model)) {
						affectedModels.add(model);
					}
				}
				
				//See if all arguments have satisfied variable references. If not, add
				//the respective declarations to the list of new statements.
				if (expression instanceof DEVariableReference) {
					DEVariableReference variableReference = (DEVariableReference) expression;
					
					DEVariableDeclaration variableDeclaration = variableReference.getVariable();
					
					if (!declaredVariables.contains(variableDeclaration)) {
						//Assign meaningful and unique names to the declared variables.
						variableNameBuilder.createUniqueVariableNameIfNecessary(variableDeclaration);
						
						//Complete statements by adding required variable declarations at the proper spot (immediately before first usage).
						newStatements.add(variableDeclaration);
						declaredVariables.add(variableDeclaration);
					}
				}
			}
			
			newStatements.add(deltaOperationCall);
		}
		
		//TODO: Set the collected affected models
//		List<EObject> requiredElements = block.getRequiredElements();
//		requiredElements.addAll(affectedModels);
		
		//Set the collected statements
		List<DEStatement> statements = block.getStatements();
		statements.addAll(newStatements);
		
		return delta;
	}
}
