package org.deltaecore.feature.graphical.base.figures;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class DEGroupFigure extends DEAbstractFigure {
	private DEGroup group;
	
	private Path groupTypeArc;
	private List<Path> lines;
	
	public DEGroupFigure(DEGroup group, DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
		
		this.group = group;
		lines = new ArrayList<Path>();
		
		setLayoutManager(new XYLayout());
		update();
	}

	private void createGroupTypeArc() {
		groupTypeArc = null;
		
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		List<DEFeature> features = group.getFeatures();
		
		if (!groupTypeVisible()) {
			return;
		}
		
		DEFeature firstFeature = features.get(0);
		DEFeature lastFeature = features.get(features.size() - 1);
		
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		DEFeatureLayouterManager featureLayouterManager = graphicalEditor.getFeatureLayouterManager();
		
		Point originPoint = DEGeometryUtil.getStartCoordinate(group.getParentOfGroup(), featureLayouterManager);
		Point leftLineEndPoint = DEGeometryUtil.getEndCoordinate(firstFeature, featureLayouterManager);
		Point rightLineEndPoint = DEGeometryUtil.getEndCoordinate(lastFeature, featureLayouterManager);
		
		Point intersectionPointOfLeftLineAndCircle = scaleLineEndPoint(originPoint, leftLineEndPoint, theme.getGroupSymbolRadius());
		

		int circleX = originPoint.x - theme.getGroupSymbolRadius();
		int circleY = originPoint.y - theme.getGroupSymbolRadius();
		
		int circleWidth = 2 * theme.getGroupSymbolRadius();
		int circleHeight = 2 * theme.getGroupSymbolRadius();
		
		
		//Here is the problem: Trigonometric functions assume a Cartesian coordinate system with y coordinate extending towards the top.
		//However, drawing canvas has a Cartesian coordinate system where y extends towards the bottom.
		//Hence, coordinates have to be mirrored along the y-axis before being used as input to the trigonometric functions.
		
		Point mirroredOriginPoint = originPoint.getCopy();
		mirroredOriginPoint.y = -mirroredOriginPoint.y;
		
		Point mirroredLeftLineEndPoint = leftLineEndPoint.getCopy();
		mirroredLeftLineEndPoint.y = -mirroredLeftLineEndPoint.y;
		
		Point mirroredRightLineEndPoint = rightLineEndPoint.getCopy();
		mirroredRightLineEndPoint.y = -mirroredRightLineEndPoint.y;
		
		
		double rawAngle1 = DEGeometryUtil.calculateRotationAngleForLineWithEndpoints(mirroredOriginPoint, mirroredLeftLineEndPoint);
		double rawAngle2 = DEGeometryUtil.calculateRotationAngleForLineWithEndpoints(mirroredOriginPoint, mirroredRightLineEndPoint);
		
		
		float angle1 = (float) Math.toDegrees(rawAngle1);
		float angle2 = (float) Math.toDegrees(rawAngle2);
		
		
		Display display = Display.getCurrent();
		groupTypeArc = new Path(display);
		
		groupTypeArc.moveTo(originPoint.x, originPoint.y);
		groupTypeArc.lineTo(intersectionPointOfLeftLineAndCircle.x, intersectionPointOfLeftLineAndCircle.y);
		groupTypeArc.addArc(circleX, circleY, circleWidth, circleHeight, angle1, angle2 - angle1);
		groupTypeArc.close();
	}
	
	private void createLines() {
		lines.clear();
		
		DEFeature parentFeature = group.getParentOfGroup();
		
		if (parentFeature == null) {
			return;
		}
		
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		DEFeatureLayouterManager featureLayouterManager = graphicalEditor.getFeatureLayouterManager();
		
		Point startPoint = DEGeometryUtil.getStartCoordinate(parentFeature, featureLayouterManager);
		List<DEFeature> features = group.getFeatures();
		
		for (DEFeature feature : features) {
			Point endPoint = DEGeometryUtil.getEndCoordinate(feature, featureLayouterManager);
			
			Path line = createLine(startPoint, endPoint);
			lines.add(line);
		}
	}
	
	private static Path createLine(Point p1, Point p2) {
		Display display = Display.getCurrent();
		Path line = new Path(display);
		
		line.moveTo(p1.x, p1.y);
		line.lineTo(p2.x, p2.y);
		
		return line;
	}
	
	private static Point scaleLineEndPoint(Point startPoint, Point endPoint, double targetLength) {
		double vectorX = endPoint.x - startPoint.x;
		double vectorY = endPoint.y - startPoint.y;
		double currentLength = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
		
		double scaleFactor = targetLength / currentLength;
		
		double finalX = startPoint.x + scaleFactor * vectorX;
		double finalY = startPoint.y + scaleFactor * vectorY;
		
		return new Point((int) Math.round(finalX), (int) Math.round(finalY));
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		//Draw lines
		for (Path line : lines) {
			DEDrawingUtil.outlinePath(graphics, line, theme.getLineColor());
		}
		
		//Draw group type
		if (groupTypeVisible() && groupTypeArc != null) {
			Color light = group.isAlternative() ? theme.getGroupAlternativePrimaryColor() : theme.getGroupOrPrimaryColor();
			Color dark = group.isAlternative() ? theme.getGroupAlternativeSecondaryColor() : theme.getGroupOrSecondaryColor();
			
			DEDrawingUtil.gradientFillPath(graphics, groupTypeArc, light, dark);
			DEDrawingUtil.outlinePath(graphics, groupTypeArc, theme.getLineColor());
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		createGroupTypeArc();
		createLines();
		updateBounds();
		
		//This is necessary to reflect changes in variation type.
		repaint();
	}
	
	private void updateBounds() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		int lineWidth = theme.getLineWidth();
		
		Rectangle bounds = groupTypeArc != null ? DEGeometryUtil.createBoundsFromPath(groupTypeArc, lineWidth) : null;
		
		for (Path line : lines) {
			Rectangle lineBounds = DEGeometryUtil.createBoundsFromPath(line, lineWidth);
			
			if (bounds == null) {
				bounds = lineBounds;
			} else {
				bounds.union(lineBounds);
			}
		}
		
		if (bounds == null) {
			bounds = new Rectangle(0, 0, 0, 0);
		}
		
		setBounds(bounds);
	}
	
	private boolean groupTypeVisible() {
		if (group.isAlternative() || group.isOr()) {
			List<DEFeature> features = group.getFeatures();
			
			return features.size() >= 2;
		}
		
		return false;
	}

	@Override
	protected Rectangle getEffectiveBounds() {
		Rectangle bounds = getBounds();
		Rectangle effectiveBounds = bounds.getCopy();
		
		if (!groupTypeVisible()) {
			DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
			int offset = (theme.getFeatureVariationTypeExtent() - 1) / 2;
			
			effectiveBounds.x -= offset;
			effectiveBounds.width += 2 * offset;
		}
		
		return effectiveBounds;
	}
	
	public DEGroup getGroup() {
		return group;
	}
}

