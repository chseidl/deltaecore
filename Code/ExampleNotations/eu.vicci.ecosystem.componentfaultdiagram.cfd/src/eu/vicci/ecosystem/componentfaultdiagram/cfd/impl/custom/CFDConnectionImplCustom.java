package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDConnectionImpl;

public class CFDConnectionImplCustom extends CFDConnectionImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "Connection";
		message += " (";
		message += getSourcePort() + " -> " + getTargetPort();
		message += ")";
		
		return message;
	}
}
