package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports;

import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports.CFDOutPortFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDUtil;

public class CFDOutPortEditPart extends CFDPortEditPart {

	public CFDOutPortEditPart(CFDOutPort outPort) {
		super(outPort);
	}

	@Override
	protected CFDOutPortFigure doCreateFigure() {
		return new CFDOutPortFigure();
	}

	@Override
	protected Class<? extends CFDPort> getModelType() {
		return CFDOutPort.class;
	}
	
	@Override
	protected CFDOutPort getElement() {
		return (CFDOutPort) super.getElement();
	}
	
	@Override
	protected List<CFDConnection> getModelSourceConnections() {
		CFDOutPort outPort = getElement();
		
		return CFDUtil.findOutgoingConnectionsForPort(outPort);
	}

	@Override
	protected List<CFDConnection> getModelTargetConnections() {
		CFDOutPort outPort = getElement();
		
		return CFDUtil.findIncomingConnectionsForPort(outPort);
	}
}
