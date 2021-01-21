package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDBasicEventImpl;


public class CFDBasicEventImplCustom extends CFDBasicEventImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "BasicEvent";
		message += " (";
		message += "id: " + getId() + ", ";
		message += "name: " + getName() + ", ";
		message += "probability: " + getProbability();
		message += ")";
		
		return message;
	}
}
