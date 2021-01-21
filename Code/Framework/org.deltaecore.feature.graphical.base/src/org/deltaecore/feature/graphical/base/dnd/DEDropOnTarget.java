package org.deltaecore.feature.graphical.base.dnd;

public class DEDropOnTarget extends DEDropTarget {
	private Object object;
	
	public DEDropOnTarget(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
}
