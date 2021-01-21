package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureEditPart;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureRenameCommand;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

public class DEFeatureDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		DEFeatureEditPart editPart = getHost();
		DEFeature feature = editPart.getModel();
		
		CellEditor cellEditor = request.getCellEditor();
		String newName = (String) cellEditor.getValue();
		
		return new DEFeatureRenameCommand(feature, newName);
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		DEFeatureFigure figure = getHostFigure();
		
		CellEditor cellEditor = request.getCellEditor();
		String currentEditValue = (String) cellEditor.getValue();

		Label label = figure.getLabel();
		label.setText(currentEditValue);
	}

	@Override
	public DEFeatureEditPart getHost() {
		return (DEFeatureEditPart) super.getHost();
	}
	
	@Override
	protected DEFeatureFigure getHostFigure() {
		return (DEFeatureFigure) super.getHostFigure();
	}
}
