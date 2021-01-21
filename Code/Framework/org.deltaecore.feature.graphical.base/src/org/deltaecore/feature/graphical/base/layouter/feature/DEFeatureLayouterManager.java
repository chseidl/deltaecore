package org.deltaecore.feature.graphical.base.layouter.feature;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Map.Entry;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.dnd.DEDropBetweenTarget;
import org.deltaecore.feature.graphical.base.dnd.DEDropOnTarget;
import org.deltaecore.feature.graphical.base.dnd.DEDropTarget;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * http://code.google.com/p/treelayout/
 * 
 * @author Florian
 */
public class DEFeatureLayouterManager {
	private static final int offsetX = calculateOffsetX();
	private static final int offsetY = calculateOffsetY();
	
	private static int calculateOffsetX() {
		//Margin to ensure selection of features is drawn correctly.
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		return theme.getPrimaryMargin() + 1;
	}
	
	private static int calculateOffsetY() {
		//Root feature never has the variation type circle drawn.
		return 0; //-DEFeatureModelEditorConstants.FEATURE_VARIATION_TYPE_EXTENT;
	}
	
	private DEFeatureModel featureModel;
	private TreeLayout<DEFeature> treeLayout;

	public DEFeatureLayouterManager(DEFeatureModel featureModel) {
		this.featureModel = featureModel;
		update();
	}
	
	public void update() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		DEFeature rootFeature = featureModel.getRootFeature();
		
		if (rootFeature == null) {
			return;
		}
		
		TreeForTreeLayout<DEFeature> tree = new DEFeatureTreeForTreeLayout(rootFeature);
		DEFeatureNodeExtentProvider extents = new DEFeatureNodeExtentProvider();
		DefaultConfiguration<DEFeature> configuration = new DefaultConfiguration<DEFeature>(theme.getFeatureExtentY(), theme.getFeatureExtentX()) {
			@Override
			public AlignmentInLevel getAlignmentInLevel() {
				return AlignmentInLevel.TowardsRoot;
			}
		};
		
		treeLayout = new TreeLayout<DEFeature>(tree, extents, configuration);
	}
	
	public Rectangle getBoundsOfFeature(DEFeature feature) {
		Rectangle2D.Double nodeBounds = getNodeBounds(feature);
		return DEGeometryUtil.rectangle2DToDraw2DRectangle(nodeBounds);
	}
	
	private Rectangle2D.Double getNodeBounds(DEFeature feature) {
		Map<DEFeature, Rectangle2D.Double> elements = treeLayout.getNodeBounds();
		Rectangle2D.Double nodeBounds = elements.get(feature);
		
		if (nodeBounds == null) {
			//For newly created elements
			nodeBounds = new Rectangle2D.Double(0, 0, 0, 0);
		}
		
		return adjustNodeBounds(nodeBounds, feature);
	}
	
	public DEDropTarget determineDropTarget(int x, int y) {
		Map<DEFeature, Rectangle2D.Double> nodeBounds = treeLayout.getNodeBounds();
		DEFeature featureOnTheRightSide = null;
		double smallestDistanceToFeatureOnTheRightSide = 0;
		
		DEFeature featureOnTheLeftSide = null;
		double smallestDistanceToFeatureOnTheLeftSide = 0;
		
		for (Entry<DEFeature, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
			DEFeature feature = entry.getKey();
			Rectangle2D.Double nodeBound = entry.getValue();
			
			nodeBound = adjustNodeBounds(nodeBound, feature);
			
			//Check if on same level
			double y1 = nodeBound.getY();
			
			//TODO: Take the max height of this level instead
//			treeLayout.getSizeOfLevel(1);
			double y2 = y1 + nodeBound.getHeight() - 1;
			
			if (y >= y1 && y <= y2) {
				double x1 = nodeBound.x;
				double x2 = x1 + nodeBound.width - 1;
				
				//Check if over feature
				if (x >= x1 && x <= x2) {
					return new DEDropOnTarget(feature);
				}
				
				if (x < x1) {
					double distanceToFeatureOnTheRightSide = x1 - x;
					
					if (featureOnTheRightSide == null || distanceToFeatureOnTheRightSide < smallestDistanceToFeatureOnTheRightSide) {
						featureOnTheRightSide = feature;
						smallestDistanceToFeatureOnTheRightSide = distanceToFeatureOnTheRightSide;
					}
				}

				if (x > x2) {
					double distanceToFeatureOnTheLeftSide = x - x2;
					
					if (featureOnTheLeftSide == null || distanceToFeatureOnTheLeftSide < smallestDistanceToFeatureOnTheLeftSide) {
						featureOnTheLeftSide = feature;
						smallestDistanceToFeatureOnTheLeftSide = distanceToFeatureOnTheLeftSide;
					}
				}
			}
		}
		
		if (featuresOnSidesBelongToSameGroup(featureOnTheLeftSide, featureOnTheRightSide)) {
			return new DEDropBetweenTarget(featureOnTheLeftSide, featureOnTheRightSide);
		}
		
		return null;
	}
	
	private static boolean featuresOnSidesBelongToSameGroup(DEFeature featureOnTheLeftSide, DEFeature featureOnTheRightSide) {
		if (featureOnTheLeftSide == null || featureOnTheRightSide == null) {
			return false;
		}
		
		DEGroup leftParentGroup = featureOnTheLeftSide.getParentOfFeature();
		DEGroup rightParentGroup = featureOnTheRightSide.getParentOfFeature();
		
		if (leftParentGroup == null || rightParentGroup == null) {
			return false;
		}
		
		return leftParentGroup == rightParentGroup;
	}
	
	private Rectangle2D.Double adjustNodeBounds(Rectangle2D.Double nodeBounds, DEFeature feature) {
		Rectangle2D.Double adjustedNodeBounds = (Rectangle2D.Double) nodeBounds.clone();
		
		adjustedNodeBounds.x += offsetX;
		adjustedNodeBounds.y += offsetY;
		
		if (!DEFeatureFigure.variationTypeCircleVisible(feature)) {
			DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
			adjustedNodeBounds.y += theme.getFeatureVariationTypeExtent();
		}
		
		return adjustedNodeBounds;
	}
}
