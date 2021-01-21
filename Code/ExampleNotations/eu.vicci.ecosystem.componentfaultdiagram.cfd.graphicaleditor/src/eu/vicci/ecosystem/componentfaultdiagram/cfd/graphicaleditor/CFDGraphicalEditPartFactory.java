package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.CFDBasicEventEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.CFDComponentEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.CFDDiagramEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.CFDGateEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.CFDIntermediateEventEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.connections.CFDConnectionEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports.CFDInPortEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports.CFDOutPortEditPart;

public class CFDGraphicalEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object object) {
		if (object instanceof CFDDiagram) {
			CFDDiagram diagram = (CFDDiagram) object;
			return new CFDDiagramEditPart(diagram);
		}
		
		if (object instanceof CFDComponent) {
			CFDComponent component = (CFDComponent) object;
			return new CFDComponentEditPart(component);
		}
		
		if (object instanceof CFDIntermediateEvent) {
			CFDIntermediateEvent intermediateEvent = (CFDIntermediateEvent) object;
			return new CFDIntermediateEventEditPart(intermediateEvent);
		}
		
		if (object instanceof CFDBasicEvent) {
			CFDBasicEvent basicEvent = (CFDBasicEvent) object;
			return new CFDBasicEventEditPart(basicEvent);
		}
		
		if (object instanceof CFDGate) {
			CFDGate gate = (CFDGate) object;
			return new CFDGateEditPart(gate);
		}

		if (object instanceof CFDOutPort) {
			CFDOutPort outPort = (CFDOutPort) object;
			return new CFDOutPortEditPart(outPort);
		}
		
		if (object instanceof CFDInPort) {
			CFDInPort inPort = (CFDInPort) object;
			return new CFDInPortEditPart(inPort);
		}
		
		if (object instanceof CFDConnection) {
			CFDConnection connection = (CFDConnection) object;
			return new CFDConnectionEditPart(connection);
		}
		
		String message = "Could not create edit part for \"" + object.getClass().getSimpleName() + "\".";
		System.err.println(message);
		return null;
//		throw new InvalidParameterException(message);
	}
}