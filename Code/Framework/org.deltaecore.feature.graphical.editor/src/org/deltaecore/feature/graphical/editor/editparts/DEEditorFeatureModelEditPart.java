package org.deltaecore.feature.graphical.editor.editparts;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.deltaecore.feature.graphical.editor.editor.DEEditorGraphicalEditor;
import org.deltaecore.feature.graphical.editor.figures.DEEditorFeatureModelFigure;
import org.deltaecore.feature.graphical.editor.policy.DEFeatureModelLayoutEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

public class DEEditorFeatureModelEditPart extends DEFeatureModelEditPart {
	public DEEditorFeatureModelEditPart(DEEditorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected IFigure createFigure() {
		DEFeatureModel featureModel = getModel();
		DEEditorGraphicalEditor graphicalEditor = (DEEditorGraphicalEditor) getGraphicalEditor();
		return new DEEditorFeatureModelFigure(featureModel, graphicalEditor);
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new DEFeatureModelLayoutEditPolicy());
	}
}
