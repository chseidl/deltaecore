package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.base.dnd.DEDropBetweenTarget;
import org.deltaecore.feature.graphical.base.dnd.DEDropTarget;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureMoveCommand;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class DEFeatureModelLayoutEditPolicy extends DEAbstractLayoutEditPolicy {

	public DEFeatureModelLayoutEditPolicy() {
		super(true, false);
	}

	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		DEFeatureModelEditPart editPart = (DEFeatureModelEditPart) getHost();
		IFigure featureModelFigure = editPart.getFigure();

		DEGraphicalEditor graphicalEditor = editPart.getGraphicalEditor();
		DEGraphicalViewer graphicalViewer = graphicalEditor.getViewer();
		
		//New position should be mouse position in coordinate system of feature model
		Point newPosition = request.getLocation();
		featureModelFigure.translateToRelative(newPosition);
		
		Object model = child.getModel();
		
		if (model instanceof DEFeature) {
			DEFeature feature = (DEFeature) model;
			
			DEFeatureLayouterManager featureLayouterManager = graphicalEditor.getFeatureLayouterManager();
			DEDropTarget dropTarget = featureLayouterManager.determineDropTarget(newPosition.x, newPosition.y);
			
			if (dropTarget instanceof DEDropBetweenTarget) {
				DEDropBetweenTarget dropBetweenTarget = (DEDropBetweenTarget) dropTarget;
				
				DEFeature featureToTheLeft = (DEFeature) dropBetweenTarget.getObject1();
				DEFeature featureToTheRight = (DEFeature) dropBetweenTarget.getObject2();
				
				if (featureToTheLeft != feature && featureToTheRight != feature) {
					Rectangle leftBounds = featureLayouterManager.getBoundsOfFeature(featureToTheLeft);
					Rectangle rightBounds = featureLayouterManager.getBoundsOfFeature(featureToTheRight);
					
					int offset = 4;
					int x1 = leftBounds.x + leftBounds.width - 1;
					int x2 = rightBounds.x;
					
					
					int width = offset;
					int height = Math.max(leftBounds.height, rightBounds.height) + 2 * offset;
					int x = x1 + (x2 - x1 - (width - 1)) / 2;
					int y = Math.min(leftBounds.y, rightBounds.y) - offset;
					
					Rectangle bounds = new Rectangle(x, y, width, height);
					graphicalViewer.setDropTargetMarkerBounds(bounds);
					graphicalViewer.setDropTargetMarkerVisible(true);
					
					return new DEFeatureMoveCommand(feature, featureToTheRight, true);
				}
			}
		}
		
		graphicalViewer.setDropTargetMarkerVisible(false);
		return null;
	}
	
}
