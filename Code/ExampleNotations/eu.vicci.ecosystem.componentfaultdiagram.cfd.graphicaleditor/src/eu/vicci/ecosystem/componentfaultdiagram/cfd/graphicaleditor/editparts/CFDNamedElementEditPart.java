package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies.CFDNamedElementDirectEditPolicy;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDNamedElementFigure;

public abstract class CFDNamedElementEditPart extends CFDElementEditPart {
	
	public CFDNamedElementEditPart(CFDElement element) {
		super(element);
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CFDNamedElementDirectEditPolicy());
	}

	@Override
	protected abstract CFDNamedElementFigure createElementFigure();
	
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEditing();
		} else {
			super.performRequest(request);
		}
	}

	
	private void performDirectEditing() {
		CFDNamedElementFigure figure = (CFDNamedElementFigure) getElementFigure();
		
		DirectEditManager directEditManager = figure.createDirectEditManager(this);
		directEditManager.show();
	}
}
