package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GSNConnectionCreateCommand extends Command {

	private GSNConnection connection;
	private GSNModel model;
	private GSNElement sourceElement;
	private GSNElement targetElement;

	public GSNConnectionCreateCommand(GSNModel model, GSNConnection connection, GSNElement sourceElement) {
		this.connection = connection;
		this.model = model;
		this.sourceElement = sourceElement;
		this.targetElement = null;
	}

	@Override
	public boolean canExecute() {
		return (targetElement != null);
	}

	@Override
	public void execute() {
		connection.setModel(model);
		connection.setSourceElement(sourceElement);
		connection.setTargetElement(targetElement);
	}

	@Override
	public void undo() {
		connection.setModel(null);
		connection.setSourceElement(null);
		connection.setTargetElement(null);
	}
	
	public void setTargetElement(GSNElement targetElement) {
		this.targetElement = targetElement;
	}

}
