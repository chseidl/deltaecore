package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.connections;

import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;

public class CFDCausalConnectionEditPart extends CFDConnectionEditPart {
	
	public CFDCausalConnectionEditPart(CFDConnection connection) {
		super(connection);
	}

	@Override
	protected Class<? extends EObject> getModelType() {
		return CFDConnection.class;
	}
}
