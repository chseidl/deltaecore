package org.deltaecore.feature.graphical.base.figures;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;

public abstract class DEAbstractFigure extends Figure {
	private DEGraphicalEditor graphicalEditor;
	
	public DEAbstractFigure(DEGraphicalEditor graphicalEditor) {
		this.graphicalEditor = graphicalEditor;
	}
	
	public void update() {
	}

	protected DEGraphicalEditor getGraphicalEditor() {
		return graphicalEditor;
	}
	
	protected Rectangle getEffectiveBounds() {
		return getBounds();
	}
	
	public Rectangle getSelectionMarkerBounds() {
		Rectangle selectionBounds = getEffectiveBounds().getCopy();
		
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		selectionBounds.expand(theme.getSecondaryMargin(), theme.getSecondaryMargin());
		
		translateToAbsolute(selectionBounds);
		
		return selectionBounds;
	}
}