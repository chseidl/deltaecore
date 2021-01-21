/**
 * 
 */
package org.deltaecore.feature.graphical.base.layouter.version;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.deltaecore.feature.util.DEVersionUtil;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class performs the layouting process of the tree.
 * model elements.
 * 
 * @author Florian
 * 
 */
public class DEVersionTreeLayouter {
	private TreeLayout<DEVersion> treeLayout;
	
	private static final int offsetX = calculateOffsetX();
	private static final int offsetY = calculateOffsetY();
	
	private static int calculateOffsetX() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		return theme.getPrimaryMargin();
	}
	
	private static int calculateOffsetY() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		return theme.getFeatureVariationTypeExtent() + theme.getFeatureNameAreaHeight() + theme.getPrimaryMargin();
	}
	
	public DEVersionTreeLayouter(DEFeature feature) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		DEVersion initialVersion = DEVersionUtil.getRootVersion(feature);
		
		TreeForTreeLayout<DEVersion> tree = new DEVersionTreeForTreeLayout(initialVersion);
		DEVersionNodeExtentProvider extentProvider = new DEVersionNodeExtentProvider();

		DefaultConfiguration<DEVersion> configuration = new DefaultConfiguration<DEVersion>(theme.getVersionExtentX(), theme.getVersionExtentY()) {
			@Override
			public Location getRootLocation() {
				return Location.Left;
			}
		};
		
		treeLayout = new TreeLayout<DEVersion>(tree, extentProvider, configuration);
	}

	/**
	 * @return A point representing the upper left point of the rectangle representing the bounds.
	 */
	public Point getCoordinates(DEVersion version) {
		Rectangle2D.Double nodeBounds = getNodeBounds(version);
		
		return new Point((int) nodeBounds.getX(), (int) nodeBounds.getY());
	}

	public Rectangle getBounds(DEVersion version) {
		Rectangle2D.Double nodeBounds = getNodeBounds(version);
		return DEGeometryUtil.rectangle2DToDraw2DRectangle(nodeBounds);
	}
	
	private Rectangle2D.Double adjustNodeBounds(Rectangle2D.Double nodeBounds, DEVersion version) {
		Rectangle2D.Double adjustedNodeBounds = (Rectangle2D.Double) nodeBounds.clone();
		
		adjustedNodeBounds.x += offsetX;
		adjustedNodeBounds.y += offsetY;
		
		DEFeature feature = version.getFeature();
		
		if (!DEFeatureFigure.variationTypeCircleVisible(feature)) {
			DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
			adjustedNodeBounds.y -= theme.getFeatureVariationTypeExtent();
		}
		
		return adjustedNodeBounds;
	}

	private Rectangle2D.Double getNodeBounds(DEVersion version) {
		Map<DEVersion, Double> elements = treeLayout.getNodeBounds();
		Rectangle2D.Double nodeBounds = elements.get(version);
		
		return adjustNodeBounds(nodeBounds, version);
	}
	
	public Rectangle getTreeBounds() {
		Rectangle2D rawBounds = treeLayout.getBounds();
		return DEGeometryUtil.rectangle2DToDraw2DRectangle(rawBounds);
	}
}
