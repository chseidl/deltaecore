package org.deltaecore.core.decore.util;

import java.util.List;

import org.deltaecore.core.decore.DEDelta;

public class DEDeltaRequirementsCycleException extends Exception {
	private static final long serialVersionUID = 1L;

	private DEDelta affectedDelta;
	private List<? extends DEDelta> requirementsCycle;
	
	public DEDeltaRequirementsCycleException(DEDelta affectedDelta, List<? extends DEDelta> requirementsCycle) {
		super(createMessage(affectedDelta, requirementsCycle));
		
		this.affectedDelta = affectedDelta;
		this.requirementsCycle = requirementsCycle;
	}
	
	private static String createMessage(DEDelta affectedDelta, List<? extends DEDelta> requirementsCycle) {
		String message = "A requirements cycle was detected for delta \"" + affectedDelta.getName() + "\" containing a subset of the following deltas: ";
		
		boolean isFirst = true;
		
		for (DEDelta delta : requirementsCycle) {
			if (!isFirst) {
				message += ", ";
			}
			
			message += "\"" + delta.getName() + "\"";
			isFirst = false;
		}
		
		return message;
	}

	public DEDelta getAffectedDelta() {
		return affectedDelta;
	}
	
	public List<? extends DEDelta> getRequirementsCycle() {
		return requirementsCycle;
	}
}
