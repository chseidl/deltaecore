package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDEvent;

public abstract class CFDEventFigure extends CFDNamedElementFigure {
	@Override
	protected Class<? extends CFDElement> getElementType() {
		return CFDEvent.class;
	}
	
	@Override
	protected IFigure createMainFigure() {
		Shape mainShape = new RectangleFigure();
		mainShape.setLayoutManager(new GridLayout());
		mainShape.setAntialias(1);
		
		Label idLabel = getIdLabel();
		mainShape.add(idLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		Label nameLabel = getNameLabel();
		mainShape.add(nameLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		return mainShape;
	}
}
