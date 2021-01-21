package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDInPortImpl;


public class CFDInPortImplCustom extends CFDInPortImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "InPort";
		message += " (";
		message += "name: " + getName();
		message += ")";
		
		return message;
	}
}
