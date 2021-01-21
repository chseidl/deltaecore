package org.deltaecore.feature.graphical.base.editparts;

import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEGroupFigure;
import org.deltaecore.feature.graphical.base.util.DEAbstractAdapter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public class DEGroupEditPart extends DEAbstractEditPartWithAdapter {

	public DEGroupEditPart(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected DEAbstractAdapter createAdapter(DEAbstractEditPartWithAdapter editPart) {
		return new DEAbstractAdapter(editPart) {
			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(DEGroup.class);
			}
		};
	}

	@Override
	public DEGroup getModel() {
		return (DEGroup) super.getModel();
	}
	
	@Override
	protected IFigure createFigure() {
		DEGroup group = (DEGroup) getModel();
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		return new DEGroupFigure(group, graphicalEditor);
	}
	
	@Override
	public DEGroupFigure getFigure() {
		return (DEGroupFigure) super.getFigure();
	}
	
	@Override
	public void refreshVisuals() {
		DEGroupFigure figure = getFigure();
		
		figure.update();
		
		DEFeatureModelEditPart parent = (DEFeatureModelEditPart) getParent();
		Rectangle bounds = figure.getBounds();
		parent.setLayoutConstraint(this, figure, bounds);
	}
}
