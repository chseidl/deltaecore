package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class CFDOutPortFigure extends CFDPortFigure {
	private static final Color backgroundColor = new Color(Display.getCurrent(), 0, 150, 50);
	
	public CFDOutPortFigure() {
		super(true);
	}
	
	@Override
	protected Color getPortBackgroundColor() {
		return backgroundColor;
	}
}
