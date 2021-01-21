package org.deltaecore.feature.graphical.editor.editparts;

import org.deltaecore.feature.graphical.base.editparts.DEVersionEditPart;
import org.deltaecore.feature.graphical.base.figures.DEVersionFigure;
import org.deltaecore.feature.graphical.editor.editor.DEEditorGraphicalEditor;
import org.deltaecore.feature.graphical.editor.policy.DEVersionComponentEditPolicy;
import org.deltaecore.feature.graphical.editor.policy.DEVersionDirectEditPolicy;
import org.deltaecore.feature.graphical.editor.policy.DEVersionLayoutEditPolicy;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;

import de.christophseidl.util.gef.util.LabelCellEditorLocator;
import de.christophseidl.util.gef.util.LabelDirectEditManager;

public class DEEditorVersionEditPart extends DEVersionEditPart {
	public DEEditorVersionEditPart(DEEditorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DEVersionLayoutEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DEVersionDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DEVersionComponentEditPolicy());
	}
	
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEditing();
		}
	}
	
	protected void performDirectEditing() {
		DEVersionFigure figure = getFigure();
		Label label = figure.getLabel();
		
		LabelDirectEditManager manager = new LabelDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(label), label);
		
	    manager.show();
	}
}
