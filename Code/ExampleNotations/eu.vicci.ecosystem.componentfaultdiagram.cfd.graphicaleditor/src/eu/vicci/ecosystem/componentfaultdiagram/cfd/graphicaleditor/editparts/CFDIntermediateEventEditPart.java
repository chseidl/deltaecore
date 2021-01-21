package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDIntermediateEventFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDNamedElementFigure;

public class CFDIntermediateEventEditPart extends CFDEventEditPart {

	public CFDIntermediateEventEditPart(CFDIntermediateEvent event) {
		super(event);
	}

	@Override
	protected Class<? extends CFDElement> getModelType() {
		return CFDIntermediateEvent.class;
	}

	@Override
	protected CFDNamedElementFigure createElementFigure() {
		return new CFDIntermediateEventFigure();
	}
	
//	@Override
//	protected List<CFDPort> getModelChildren() {
//		List<CFDPort> ports = super.getModelChildren();
//		
//		CFDIntermediateEvent intermediateEvent = (CFDIntermediateEvent) getElement();
//		
//		ports.addAll(intermediateEvent.getInPorts());
//		
//		return ports;
//	}
	
	@Override
	protected CFDIntermediateEvent getElement() {
		return (CFDIntermediateEvent) super.getElement();
	}
	
	@Override
	protected List<CFDInPort> getInPorts() {
		CFDIntermediateEvent intermediateEvent = getElement();
		List<CFDInPort> inPorts = new ArrayList<CFDInPort>();
		inPorts.addAll(intermediateEvent.getInPorts());
		return inPorts;
	}
}
