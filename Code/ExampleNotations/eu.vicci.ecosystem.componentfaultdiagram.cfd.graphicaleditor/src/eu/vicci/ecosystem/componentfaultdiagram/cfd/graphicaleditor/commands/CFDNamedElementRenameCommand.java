package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDNamedElement;

public class CFDNamedElementRenameCommand extends Command {
	
	private CFDNamedElement namedElement;
	private String newName;

	//Internal state for undo
	private String oldName;
	
	public CFDNamedElementRenameCommand(CFDNamedElement namedElement, String newName) {
		this.namedElement = namedElement;
		this.newName = newName;
	}
	
	@Override
	public void execute() {
		oldName = namedElement.getName();
		namedElement.setName(newName);
	}

	@Override
	public void undo() {
		namedElement.setName(oldName);
	}
}
