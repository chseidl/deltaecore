package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;

public class CFDElementChangeConstraintCommand extends Command {
	private CFDElement element;
	private Rectangle newConstraint;
	
	//Internal state for undo
	private Rectangle oldConstraint;

	public CFDElementChangeConstraintCommand(CFDElement element, Rectangle newConstraint) {
		this.element = element;
		this.newConstraint = newConstraint;
	}
	
	@Override
	public void execute() {
		if (oldConstraint == null) {
			oldConstraint = element.getConstraints();
		}
		element.setConstraints(newConstraint);
	}

	@Override
	public void undo() {
		element.setConstraints(oldConstraint);
	}
}
