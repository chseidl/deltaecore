package eu.vicci.ecosystem.sft.gef.figures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;
import eu.vicci.ecosystem.sft.treelayout.AbegoLayouter;

public abstract class SFTAbstractFigure extends Figure {

	protected SFTToolTipFigure tooltipFigure;

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	protected Rectangle bounds = this.getBounds();

	protected Label label;

	protected AbegoLayouter abegoLayouter = AbegoLayouter.getInstance();

	public SFTAbstractFigure() {
	}

	protected void setLabel() {
		label = new Label();
		add(label);
	}

	public Label getLabel() {
		return label;
	}
	
	/**
	 * Sets the tooltip messages for all elements to the predefined string.
	 */
	protected void initTooltip(){
		tooltipFigure.setMessage(ConstantsEnum.NO_DESC_FOR_TOOLTIP);
	}
	
}