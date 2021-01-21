package org.deltaecore.feature.graphical.base.dnd;

public class DEDropBetweenTarget extends DEDropTarget {
	private Object object1;
	private Object object2;
	
	public DEDropBetweenTarget(Object object1, Object object2) {
		this.object1 = object1;
		this.object2 = object2;
	}

	public Object getObject1() {
		return object1;
	}
	
	public Object getObject2() {
		return object2;
	}
}
