package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class CFDInPortFigure extends CFDPortFigure {
	private static final Color backgroundColor = new Color(Display.getCurrent(), 150, 0, 50);
	
	public CFDInPortFigure() {
		super(false);
	}
	
	@Override
	protected Color getPortBackgroundColor() {
		return backgroundColor;
	}
}
