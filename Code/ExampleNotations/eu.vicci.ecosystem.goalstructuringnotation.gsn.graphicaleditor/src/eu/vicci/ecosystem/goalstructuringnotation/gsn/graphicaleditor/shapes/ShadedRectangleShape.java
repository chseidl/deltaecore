package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;

public class ShadedRectangleShape extends RectangleFigure {
	@Override
	protected void outlineShape(Graphics graphics) {
		GradientShapeUtil.gradientFillRectangle(graphics, this);
		super.outlineShape(graphics);
	}
}
