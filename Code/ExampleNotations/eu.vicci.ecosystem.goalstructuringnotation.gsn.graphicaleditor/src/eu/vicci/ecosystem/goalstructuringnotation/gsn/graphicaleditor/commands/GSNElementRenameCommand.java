package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;

public class GSNElementRenameCommand extends Command {
	
	private GSNConcreteElement element;
	private String newDescription;

	//Internal state for undo
	private String oldDescription;
	
	public GSNElementRenameCommand(GSNConcreteElement element, String newDescription) {
		this.element = element;
		this.newDescription = newDescription;
	}
	
	@Override
	public void execute() {
		oldDescription = element.getDescription();
		element.setDescription(newDescription);
	}

	@Override
	public void undo() {
		element.setDescription(oldDescription);
	}
}
