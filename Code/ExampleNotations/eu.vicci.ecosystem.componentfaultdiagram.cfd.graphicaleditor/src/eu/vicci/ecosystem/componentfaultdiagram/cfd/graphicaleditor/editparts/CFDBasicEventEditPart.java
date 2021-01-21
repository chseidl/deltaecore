package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDBasicEventFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDNamedElementFigure;

public class CFDBasicEventEditPart extends CFDEventEditPart {

	public CFDBasicEventEditPart(CFDBasicEvent event) {
		super(event);
	}

	@Override
	protected Class<? extends CFDElement> getModelType() {
		return CFDBasicEvent.class;
	}

	@Override
	protected CFDNamedElementFigure createElementFigure() {
		return new CFDBasicEventFigure();
	}
	
	
	@Override
	protected CFDBasicEvent getElement() {
		return (CFDBasicEvent) super.getElement();
	}
	
	@Override
	protected List<CFDInPort> getInPorts() {
		//No in ports for basic events.
		return new ArrayList<CFDInPort>();
	}
}
