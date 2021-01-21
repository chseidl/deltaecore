package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDComponentImpl;

public class CFDComponentImplCustom extends CFDComponentImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "Component";
		message += " (";
		message += "id: " + getId() + ", ";
		message += "name: " + getName();
		message += ")";
		
		return message;
	}
}
