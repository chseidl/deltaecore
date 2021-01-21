package org.deltaecore.core.variant.call;

import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.deltaecore.core.variant.exception.DEDeltaInterpretationException;
import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;
import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;

public class DEDeltaOperationCallExecutor {
	private String dialectNamespaceURI;
	
	private DEDeltaDialectInterpreter dialectInterpreter;
	private DEModelWriter modelWriter;
	
	public DEDeltaOperationCallExecutor(String dialectNamespaceURI) {
		this.dialectNamespaceURI = dialectNamespaceURI;
		
		initialize();
	}

	private void initialize() {
		//Find concrete interpreter for dialect in registry.
		dialectInterpreter = DEDeltaDialectExtensionRegistry.getDeltaDialectInterpreter(dialectNamespaceURI);
		
		if (dialectInterpreter == null) {
			throw new IllegalArgumentException("No suitable interpreter for delta dialect \"" + dialectNamespaceURI + "\" registered.");
		}
		
		modelWriter = DEModelWriter.createInstance();
	}
	
	public void interpretDeltaOperationCall(DEDeltaOperationCall deltaOperationCall) throws DEDeltaInterpretationException {
		//Delegate concrete interpretation to dialect interpreter.
		boolean wasInterpreted = dialectInterpreter.interpretDeltaOperationCall(deltaOperationCall, modelWriter);
		
		if (!wasInterpreted) {
			DEDeltaOperationDefinition operationDefinition = deltaOperationCall.getOperationDefinition();
			String operationName = operationDefinition.getName();
			throw new DEDeltaInterpretationException(deltaOperationCall, "Delta dialect interpreter failed to interpret call to \"" + operationName + "()\".");
		}
	}

	public DEModelWriter getModelWriter() {
		return modelWriter;
	}

	protected String getDialectNamespaceURI() {
		return dialectNamespaceURI;
	}
}
