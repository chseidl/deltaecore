package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDGateImpl;

public class CFDGateImplCustom extends CFDGateImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "Gate";
		message += " (";
		message += "id: " + getId() + ", ";
//		message += "name: " + getName() + ", ";
		message += "type: " + getGateType();
		message += ")";
		
		return message;
	}
}
