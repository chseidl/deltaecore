package eu.vicci.ecosystem.sft.gef.editparts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.gef.figures.SFTBasicFaultFigure;
import eu.vicci.ecosystem.sft.gef.util.ToolTipBuilder;

public class SFTBasicFaultEditPart extends SFTAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		SFTBasicFault model = (SFTBasicFault) getModel();
		String id = model.getId();
		return new SFTBasicFaultFigure(id);
	}

	@Override
	protected void refreshVisuals() {
		SFTBasicFaultFigure figure = (SFTBasicFaultFigure) getFigure();
		SFTSoftwareFaultTreeEditPart parent = (SFTSoftwareFaultTreeEditPart) getParent();
		SFTBasicFault model = (SFTBasicFault) getModel();

		Point location = figure.getLocation();
		Dimension size = figure.getSize();

		Rectangle layout = new Rectangle(location, size);

		figure.getLabel().setText(model.getId());
		String t = buildProbabilityText(model.getProbability());
		logger.trace("Probability: " + t);
		figure.getProbabilityLabel().setText(t);

		parent.setLayoutConstraint(this, figure, layout);

		createAndSetTooltipText(figure, model);
	}

	protected void createAndSetTooltipText(SFTBasicFaultFigure figure, SFTBasicFault model) {

		String d = ToolTipBuilder.getCorrectDescription(model.getDescription());

		figure.setTooltipText(model.getName() + "\n\n" + d);
	}

	private String buildProbabilityText(double probabilityValue) {

		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(',');
		DecimalFormat decimalFormat = new DecimalFormat("#.#############################", decimalFormatSymbols);

		return "(" + decimalFormat.format(probabilityValue) + ")";
	}
}