package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands.CFDCausalConnectionCreateCommand;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDUtil;

public class CFDPortGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Object model = getHost().getModel();
		Object newObject = request.getNewObject();
		
		if (model instanceof CFDOutPort && newObject instanceof CFDConnection) {
			CFDOutPort sourcePort = (CFDOutPort) model;
			CFDComponent component = CFDUtil.getComponentForPort(sourcePort);
			CFDConnection causalConnection = (CFDConnection) newObject;
			
			CFDCausalConnectionCreateCommand command = new CFDCausalConnectionCreateCommand(sourcePort, component, causalConnection);
			
			request.setStartCommand(command);
			
			return command;
		}
		
		return null;
	}
	
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCommand = request.getStartCommand();
		Object model = getHost().getModel();
		
		if (startCommand instanceof CFDCausalConnectionCreateCommand && model instanceof CFDInPort) {
			CFDCausalConnectionCreateCommand command = (CFDCausalConnectionCreateCommand) startCommand;

			CFDInPort targetPort = (CFDInPort) model;
			command.setTargetPort(targetPort);
			
			return command;
		}
		
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

}
