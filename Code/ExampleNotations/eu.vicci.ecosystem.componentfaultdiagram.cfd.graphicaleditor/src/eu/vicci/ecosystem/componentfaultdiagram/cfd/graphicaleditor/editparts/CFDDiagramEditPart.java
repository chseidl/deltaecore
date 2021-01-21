package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies.CFDDiagramLayoutEditPolicy;

public class CFDDiagramEditPart extends EObjectEditPart {

	public CFDDiagramEditPart(CFDDiagram cfd) {
		super(cfd);
	}
	
	@Override
	protected CFDDiagram getElement() {
		return (CFDDiagram) super.getElement();
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
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CFDDiagramLayoutEditPolicy());
	}

	@Override
	protected List<CFDElement> getModelChildren() {
		CFDDiagram model = getElement();
		
		List<CFDElement> children = new ArrayList<CFDElement>();
		CFDComponent rootComponent = model.getRootComponent();
		
		if (rootComponent != null) {
			children.add(rootComponent);
		}
		
		return children;
	}

	@Override
	protected Class<? extends CFDDiagram> getModelType() {
		return CFDDiagram.class;
	}
}
