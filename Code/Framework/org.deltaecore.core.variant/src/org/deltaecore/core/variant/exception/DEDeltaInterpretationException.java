package org.deltaecore.core.variant.exception;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEStatement;

public class DEDeltaInterpretationException extends Exception {
	private static final long serialVersionUID = 1L;

	private DEStatement statement;

//	public DEDeltaInterpretationException(DEStatement statement) {
//		this(statement, "");
//	}
	
	public DEDeltaInterpretationException(DEStatement statement, String message) {
		super(message);
		
		this.statement = statement;
	}
	
	public DEStatement getStatement() {
		return statement;
	}
	
	public DEDeltaBlock getBlock() {
		return (DEDeltaBlock) statement.eContainer();
	}
	
	public DEDelta getDelta() {
		return getBlock().getDelta();
	}
	
}
