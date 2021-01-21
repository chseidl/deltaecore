package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class GradientShapeUtil {
	private static Display display = Display.getCurrent();
	
	private static Color gradientColor1 = new Color(display, 251, 251, 254);
	private static Color gradientColor2 = new Color(display, 246, 246, 250);
	
	public static void gradientFillRectangle(Graphics graphics, RectangleFigure shape) {
		Rectangle bounds = shape.getBounds();
		
		Path path = new Path(display);
		
		float x1 = bounds.x;
		float y1 = bounds.y;
		float x2 = bounds.x + bounds.width;
		float y2 = bounds.y + bounds.height;
		
		path.moveTo(x1, y1);
		path.lineTo(x2, y1);
		path.lineTo(x2, y2);
		path.lineTo(x1, y2);
		path.close();
		
		gradientFillPath(graphics, shape, path);
	}
	
	public static void gradientFillEllipse(Graphics graphics, Ellipse shape) {
		Rectangle bounds = shape.getBounds();
		
		Path path = new Path(display);
		path.addArc(bounds.x, bounds.y, bounds.width, bounds.height, 0.0f, 360.0f);
		path.close();
		
		gradientFillPath(graphics, shape, path);
	}
	
	public static void gradientFillPath(Graphics graphics, Shape shape, Path path) {
		Rectangle bounds = shape.getBounds();
		
		graphics.setClip(path);
		
		graphics.setForegroundColor(gradientColor1);
		graphics.setBackgroundColor(gradientColor2);
		graphics.fillGradient(bounds, true);
		
		graphics.setForegroundColor(shape.getForegroundColor());
		
		graphics.setClip(bounds);
	}
}
