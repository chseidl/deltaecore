package org.deltaecore.core.variant.dependency;

public class DEDependency {
	private DEDependencyGraphNode target;
	private DEDependencyReason reason;
	private boolean active;
	
	public DEDependency(DEDependencyGraphNode target, DEDependencyReason reason) {
		this.target = target;
		this.reason = reason;
		active = true;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public void reset() {
		active = true;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public DEDependencyGraphNode getTarget() {
		return target;
	}

	//NOTE: For future extension and analysis
	public DEDependencyReason getReason() {
		return reason;
	}
}
