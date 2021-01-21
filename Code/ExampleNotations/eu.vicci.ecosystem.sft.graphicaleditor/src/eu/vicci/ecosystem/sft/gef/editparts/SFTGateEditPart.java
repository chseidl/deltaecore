package eu.vicci.ecosystem.sft.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.gef.figures.SFTGateFigure;

public class SFTGateEditPart extends SFTAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		return new SFTGateFigure(((SFTGate) getModel()).getId());
	}

	@Override
	protected void refreshVisuals() {
		SFTGateFigure figure = (SFTGateFigure) getFigure();
		SFTGate model = (SFTGate) getModel();
		SFTSoftwareFaultTreeEditPart parent = (SFTSoftwareFaultTreeEditPart) getParent();

		Point location = figure.getLocation();
		Dimension size = figure.getSize();

		Rectangle layout = new Rectangle(location, size);

		figure.getLabel().setText(model.getId());

		figure.getLabel().setText(model.getId());
		String t = model.getGateType().toString();
		figure.getGateTypeLabel().setText(t);
		
		parent.setLayoutConstraint(this, figure, layout);
	}
}
