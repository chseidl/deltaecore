package org.deltaecore.feature.graphical.editor.editparts;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureEditPart;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.editor.editor.DEEditorGraphicalEditor;
import org.deltaecore.feature.graphical.editor.figures.DEEditorFeatureFigure;
import org.deltaecore.feature.graphical.editor.policy.DEFeatureComponentEditPolicy;
import org.deltaecore.feature.graphical.editor.policy.DEFeatureDirectEditPolicy;
import org.deltaecore.feature.graphical.editor.policy.DEFeatureLayoutEditPolicy;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;

import de.christophseidl.util.gef.util.LabelCellEditorLocator;
import de.christophseidl.util.gef.util.LabelDirectEditManager;

public class DEEditorFeatureEditPart extends DEFeatureEditPart {
	public DEEditorFeatureEditPart(DEEditorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected DEEditorFeatureFigure createFigure() {
		DEFeature feature = getModel();
		DEEditorGraphicalEditor graphicalEditor = (DEEditorGraphicalEditor) getGraphicalEditor();
		return new DEEditorFeatureFigure(feature, graphicalEditor);
	}
	
	@Override
	public DEEditorFeatureFigure getFigure() {
		return (DEEditorFeatureFigure) super.getFigure();
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DEFeatureLayoutEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DEFeatureDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DEFeatureComponentEditPolicy());
	}
	
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEditing();
		}
	}
	
	protected void performDirectEditing() {
		DEFeatureFigure figure = getFigure();
		Label label = figure.getLabel();
		
		LabelDirectEditManager manager = new LabelDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(label), label);
		
	    manager.show();
	}
}
