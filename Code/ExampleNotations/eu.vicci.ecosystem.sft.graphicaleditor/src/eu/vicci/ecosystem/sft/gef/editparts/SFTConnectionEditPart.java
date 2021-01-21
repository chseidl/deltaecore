package eu.vicci.ecosystem.sft.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.gef.figures.SFTConnectionFigure;
import eu.vicci.ecosystem.sft.util.SFTConnection;

public class SFTConnectionEditPart extends SFTAbstractEditPart {

	@Override
	protected IFigure createFigure() {
		String gate = ((SFTConnection) getModel()).getGate();
		String fault = ((SFTConnection) getModel()).getFault();

		return new SFTConnectionFigure(gate, fault);
	}

	@Override
	protected void refreshVisuals() {

		SFTConnectionFigure figure = (SFTConnectionFigure) getFigure();
		SFTSoftwareFaultTreeEditPart parent = (SFTSoftwareFaultTreeEditPart) getParent();

		Point location = figure.getPolyline().getLocation();
		Dimension size = figure.getPolyline().getSize();

	
		Rectangle layout = new Rectangle(location, size);

		parent.setLayoutConstraint(this, figure, layout);
	}

}
