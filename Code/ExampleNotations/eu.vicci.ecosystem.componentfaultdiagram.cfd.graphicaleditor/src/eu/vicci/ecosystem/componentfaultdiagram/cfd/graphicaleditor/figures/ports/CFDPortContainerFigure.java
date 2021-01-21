package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;

public class CFDPortContainerFigure extends Figure {
	
	public CFDPortContainerFigure(boolean isInPort) {
//		setBackgroundColor(new Color(Display.getCurrent(), 255, 0, 255));
//		setOpaque(true);
		
		FlowLayout layout = new FlowLayout(false);
//		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
//		
//		int minorAlignment = isInPort ? FlowLayout.ALIGN_TOPLEFT : FlowLayout.ALIGN_BOTTOMRIGHT;
//		layout.setMinorAlignment(minorAlignment);
		
		layout.setStretchMinorAxis(true);
		layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		
		int majorAlignment = isInPort ? FlowLayout.ALIGN_TOPLEFT : FlowLayout.ALIGN_BOTTOMRIGHT;
		layout.setMajorAlignment(majorAlignment);
		
		setLayoutManager(layout);
	}
	
	public void update(List<? extends CFDPort> ports) {
		//TODO: implement
		List<?> children = getChildren();

		if (children.size() != ports.size()) {
			//Don't handle port additions or removals right now.
			return;
		}
		
		int i = 0;
		
		for (Object child : children) {
			if (child instanceof CFDPortFigure) {
				CFDPortFigure figure = (CFDPortFigure) child;
				CFDPort port = ports.get(i);
						
				figure.update(port);
			}
			
			i++;
		}
	}
}
