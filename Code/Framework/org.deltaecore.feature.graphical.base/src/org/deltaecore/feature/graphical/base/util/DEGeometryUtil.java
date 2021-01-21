package org.deltaecore.feature.graphical.base.util;

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionLayouterManager;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionTreeLayouter;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;

public class DEGeometryUtil {
	public static int calculateFeatureWidth(DEFeature feature) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		int rawFeatureWidth = theme.getFeatureMinimumWidth();
		
		//Label width
		String featureName = feature.getName();
		int labelWidth = DEGeometryUtil.getTextWidth(featureName, theme.getFeatureFont());
		rawFeatureWidth = Math.max(rawFeatureWidth, labelWidth);
		
		//Version tree width
		if (!feature.getVersions().isEmpty()) {
			DEVersionTreeLayouter versionTreeLayouter = DEVersionLayouterManager.getLayouter(feature);
			Rectangle versionTreeBounds = versionTreeLayouter.getTreeBounds();
			int versionTreeWidth = versionTreeBounds.width;
			rawFeatureWidth = Math.max(rawFeatureWidth, versionTreeWidth);
		}
		
		return rawFeatureWidth + 2 * theme.getPrimaryMargin();
	}
	
	public static int calculateFeatureHeight(DEFeature feature) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		int versionAreaHeight = calculateVersionAreaHeight(feature);
		
		int featureHeight = theme.getFeatureNameAreaHeight() + versionAreaHeight;
		
		if (DEFeatureFigure.variationTypeCircleVisible(feature)) {
			featureHeight += theme.getFeatureVariationTypeExtent();
		}
		
		return featureHeight;
	}
	
	private static int calculateVersionAreaHeight(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		
		//Empty version areas are not drawn. Hence, they are calculated as height 0.
		if (versions.isEmpty()) {
			return 0;
		}
		
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		//Start with default height
		int rawVersionAreaHeight = theme.getFeatureVersionAreaDefaultHeight();
		
		
		//See if individual height is greater
		DEVersionTreeLayouter versionTreeLayouter = DEVersionLayouterManager.getLayouter(feature);
		Rectangle versionTreeBounds = versionTreeLayouter.getTreeBounds();
		int versionTreeHeight = versionTreeBounds.height;
		
		rawVersionAreaHeight = Math.max(rawVersionAreaHeight, versionTreeHeight);
		
		
		//See if the height of another version area on that tree level is greater 
		int highestVersionAreaHightOnSameLevel = findHighestVersionAreaHightOnSameLevel(feature);
		rawVersionAreaHeight = Math.max(rawVersionAreaHeight, highestVersionAreaHightOnSameLevel);
		
		return rawVersionAreaHeight + 2 * theme.getPrimaryMargin();
	}
	
	private static int findHighestVersionAreaHightOnSameLevel(DEFeature feature) {
		List<DEFeature> featuresOnSameTreeLevel = DEFeatureUtil.findFeaturesOnSameTreeLevel(feature);
		
		int highestVersionAreaHightOnSameLevel = 0;
		
		for (DEFeature featureOnSameTreeLevel : featuresOnSameTreeLevel) {
			List<DEVersion> versions = featureOnSameTreeLevel.getVersions();
			
			if (!versions.isEmpty()) {
				DEVersionTreeLayouter versionTreeLayouter = DEVersionLayouterManager.getLayouter(featureOnSameTreeLevel);
				
				Rectangle versionTreeBounds = versionTreeLayouter.getTreeBounds();
				int versionTreeHeight = versionTreeBounds.height;
				
				highestVersionAreaHightOnSameLevel = Math.max(highestVersionAreaHightOnSameLevel, versionTreeHeight);
			}
		}
		
		return highestVersionAreaHightOnSameLevel;
	}
	
	public static int calculateVersionWidth(DEVersion version) {
		String versionNumber = version.getNumber();
		return calculateVersionWidth(versionNumber);
	}
	
	public static int calculateVersionWidth(String versionNumber) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		int labelWidth = DEGeometryUtil.getTextWidth(versionNumber, theme.getVersionFont());
		int triangleWidth = theme.getVersionTriangleEdgeLength();
		return Math.max(labelWidth, triangleWidth) + 2 * theme.getSecondaryMargin();	
	}
	
	public static int calculateVersionHeight(DEVersion version) {
		String versionNumber = version.getNumber();
		return calculateVersionHeight(versionNumber);
	}
	
	public static int calculateVersionHeight(String versionNumber) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		int labelHeight = DEGeometryUtil.getTextHeight(versionNumber, theme.getVersionFont());
		int triangleHeight = theme.getVersionTriangleEdgeLength();
		return triangleHeight + theme.getSecondaryMargin() + labelHeight;
	}
	
	
	public static Point getStartCoordinate(DEFeature feature, DEFeatureLayouterManager layouterManager) {
		Rectangle bounds = layouterManager.getBoundsOfFeature(feature);
		
		int x = bounds.x + (bounds.width / 2);
		int y = bounds.y + bounds.height;

		return new Point(x, y);
	}
	
	public static Point getEndCoordinate(DEFeature feature, DEFeatureLayouterManager layouterManager) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Rectangle bounds = layouterManager.getBoundsOfFeature(feature);
		
		int x = bounds.x + (bounds.width / 2);
		int y = bounds.y;
		
		if (DEFeatureFigure.variationTypeCircleVisible(feature)) {
			y += theme.getFeatureVariationTypeExtent() / 2;
		}
		
		return new Point(x, y);
	}
	
	public static int getTextWidth(String text, Font font) {
		Label label = new Label();
		
		label.setText(text);
		label.setFont(font);
		
		return label.getPreferredSize().width;
	}

	public static int getTextHeight(String text, Font font) {
		Label label = new Label();
		
		label.setText(text);
		label.setFont(font);

		return label.getPreferredSize().height;
	}
	
	public static double calculateRotationAngleForLineWithEndpoints(Point startPoint, Point endPoint) {
		double deltaX = endPoint.x - startPoint.x;
		double deltaY = endPoint.y - startPoint.y;
		
		return Math.atan2(deltaY, deltaX);
	}
	
	public static Rectangle createBoundsFromPath(Path path, float lineWidth) {
		float[] rawBounds = new float[4];
		path.getBounds(rawBounds);
		return new Rectangle((int) Math.floor(rawBounds[0] - lineWidth / 2f) , (int) Math.floor(rawBounds[1] - lineWidth / 2f), (int) Math.ceil(rawBounds[2] + lineWidth), (int) Math.ceil(rawBounds[3] + lineWidth));
	}
	
	public static Rectangle rectangle2DToDraw2DRectangle(Rectangle2D nodeBounds) {
		return new Rectangle((int) nodeBounds.getX(), (int) nodeBounds.getY(), (int) nodeBounds.getWidth(), (int) nodeBounds.getHeight());
	}
}
