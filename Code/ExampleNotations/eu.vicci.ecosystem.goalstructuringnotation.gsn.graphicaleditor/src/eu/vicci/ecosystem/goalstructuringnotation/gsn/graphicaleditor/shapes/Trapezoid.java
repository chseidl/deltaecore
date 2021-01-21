package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class Trapezoid extends Shape {
	private int sheerOffset;
	
	public Trapezoid() {
		this(20);
	}
	
	public Trapezoid(int sheerOffset) {
		setSheerOffset(sheerOffset);
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		Path path = createPath();
		
		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillPath(path);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		//Insets to compensate for stroke width!
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);
		
		Path path = createPath(inset1, inset2);
		
		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.drawPath(path);
	}

	protected Path createPath() {
		return createPath(0, 0);
	}
	
	protected Path createPath(int inset1, int inset2) {
		Rectangle bounds = getBounds();
		
		float x1 = bounds.x + sheerOffset + inset1;
		float y1 = bounds.y + inset1;
		float x2 = bounds.x + bounds.width - inset1 - inset2;
		float y2 = bounds.y + inset1;
		float x3 = bounds.x + bounds.width - sheerOffset - inset1 - inset2;
		float y3 = bounds.y + bounds.height - inset1 - inset2;
		float x4 = bounds.x + inset1;
		float y4 = bounds.y + bounds.height - inset1 - inset2;
		
		
		Display display = Display.getCurrent();
		
		Path path = new Path(display);
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x4, y4);
		path.close();
		
		return path;
	}
	
	public float getSheerOffset() {
		return sheerOffset;
	}
	
	public void setSheerOffset(int sheerOffset) {
		this.sheerOffset = sheerOffset;
	}

	@Override
	public Rectangle getClientArea(Rectangle rect) {
		rect.x = bounds.x + sheerOffset;
		rect.y = bounds.y;
		rect.width = bounds.width - 2 * sheerOffset;
		rect.height = bounds.height;
		
		return rect;
	}
}
