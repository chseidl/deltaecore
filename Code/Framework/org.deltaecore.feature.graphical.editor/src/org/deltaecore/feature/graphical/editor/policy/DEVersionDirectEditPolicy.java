package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editparts.DEVersionEditPart;
import org.deltaecore.feature.graphical.base.figures.DEVersionFigure;
import org.deltaecore.feature.graphical.editor.commands.DEVersionRenameCommand;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

public class DEVersionDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		DEVersionEditPart editPart = getHost();
		DEVersion version = editPart.getModel();
		
		CellEditor cellEditor = request.getCellEditor();
		String newName = (String) cellEditor.getValue();
		
		return new DEVersionRenameCommand(version, newName);
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		DEVersionFigure figure = getHostFigure();
		
		CellEditor cellEditor = request.getCellEditor();
		String currentEditValue = (String) cellEditor.getValue();

		Label label = figure.getLabel();
		label.setText(currentEditValue);
	}

	@Override
	public DEVersionEditPart getHost() {
		return (DEVersionEditPart) super.getHost();
	}
	
	@Override
	protected DEVersionFigure getHostFigure() {
		return (DEVersionFigure) super.getHostFigure();
	}
}
