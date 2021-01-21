package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports.CFDPortContainerFigure;

public abstract class CFDElementFigure extends Figure {
	private CFDPortContainerFigure outPortCompartment;
	private Label idLabel;
	private CFDPortContainerFigure inPortCompartment;
	
	public CFDElementFigure() {
		initialize();
		createFigure();
	}
	
	protected void initialize() {
		idLabel = createIdLabel();
	}
	
	protected abstract Class<? extends CFDElement> getElementType();
	
	private void createFigure() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		setLayoutManager(layout);

		outPortCompartment = new CFDPortContainerFigure(false);
		add(outPortCompartment, new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		
		IFigure mainFigure = createMainFigure();
		add(mainFigure, new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		
		inPortCompartment = new CFDPortContainerFigure(true);
		add(inPortCompartment, new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
	}

	protected Label createIdLabel() {
		Label idLabel = new Label();
		idLabel.setTextAlignment(PositionConstants.LEFT);
		return idLabel;
	}
	
	protected abstract IFigure createMainFigure();
		
	public void update(CFDElement element) {
		Class<?> elementType = getElementType();
		
		if (element == null || elementType == null) {
			return;
		}
		
		if (elementType.isAssignableFrom(element.getClass())) {
			doUpdate(element);
		}
	}
	
	protected Label getIdLabel() {
		return idLabel;
	}
	
	protected void doUpdate(CFDElement element) {
		String id = element.getId();
		idLabel.setText(id);
	}

	public CFDPortContainerFigure getOutPortCompartment() {
		return outPortCompartment;
	}

	public CFDPortContainerFigure getInPortCompartment() {
		return inPortCompartment;
	}
}
