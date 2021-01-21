package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editparts.DEAbstractEditPart;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.deltaecore.feature.graphical.base.editparts.DERootEditPart;
import org.deltaecore.feature.graphical.base.figures.DEAbstractFigure;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

public class DESelectionEditPolicy extends ResizableEditPolicy {
	private RectangleFigure selectionMarker;
	private EditPart editPart;
	
	public DESelectionEditPolicy(EditPart editPart) {
		this.editPart = editPart;
		
		selectionMarker = createSelectionMarker();
		
		registerListeners();
	}
	
	private void registerListeners() {
		if (editPart instanceof GraphicalEditPart) {
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart) editPart;
			
			IFigure figure = graphicalEditPart.getFigure();
			figure.addFigureListener(new FigureListener() {
				
				@Override
				public void figureMoved(IFigure source) {
					updateSelectionMarker();					
				}
			});
		}
	}
	
	private RectangleFigure createSelectionMarker() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		RectangleFigure selectionMarker = new RectangleFigure();
		
		selectionMarker.setFill(false);
		selectionMarker.setForegroundColor(theme.getSelectionSecondaryColor());
		selectionMarker.setLineWidth(theme.getLineWidth());
		
		return selectionMarker;
	}
	
	@Override
	protected void showSelection() {
		updateSelectionMarker();
		showSelectionMarker();
	}
	
	@Override
	protected void hideSelection() {
		hideSelectionFigure();
	}
	
	protected void updateSelectionMarker() {
		//TODO: primary and normal
		//TODO: Focus and not
		
		if (editPart instanceof DEAbstractEditPart) {
			DEAbstractEditPart abstractEditPart = (DEAbstractEditPart) editPart;
			IFigure rawFigure = abstractEditPart.getFigure();
			
			if (rawFigure instanceof DEAbstractFigure) {
				DEAbstractFigure figure = (DEAbstractFigure) rawFigure;
				
				Rectangle selectionMarkerBounds = figure.getSelectionMarkerBounds();
				
				DEFeatureModelEditPart featureModelEditPart = abstractEditPart.getFeatureModelEditPart();
				
				if (featureModelEditPart != null) {
					IFigure featureModelFigure = featureModelEditPart.getFigure();
					
					featureModelFigure.translateToRelative(selectionMarkerBounds);
					selectionMarker.setBounds(selectionMarkerBounds);
				}
			}
		}
	}

	protected void showSelectionMarker() {
		IFigure selectionMarkerLayer = getSelectionMarkerLayer();
		selectionMarkerLayer.add(selectionMarker);
	}
	
	protected void hideSelectionFigure() {
		IFigure selectionMarkerLayer = getSelectionMarkerLayer();
		
		if (selectionMarker.getParent() == selectionMarkerLayer) {
			selectionMarkerLayer.remove(selectionMarker);
		}
	}
	
	private IFigure getSelectionMarkerLayer() {
		//Draw the selection behind the actual elements.
		RootEditPart rawRootEditPart = editPart.getRoot();
		
		if (rawRootEditPart instanceof DERootEditPart) {
			DERootEditPart rootEditPart = (DERootEditPart) rawRootEditPart;
			IFigure layer = rootEditPart.getLayer(DERootEditPart.SELECTION_LAYER);
			return layer;
		}
		
		return null;
	}
}
