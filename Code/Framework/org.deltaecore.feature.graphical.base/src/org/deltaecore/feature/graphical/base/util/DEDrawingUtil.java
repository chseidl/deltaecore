package org.deltaecore.feature.graphical.base.util;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class DEDrawingUtil {
	private static Display display = Display.getCurrent();

	public static void drawSelection(Graphics graphics, Rectangle markArea, IFigure parentFigure, boolean previouslySelected) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Color lightColor = previouslySelected ? theme.getPreviousSelectionPrimaryColor() : theme.getSelectionPrimaryColor();
		Color darkColor = previouslySelected ? theme.getPreviousSelectionSecondaryColor() : theme.getSelectionSecondaryColor();
		Color lineColor = previouslySelected ? theme.getPreviousSelectionLineColor() : theme.getSelectionLineColor();
		
		Rectangle drawingBounds = transformMarkRectangleToDrawingBounds(markArea, parentFigure);
		
		DEDrawingUtil.gradientFillRectangle(graphics, drawingBounds, lightColor, darkColor);
		DEDrawingUtil.outlineRectangle(graphics, drawingBounds, lineColor);
	}
	
	public static void drawProblem(Graphics graphics, Rectangle markArea, IFigure parentFigure, boolean error) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Color lineColor = error ? theme.getErrorLineColor() : theme.getWarningLineColor();
		
		Rectangle drawingBounds = transformMarkRectangleToDrawingBounds(markArea, parentFigure);
		
		graphics.setLineDash(new float[] {4.0f, 3.0f});
		graphics.setLineStyle(SWT.LINE_CUSTOM);
		
		DEDrawingUtil.outlineRectangle(graphics, drawingBounds, lineColor);
		
		//Reset dash
		graphics.setLineDash((int[]) null);
	}
	
	private static Rectangle transformMarkRectangleToDrawingBounds(Rectangle markArea, IFigure parentFigure) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle drawingBounds = markArea.getCopy();
		
		Rectangle parentBounds = parentFigure.getBounds();
		drawingBounds.translate(parentBounds.x, parentBounds.y);
		
		drawingBounds.expand(theme.getSecondaryMargin(), theme.getSecondaryMargin());

		return drawingBounds;
	}
	
	public static void gradientFillRectangle(Graphics graphics, Rectangle rectangle, Color foreground, Color background) {
		setupGraphics(graphics, foreground, background);
		
		graphics.fillGradient(rectangle, true);
	}

	public static void gradientFillEllipsis(Graphics graphics, Rectangle bounds, Color foreground, Color background) {
		Path ellipsis = createEllipsis(bounds);
		
		Rectangle oldClip = new Rectangle();
		graphics.getClip(oldClip);
		
		graphics.setClip(ellipsis);
		gradientFillRectangle(graphics, bounds, foreground, background);
		
		graphics.setClip(oldClip);
	}

	public static void gradientFillPath(Graphics graphics, Path path, Color foreground, Color background) {
		//Smallest bounds that cover entire path
		float[] rawPathBounds = new float[4];
		path.getBounds(rawPathBounds);
		Rectangle bounds = new Rectangle((int) rawPathBounds[0], (int) rawPathBounds[1], (int) rawPathBounds[2], (int) rawPathBounds[3]);
		
		Rectangle oldClip = new Rectangle();
		graphics.getClip(oldClip);
		
		graphics.setClip(path);
		gradientFillRectangle(graphics, bounds, foreground, background);
		
		graphics.setClip(oldClip);
	}

	
	public static void outlineRectangle(Graphics graphics, Rectangle rectangle, Color color) {
		setupGraphics(graphics, color, null);
		
		graphics.drawRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public static void outlineEllipsis(Graphics graphics, Rectangle bounds, Color color) {
		setupGraphics(graphics, color, null);
		
		Path ellipsis = createEllipsis(bounds);
		
		Rectangle oldClip = new Rectangle();
		graphics.getClip(oldClip);
		
		graphics.drawPath(ellipsis);
		
		graphics.setClip(oldClip);
	}
	
	public static void outlinePath(Graphics graphics, Path path, Color color) {
		setupGraphics(graphics, color, null);
		
		graphics.drawPath(path);
	}
	
	
	public static void drawLine(Graphics graphics, Point startPoint, Point endPoint, Color color) {
		setupGraphics(graphics, color, null);
		
		graphics.drawLine(startPoint, endPoint);
	}
	
	private static void setupGraphics(Graphics graphics, Color foregroundColor, Color backgroundColor) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		graphics.setAntialias(SWT.ON);
		graphics.setLineStyle(Graphics.LINE_CUSTOM);
		graphics.setLineWidth(theme.getLineWidth());
		
		if (foregroundColor != null) {
			graphics.setForegroundColor(foregroundColor);
		}
		
		if (backgroundColor != null) {
			graphics.setBackgroundColor(backgroundColor);
		}
	}
	
	private static Path createEllipsis(Rectangle bounds) {
		Path ellipsis = new Path(display);
		
		ellipsis.addArc(bounds.x, bounds.y, bounds.width, bounds.height, 0.0f, 360.0f);
		ellipsis.close();
		
		return ellipsis;
	}
}