package eu.vicci.ecosystem.sft.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;
import eu.vicci.ecosystem.sft.gef.util.GradientShapeUtil;

public class SFTGateFigure extends SFTAbstractFigure {

	private Label gateTypeLabel;
	
	public SFTGateFigure(String id) {
		super();

		setLayoutManager(new XYLayout());

		Point location = abegoLayouter.getCoordinates(id);
		this.setLocation(location);
		this.setSize(new Dimension(ConstantsEnum.W_GATE, ConstantsEnum.H_GATE));

		setLabel();
		
		//get label for gate type (and or or)
		gateTypeLabel = new Label();
		add(gateTypeLabel);
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();

		setConstraint(label, new Rectangle(0, ConstantsEnum.TEXT_OFFSET_NEG, r.width, r.height));
		setConstraint(gateTypeLabel, new Rectangle(0, 0 + 8, r.width, r.height));

		gateTypeLabel.invalidate();
	
		label.invalidate();
		
		fillShape(graphics);
		outlineShape(graphics);
	}

	
	private void fillShape(Graphics graphics){
		GradientShapeUtil.gradientFillEllipse(graphics, this);
	}
	
	protected void outlineShape(Graphics graphics) {
		GradientShapeUtil.outlineShapeEllipse(graphics, bounds);
	}
	
	public Label getGateTypeLabel(){
		return gateTypeLabel;
	}
}
