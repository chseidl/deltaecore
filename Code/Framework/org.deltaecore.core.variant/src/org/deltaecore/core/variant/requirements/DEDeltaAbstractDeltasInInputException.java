package org.deltaecore.core.variant.requirements;

import java.util.List;

import org.deltaecore.core.decore.DEDelta;

public class DEDeltaAbstractDeltasInInputException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private List<? extends DEDelta> abstractDeltas;
	
	public DEDeltaAbstractDeltasInInputException(List<? extends DEDelta> abstractDeltas) {
		super(createMessage(abstractDeltas));
		
		this.abstractDeltas = abstractDeltas;
	}

	private static String createMessage(List<? extends DEDelta> abstractDeltas) {
		String message = "";
		
		message += "Abstract deltas were detected in the input: ";
		boolean isFirst = true;
		
		for (DEDelta abstractDelta : abstractDeltas) {
			if (!isFirst) {
				message += ", ";
			}
			
			message += abstractDelta.getName();
			isFirst = false;
		}
		
		return message;
	}

	public List<? extends DEDelta> getAbstractDeltas() {
		return abstractDeltas;
	}
}
