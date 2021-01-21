package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.graphics.Color;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;

public abstract class CFDPortFigure extends Figure {
	private Shape portShape;
	private Label nameLabel;
	
	private ConnectionAnchor connectionAnchor;
	
	public CFDPortFigure(boolean labelAbove) {
		initialize();
		createFigure(labelAbove);
		
//		setBackgroundColor(new Color(Display.getCurrent(), 255, 0, 255));
		setOpaque(true);
	}
	
	private void initialize() {
		portShape = new RectangleFigure();
		portShape.setSize(12, 12);
		Color portBackgroundColor = getPortBackgroundColor();
		
		if (portBackgroundColor != null) {
			portShape.setBackgroundColor(portBackgroundColor);
		}
		
		nameLabel = new Label();
	}
	
	protected abstract Color getPortBackgroundColor();
	
	protected void createFigure(boolean labelAbove) {
		GridLayout layout = new GridLayout();
		setLayoutManager(layout);
		
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 3;
		
		if (labelAbove) {
			add(nameLabel, new GridData(GridData.FILL_HORIZONTAL));
			add(portShape, new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		} else {
			add(nameLabel, new GridData(GridData.FILL_HORIZONTAL));
			add(portShape, new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		}
	}
	
	public void update(CFDPort port) {
		String name = port.getName();
		nameLabel.setText(name);
	}
	
	public ConnectionAnchor getConnectionAnchor() {
		if (connectionAnchor == null) {
			connectionAnchor = new ChopboxAnchor(portShape);
		}
		
		return connectionAnchor;
	}
}
