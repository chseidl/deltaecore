package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;

public class GSNElementCreateCommand extends Command {
	private GSNModel model;
	private GSNElement elementToCreate;
	private Rectangle constraints;
		
	public GSNElementCreateCommand(GSNModel model, GSNElement elementToCreate, Point location, Dimension size) {
		this.model = model;
		this.elementToCreate = elementToCreate;
		
		constraints = new Rectangle(location, size);
	}

	@Override
	public void execute() {
		elementToCreate.setModel(model);

		if (constraints != null) {
			elementToCreate.setConstraints(constraints);
		}
	}
	
	@Override
	public void undo() {
		elementToCreate.setModel(null);
		elementToCreate.setConstraints(null);
	}
}
