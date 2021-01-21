package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;

public abstract class CFDEventEditPart extends CFDNamedElementEditPart {

	public CFDEventEditPart(CFDEvent event) {
		super(event);
	}

	@Override
	protected CFDEvent getElement() {
		return (CFDEvent) super.getElement();
	}
	
	@Override
	protected List<CFDOutPort> getOutPorts() {
		CFDEvent event = getElement();
		List<CFDOutPort> outPorts = new ArrayList<CFDOutPort>();
		outPorts.addAll(event.getOutPorts());
		return outPorts;
	}
}
