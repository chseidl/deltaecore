package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GSNConnectionDeleteCommand extends Command {

	private GSNConnection connection;

	// Internal state for undo
	private GSNModel model;
	private GSNConnectionType type;
	private GSNElement sourceElement;
	private GSNElement targetElement;

	public GSNConnectionDeleteCommand(GSNConnection connection) {
		this.connection = connection;
	}

	@Override
	public boolean canExecute() {
		return connection != null;
	}

	@Override
	public void execute() {
		model = connection.getModel();
		type = connection.getType();
		sourceElement = connection.getSourceElement();
		targetElement = connection.getTargetElement();

		connection.setModel(null);
		connection.setType(null);
		connection.setSourceElement(null);
		connection.setTargetElement(null);
	}

	@Override
	public void undo() {
		connection.setModel(model);
		connection.setType(type);
		connection.setSourceElement(sourceElement);
		connection.setTargetElement(targetElement);
	}
}