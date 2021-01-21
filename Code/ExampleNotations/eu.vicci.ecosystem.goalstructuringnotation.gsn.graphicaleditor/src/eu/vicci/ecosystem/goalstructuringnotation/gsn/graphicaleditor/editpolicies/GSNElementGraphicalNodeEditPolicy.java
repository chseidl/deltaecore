package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNConnectionCreateCommand;

public class GSNElementGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCommand = request.getStartCommand();
		Object model = getHost().getModel();
		
		GSNConnectionCreateCommand command = (GSNConnectionCreateCommand) startCommand;
		
		GSNElement targetElement = (GSNElement) model;
		command.setTargetElement(targetElement);
		
		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Object newObject = request.getNewObject();
		
		GSNElement sourceElement = (GSNElement) getHost().getModel();
		GSNConnection connection = (GSNConnection) newObject;
		GSNModel model = sourceElement.getModel();
	
		Command command = new GSNConnectionCreateCommand(model, connection, sourceElement);
		request.setStartCommand(command);
		return command;
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
