package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDElementFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDGateFigure;

public class CFDGateEditPart extends CFDElementEditPart {

	public CFDGateEditPart(CFDGate gate) {
		super(gate);
	}

	@Override
	protected Class<? extends CFDElement> getModelType() {
		return CFDGate.class;
	}

	@Override
	protected CFDElementFigure createElementFigure() {
		return new CFDGateFigure();
	}

	@Override
	protected CFDGate getElement() {
		return (CFDGate) super.getElement();
	}
	
	@Override
	protected List<CFDOutPort> getOutPorts() {
		CFDGate gate = getElement();
		List<CFDOutPort> outPorts = new ArrayList<CFDOutPort>();
		outPorts.addAll(gate.getOutPorts());
		return outPorts;
	}
	
	@Override
	protected List<CFDInPort> getInPorts() {
		CFDGate gate = getElement();
		List<CFDInPort> inPorts = new ArrayList<CFDInPort>();
		inPorts.addAll(gate.getInPorts());
		return inPorts;
	}
}
