package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDOutPortImpl;

public class CFDOutPortImplCustom extends CFDOutPortImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "OutPort";
		message += " (";
		message += "name: " + getName();
		message += ")";
		
		return message;
	}
}
