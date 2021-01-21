package eu.vicci.ecosystem.sft.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.gef.figures.SFTIntermediateFaultFigure;
import eu.vicci.ecosystem.sft.gef.util.ToolTipBuilder;

public class SFTIntermediateFaultEditPart extends SFTAbstractEditPart {

	@Override
	protected IFigure createFigure() {		
		SFTIntermediateFault model = (SFTIntermediateFault) getModel();

		return new SFTIntermediateFaultFigure(model.getId());
	}

	@Override
	protected void refreshVisuals() {
		SFTIntermediateFaultFigure figure = (SFTIntermediateFaultFigure) getFigure();
		SFTIntermediateFault model = (SFTIntermediateFault) getModel();
		SFTSoftwareFaultTreeEditPart parent = (SFTSoftwareFaultTreeEditPart) getParent();

		Point location = figure.getLocation();
		Dimension size = figure.getSize();
		
		Rectangle layout = new Rectangle(location, size);

		figure.getLabel().setText(model.getId());

		createAndSetTooltipText(figure, model);
		
		parent.setLayoutConstraint(this, figure, layout);
	}

	protected void createAndSetTooltipText(SFTIntermediateFaultFigure figure, SFTIntermediateFault model) {
		String d = ToolTipBuilder.getCorrectDescription(model.getDescription());
		figure.setTooltipText(model.getName() + "\n\n" + d);
	}
}