package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.ui.views.properties.IPropertySource;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNConnectionEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.figures.GSNConnectionFigure;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.propertysources.GSNConnectionPropertySource;

public class GSNConnectionEditPart extends AbstractConnectionEditPart {
	private Adapter listener;
	private IPropertySource propertySource;
	
	public GSNConnectionEditPart(GSNConnection connection) {
		setModel(connection);
		propertySource = null;
		
		//Register listener for changes.
		listener = new Adapter() {
			@Override
			public void notifyChanged(Notification notification) {
				refreshVisuals();
				refreshChildren();
				refreshSourceConnections();
				refreshTargetConnections();
			}

			@Override
			public Notifier getTarget() {
				return getElement();
			}

			@Override
			public void setTarget(Notifier newTarget) {
			}

			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(GSNConnection.class);
			}
		};
	}
	
	protected GSNConnection getElement() {
		return (GSNConnection) getModel();
	}
	
	protected GSNConnectionFigure getTypedFigure() {
		return (GSNConnectionFigure) getFigure();
	}
	
	@Override
	public void activate() {
		if (!isActive()) {
			EObject element = getElement();
			element.eAdapters().add(listener);
		}
		
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			EObject element = getElement();
			element.eAdapters().remove(listener);
		}

		super.deactivate();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new GSNConnectionEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		return new GSNConnectionFigure();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class) {
			return getPropertySource();
		}
		
		return super.getAdapter(key);
	}
	
	protected IPropertySource getPropertySource() {
		if (propertySource == null) {
			propertySource = createPropertySource();
		}
		
		return propertySource;
	}

	protected IPropertySource createPropertySource() {
		GSNConnection connection = getElement();
		return new GSNConnectionPropertySource(connection);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		
		GSNConnection connection = getElement();
		GSNConnectionFigure figure = getTypedFigure();
		figure.update(connection);
	}
}
