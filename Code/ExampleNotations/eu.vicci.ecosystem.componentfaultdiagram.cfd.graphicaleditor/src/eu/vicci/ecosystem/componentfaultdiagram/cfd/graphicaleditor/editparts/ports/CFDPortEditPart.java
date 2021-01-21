package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.EObjectEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies.CFDPortGraphicalNodeEditPolicy;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports.CFDPortFigure;

public abstract class CFDPortEditPart extends EObjectEditPart implements NodeEditPart {

	public CFDPortEditPart(CFDPort port) {
		super(port);
	}

	@Override
	protected abstract Class<? extends CFDPort> getModelType();

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CFDPortGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new NonResizableEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		CFDPortFigure figure = doCreateFigure();
		CFDPort port = getElement();
		
		figure.update(port);
		
		return figure;
	}
	
	protected abstract CFDPortFigure doCreateFigure();
	
	@Override
	protected CFDPort getElement() {
		return (CFDPort) super.getElement();
	}
	
	protected CFDPortFigure getTypedFigure() {
		return (CFDPortFigure) getFigure();
	}
	
	private ConnectionAnchor getConnectionAnchor() {
		CFDPortFigure figure = getTypedFigure();
		return figure.getConnectionAnchor();
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connectionEditPart) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connectionEditPart) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}
}
