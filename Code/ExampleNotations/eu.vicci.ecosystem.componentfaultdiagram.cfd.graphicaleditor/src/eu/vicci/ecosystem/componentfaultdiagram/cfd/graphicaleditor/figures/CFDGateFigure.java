package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGateType;

public class CFDGateFigure extends CFDElementFigure {
	private Label gateTypeLabel;
	
	@Override
	protected void initialize() {
		super.initialize();
		gateTypeLabel = createGateTypeLabel();
	}
	
	protected Label createGateTypeLabel() {
		return new Label();
	}
	
	@Override
	protected Class<? extends CFDElement> getElementType() {
		return CFDGate.class;
	}

	@Override
	protected IFigure createMainFigure() {
		Shape mainShape = new Ellipse();
		mainShape.setLayoutManager(new GridLayout());
		mainShape.setAntialias(1);
		
		Label idLabel = getIdLabel();
		mainShape.add(idLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		mainShape.add(gateTypeLabel, new GridData(GridData.FILL_HORIZONTAL));
		
		return mainShape;
	}

	@Override
	public void update(CFDElement element) {
		super.update(element);

		CFDGate gate = (CFDGate) element;
		CFDGateType gateType = gate.getGateType();
		
		String gateTypeName = "";
		
		switch (gateType.getValue()) {
			case CFDGateType.AND_VALUE:
				gateTypeName = "AND";
				break;
			case CFDGateType.OR_VALUE:
				gateTypeName = "OR";
				break;
		}
		
		gateTypeLabel.setText(gateTypeName);
	}
}
