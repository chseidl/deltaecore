package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.connections;

import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPopulatingConnection;

public class CFDPopulatingConnectionEditPart extends CFDConnectionEditPart {
	
	public CFDPopulatingConnectionEditPart(CFDPopulatingConnection connection) {
		super(connection);
	}

	@Override
	protected Class<? extends EObject> getModelType() {
		return CFDPopulatingConnection.class;
	}
}
