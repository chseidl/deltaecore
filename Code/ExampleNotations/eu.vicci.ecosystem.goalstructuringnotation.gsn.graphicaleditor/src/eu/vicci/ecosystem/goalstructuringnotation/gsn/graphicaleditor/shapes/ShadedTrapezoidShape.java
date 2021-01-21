package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Path;

public class ShadedTrapezoidShape extends Trapezoid {

	public ShadedTrapezoidShape() {
		super();
	}

	public ShadedTrapezoidShape(int sheerOffset) {
		super(sheerOffset);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		Path path = createPath();
		GradientShapeUtil.gradientFillPath(graphics, this, path);
		super.outlineShape(graphics);
	}
}
