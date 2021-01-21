package org.deltaecore.feature.graphical.editor.editparts;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editparts.DEGroupEditPart;
import org.deltaecore.feature.graphical.editor.policy.DEGroupComponentEditPolicy;
import org.deltaecore.feature.graphical.editor.policy.DEGroupLayoutEditPolicy;
import org.eclipse.gef.EditPolicy;

public class DEEditorGroupEditPart extends DEGroupEditPart {

	public DEEditorGroupEditPart(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DEGroupLayoutEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DEGroupComponentEditPolicy());
	}
}
