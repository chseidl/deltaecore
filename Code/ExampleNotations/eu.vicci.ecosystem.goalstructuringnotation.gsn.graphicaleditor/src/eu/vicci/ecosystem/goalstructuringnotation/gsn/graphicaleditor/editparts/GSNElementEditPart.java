package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.ui.views.properties.IPropertySource;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory.GSNFigureFactory;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNElementComponentEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNElementDirectEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNElementGraphicalNodeEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNModelLayoutEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.figures.GSNElementFigure;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.propertysources.GSNElementPropertySource;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util.PropertySourceEObjectEditPart;

public class GSNElementEditPart extends PropertySourceEObjectEditPart implements NodeEditPart {
	public GSNElementEditPart(GSNConcreteElement element) {
		super(element);
	}
	
	protected GSNConcreteElement getElement() {
		return (GSNConcreteElement) getModel();
	}
	
	@Override
	protected IFigure createFigure() {
		GSNConcreteElement element = getElement();
		GSNElementType type = element.getType();
		return GSNFigureFactory.createFigure(type);
	}

	protected GSNElementFigure getTypedFigure() {
		return (GSNElementFigure) getFigure();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new GSNElementComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GSNElementGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new GSNElementDirectEditPolicy());
//		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
	}

	@Override
	protected void refreshVisuals() {
		GSNElementFigure figure = getTypedFigure();
		GSNConcreteElement element = getElement();
		GSNModelEditPart modelEditPart =  (GSNModelEditPart) getParent();
		
		Rectangle constraints = element.getConstraints();
		
		//NOTE: Quick fix for generated models that don't set the "constraints" attribute. Find a better default here some time.
		if (constraints == null) {
			constraints = new Rectangle(50, 50, 0, 0);
		}
		
		if (constraints.width == 0 || constraints.height == 0) {
			Dimension defaultDimension = GSNModelLayoutEditPolicy.getDefaultDimension(element.getType());
			
			constraints.width = defaultDimension.width;
			constraints.height = defaultDimension.height;
		}
		
		modelEditPart.setLayoutConstraint(this, figure, constraints);
			  
		figure.update(element);
	}

	private ConnectionAnchor getConnectionAnchor() {
		GSNElementFigure figure = getTypedFigure();
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

	@Override
	protected List<GSNConnection> getModelSourceConnections() {
		GSNElement element = getElement();
		
		List<GSNConnection> connections = new ArrayList<GSNConnection>();
		
		connections.addAll(element.getOutgoingConnections());
		
//		System.out.println("Found " + connections.size() + " outgoing connections for " + element);
		
		return connections;
	}

	@Override
	protected List<GSNConnection> getModelTargetConnections() {
		GSNElement element = getElement();
		
		List<GSNConnection> connections = new ArrayList<GSNConnection>();
		connections.addAll(element.getIncomingConnections());
		
		return connections;
	}

	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEditing();
		} else {
			super.performRequest(request);
		}
	}
	
	private void performDirectEditing() {
		GSNElementFigure figure = getTypedFigure();
		
		DirectEditManager directEditManager = figure.createDirectEditManager(this);
		directEditManager.show();
	}
	
	@Override
	protected Class<? extends EObject> getModelType() {
		return GSNConcreteElement.class;
	}

	@Override
	protected IPropertySource createPropertySource() {
		GSNConcreteElement element = getElement();
		return new GSNElementPropertySource(element);
	}
}
