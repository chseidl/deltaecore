package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.connections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;

public class CFDConnectionEditPart extends EObjectConnectionEditPart {
	public CFDConnectionEditPart(CFDConnection connection) {
		//NOTE: Remember this: Not setting the model for connections leaves GEF unable to detect that it already has an edit part
		//for a particular connection, which effectively causes an individual connection to be created for the source and target connection.
		super(connection);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		return connection;
	}

	@Override
	protected Class<? extends EObject> getModelType() {
		return CFDConnection.class;
	}
}
