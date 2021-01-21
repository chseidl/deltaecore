package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.figures;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;

public class GSNConnectionFigure extends PolylineConnection {
	private GSNConnectionType currentConnectionType;
	
	private static final Color white = new Color(Display.getCurrent(), 255, 255, 255);
	
	public GSNConnectionFigure() {
		currentConnectionType = null;
		
		setAntialias(1);
	}
	
	public void update(GSNConnection connection) {
		//Change connection decoration etc.
		GSNConnectionType type = connection.getType();
		
		if (currentConnectionType == null || !currentConnectionType.equals(currentConnectionType)) {
			exchangeLineStyle(type);
			
			currentConnectionType = type;
		}
	}
	
	private void exchangeLineStyle(GSNConnectionType type) {
		//TODO: Externalize?!
		switch (type.getValue()) {
			case GSNConnectionType.SOLVED_BY_VALUE:
				setTargetDecoration(createArrowTip(true));
				break;
				
			case GSNConnectionType.IN_CONTEXT_OF_VALUE:
				setTargetDecoration(createArrowTip(false));
				break;
		}
	}
	
	private PolygonDecoration createArrowTip(boolean fill) {
		PolygonDecoration arrowTip = new PolygonDecoration();
		
		arrowTip.setAntialias(SWT.ON);
		arrowTip.setScale(10, 5);
		
		if (!fill) {
			arrowTip.setBackgroundColor(white);
		}
		
		return arrowTip;
	}
}
