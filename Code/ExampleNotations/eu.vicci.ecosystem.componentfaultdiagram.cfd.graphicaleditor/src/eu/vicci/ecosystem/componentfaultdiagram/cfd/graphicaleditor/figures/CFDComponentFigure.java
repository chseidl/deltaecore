package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;

public class CFDComponentFigure extends CFDNamedElementFigure {
	private IFigure elementArea;
	
	@Override
	protected Class<? extends CFDElement> getElementType() {
		return CFDComponent.class;
	}
	
	@Override
	protected Label createNameLabel() {
		Label nameLabel = super.createNameLabel();
		nameLabel.setTextAlignment(PositionConstants.LEFT);
		return nameLabel;
	}
	
	@Override
	protected IFigure createMainFigure() {
		RoundedRectangle mainShape = new RoundedRectangle();
		mainShape.setCornerDimensions(new Dimension(15, 15));
		mainShape.setLayoutManager(new GridLayout());
		mainShape.setAntialias(1);
		
		Label idLabel = getIdLabel();
		mainShape.add(idLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		Label nameLabel = getNameLabel();
		mainShape.add(nameLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		elementArea = new Figure();
		elementArea.setLayoutManager(new XYLayout());
		mainShape.add(elementArea, new GridData(GridData.FILL_BOTH));
		
		return mainShape;
	}

	public IFigure getElementArea() {
		return elementArea;
	}
}
