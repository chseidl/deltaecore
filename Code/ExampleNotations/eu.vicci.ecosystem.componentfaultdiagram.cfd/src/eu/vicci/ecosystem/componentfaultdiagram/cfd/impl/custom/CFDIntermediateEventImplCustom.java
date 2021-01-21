package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDIntermediateEventImpl;


public class CFDIntermediateEventImplCustom extends CFDIntermediateEventImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "IntermediateEvent";
		message += " (";
		message += "id: " + getId() + ", ";
		message += "name: " + getName() + ", ";
		message += ")";
		
		return message;
	}
}
