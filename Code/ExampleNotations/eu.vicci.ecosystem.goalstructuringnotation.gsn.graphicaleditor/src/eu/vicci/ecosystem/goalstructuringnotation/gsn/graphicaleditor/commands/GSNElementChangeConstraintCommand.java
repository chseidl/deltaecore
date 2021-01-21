package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;

public class GSNElementChangeConstraintCommand extends Command {
	private GSNElement element;
	private Rectangle newConstraint;
	
	//Internal state for undo
	private Rectangle oldConstraint;

	public GSNElementChangeConstraintCommand(GSNElement element, Rectangle newConstraint) {
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
