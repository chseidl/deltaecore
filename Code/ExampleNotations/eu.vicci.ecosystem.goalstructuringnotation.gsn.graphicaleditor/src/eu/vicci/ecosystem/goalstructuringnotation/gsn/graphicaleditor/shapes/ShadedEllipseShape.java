package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;

public class ShadedEllipseShape extends Ellipse {
	@Override
	protected void outlineShape(Graphics graphics) {
		GradientShapeUtil.gradientFillEllipse(graphics, this);
		super.outlineShape(graphics);
	}
}
