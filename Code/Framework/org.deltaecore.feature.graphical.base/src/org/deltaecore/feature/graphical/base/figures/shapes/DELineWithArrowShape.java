package org.deltaecore.feature.graphical.base.figures.shapes;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Transform;

public class DELineWithArrowShape extends Shape {
	private static final Polygon arrowHead = createArrowHead();
	
	private static Polygon createArrowHead() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Polygon arrowHead = new Polygon();
		PointList points = new PointList();

		// draw triangle (points from left to right, horizontal)
		int edgeLength = theme.getArrowHeadEdgeLength();
		
		points.addPoint(-edgeLength, -edgeLength);
		points.addPoint(-edgeLength, edgeLength);
		points.addPoint(2 * edgeLength, 0);
		points.addPoint(-edgeLength, -edgeLength);

		arrowHead.setPoints(points);

		return arrowHead;
	}
	
	private Point startPoint;
	private Point endPoint;
	
	public DELineWithArrowShape() {
		this(new Point(), new Point());
	}
	
	public DELineWithArrowShape(Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	private PointList rotateArrowHeadPointList() {
		//TODO: The tip of the arrow head would have to be a slight bit further back from the end of the line
		int deltaX = endPoint.x;
		int deltaY = endPoint.y;
		double angle = DEGeometryUtil.calculateRotationAngleForLineWithEndpoints(startPoint, endPoint);

		Transform transform = new Transform();
		transform.setTranslation(deltaX, deltaY);
		transform.setRotation(angle);
		
		PointList points = arrowHead.getPoints().getCopy();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.getPoint(i);
			point = transform.getTransformed(point);
			points.setPoint(point, i);
		}
		
		return points;
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		graphics.setLineWidth(theme.getVersionLineWidth());
		graphics.setForegroundColor(theme.getVersionArrowColor());
		graphics.setBackgroundColor(theme.getVersionArrowColor());
		
		PointList points = rotateArrowHeadPointList();
		
		graphics.drawLine(startPoint, endPoint);
		graphics.fillPolygon(points);
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
}
