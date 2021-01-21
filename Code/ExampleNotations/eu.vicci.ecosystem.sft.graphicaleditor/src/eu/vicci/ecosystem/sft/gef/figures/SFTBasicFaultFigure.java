package eu.vicci.ecosystem.sft.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;
import eu.vicci.ecosystem.sft.gef.util.GradientShapeUtil;

public class SFTBasicFaultFigure extends SFTAbstractFigure {

	private Label probabilityLabel;

	public SFTBasicFaultFigure(String id) {
		super();

		setLayoutManager(new XYLayout());

		this.setSize(new Dimension(ConstantsEnum.W_BASICFAULT, ConstantsEnum.H_BASICFAULT));
		Point location = abegoLayouter.getCoordinates(id);
		this.setLocation(location);

		//add label for id
		setLabel();

		//add label for probability
		probabilityLabel = new Label();
		add(probabilityLabel);
		
		//add tooltip		
		tooltipFigure = new SFTToolTipFigure();		
		setToolTip(tooltipFigure);
		initTooltip();
		
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();

		setConstraint(label, new Rectangle(0, 0 - 10, r.width, r.height));
		setConstraint(probabilityLabel, new Rectangle(0, 0 + 10, r.width, r.height));

		probabilityLabel.invalidate();
		label.invalidate();

		fillShape(graphics);
		outlineShape(graphics);
	}

	private void fillShape(Graphics graphics) {
		GradientShapeUtil.gradientFillEllipse(graphics, this);
	}

	private void outlineShape(Graphics graphics) {
		GradientShapeUtil.outlineShapeEllipse(graphics, bounds);
	}
	
	public Label getProbabilityLabel(){
		return probabilityLabel;
	}
	
    public void setTooltipText(String tooltipText) {
        tooltipFigure.setMessage(tooltipText);
    }
}
