package eu.vicci.ecosystem.sft.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;
import eu.vicci.ecosystem.sft.gef.util.GradientShapeUtil;

public class SFTIntermediateFaultFigure extends SFTAbstractFigure {

	public SFTIntermediateFaultFigure(String id) {
		super();

		setLayoutManager(new XYLayout());

		this.setSize(new Dimension(ConstantsEnum.W_INTERMEDIATEFAULT, ConstantsEnum.H_INTERMEDIATEFAULT));

		Point location = abegoLayouter.getCoordinates(id);
		this.setLocation(location);

		tooltipFigure = new SFTToolTipFigure();
		setToolTip(tooltipFigure);
		initTooltip();

		setLabel();
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();

		setConstraint(label, new Rectangle(0, 0, r.width, r.height));

		label.invalidate();

		fillShape(graphics);
		outlineShape(graphics);
	}

	public SFTAbstractFigure getRectangleFigure() {
		return this;
	}

	private void fillShape(Graphics graphics) {
		GradientShapeUtil.gradientFillRectangle(graphics, this);
	}

	protected void outlineShape(Graphics graphics) {
		GradientShapeUtil.outlineShapeRectangle(graphics, bounds);
	}

	public void setTooltipText(String tooltipText) {
		tooltipFigure.setMessage(tooltipText);
	}

}