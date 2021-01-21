package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.shapes;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class ShadedRoundedRectangleShape extends RoundedRectangle {

	@Override
	protected void outlineShape(Graphics graphics) {
		Display display = Display.getCurrent();
		Path path = new Path(display);
		
		Dimension cornerDimensions = getCornerDimensions();
		Rectangle bounds = getBounds();
		
		float effectiveWidth = bounds.width - 2 * cornerDimensions.width;
		float effectiveHeight = bounds.height - 2 * cornerDimensions.height;
		
		if (effectiveWidth < 0.0f) {
			effectiveWidth = 0.0f;
		}
		
		if (effectiveHeight < 0.0f) {
			effectiveHeight = 0.0f;
		}
		
		path.moveTo(bounds.x + cornerDimensions.width, bounds.y);
		path.lineTo(bounds.x + cornerDimensions.width + effectiveWidth, bounds.y);
		
		//First Corner (UR)
		path.addArc(bounds.x + cornerDimensions.width + effectiveWidth, bounds.y, cornerDimensions.width, cornerDimensions.height, 90.0f, -90.0f);
		path.lineTo(bounds.x + bounds.width, bounds.y + cornerDimensions.height + effectiveHeight);
		
		//Second Corner (LR)
		path.addArc(bounds.x + cornerDimensions.width + effectiveWidth, bounds.y + cornerDimensions.height + effectiveHeight, cornerDimensions.width, cornerDimensions.height, 0.0f, -90.0f);
		path.lineTo(bounds.x + cornerDimensions.width, bounds.y + bounds.height);
		
		//Third Corner (LL)
		path.addArc(bounds.x, bounds.y + cornerDimensions.height + effectiveHeight, cornerDimensions.width, cornerDimensions.height, 270.0f, -90.0f);
		path.lineTo(bounds.x, bounds.y + cornerDimensions.height);
		
		//Fourth Corner (UL
		path.addArc(bounds.x, bounds.y, cornerDimensions.width, cornerDimensions.height, 180.0f, -90.0f);
		
		
		path.close();
		
		GradientShapeUtil.gradientFillPath(graphics, this, path);
		
		super.outlineShape(graphics);
	}

}
