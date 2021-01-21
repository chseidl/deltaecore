package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports;

import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports.CFDInPortFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDUtil;

public class CFDInPortEditPart extends CFDPortEditPart {

	public CFDInPortEditPart(CFDInPort inPort) {
		super(inPort);
	}

	@Override
	protected CFDInPortFigure doCreateFigure() {
		return new CFDInPortFigure();
	}

	@Override
	protected Class<? extends CFDPort> getModelType() {
		return CFDInPort.class;
	}
	
	@Override
	protected CFDInPort getElement() {
		return (CFDInPort) super.getElement();
	}
	
	@Override
	protected List<CFDConnection> getModelSourceConnections() {
		CFDInPort inPort = getElement();
		
		return CFDUtil.findOutgoingConnectionsForPort(inPort);
	}

	@Override
	protected List<CFDConnection> getModelTargetConnections() {
		CFDInPort inPort = getElement();
		
		return CFDUtil.findIncomingConnectionsForPort(inPort);
	}
}
