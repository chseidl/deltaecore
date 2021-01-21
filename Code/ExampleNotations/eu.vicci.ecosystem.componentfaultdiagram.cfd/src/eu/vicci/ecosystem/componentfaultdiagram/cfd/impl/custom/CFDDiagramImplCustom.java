package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDDiagramImpl;

public class CFDDiagramImplCustom extends CFDDiagramImpl {
	@Override
	public String toString() {
		String message = "";
		
		message += "Diagram";
		message += " (";
		message += ")";
		
		return message;
	}
}
