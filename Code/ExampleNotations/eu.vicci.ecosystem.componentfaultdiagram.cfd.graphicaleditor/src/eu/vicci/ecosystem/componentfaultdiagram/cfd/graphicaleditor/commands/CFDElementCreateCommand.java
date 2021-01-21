package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;

public class CFDElementCreateCommand extends Command {
	private CFDComponent component;
	private CFDElement elementToCreate;
	private Rectangle constraints;
	
	public CFDElementCreateCommand(CFDComponent component, CFDElement element, Point location, Dimension size) {
		assert(component != null);
		
		this.component = component;
		this.elementToCreate = element;
		
		constraints = new Rectangle(location, size);
	}

	@Override
	public void execute() {
		elementToCreate.setComponent(component);
		elementToCreate.setConstraints(constraints);
	}
	
	@Override
	public void undo() {
		elementToCreate.setComponent(null);
		elementToCreate.setConstraints(null);
	}
}
