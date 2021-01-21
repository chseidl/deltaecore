package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies.GSNModelLayoutEditPolicy;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util.EObjectEditPart;

public class GSNModelEditPart extends EObjectEditPart {

	public GSNModelEditPart(GSNModel model) {
		super(model);
	}
	
	@Override
	protected GSNModel getElement() {
		return (GSNModel) super.getElement();
	}
	
	@Override
	protected IFigure createFigure() {
		FreeformLayer figure = new FreeformLayer();
		figure.setLayoutManager(new FreeformLayout());

		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setAntialias(SWT.ON);
		
		return figure;
	}

	@Override
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new GSNModelLayoutEditPolicy());
//		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
	}

	@Override
	protected List<GSNElement> getModelChildren() {
		GSNModel model = getElement();
		
		List<GSNElement> children = new ArrayList<GSNElement>();
		children.addAll(model.getElements());
		
		return children;
	}

	@Override
	protected Class<? extends EObject> getModelType() {
		return GSNModel.class;
	}
	
}
