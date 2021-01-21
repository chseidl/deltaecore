package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;

public class CFDElementDeleteCommand extends Command {
	private CFDElement element;
	
	//Internal state for undo.
	private CFDComponent oldComponent;
	
	public CFDElementDeleteCommand(CFDElement element) {
		this.element = element;
	}

	@Override
	public void execute() {
		oldComponent = (CFDComponent) element.eContainer();
		
		EStructuralFeature containingFeature = element.eContainingFeature();
		oldComponent.eSet(containingFeature, null);
		
		//TODO: delete connections, ports etc.
	}

	
	@Override
	public void undo() {
		EStructuralFeature containingFeature = element.eContainingFeature();
		oldComponent.eSet(containingFeature, element);
		
//		element.setContainer(oldContainer);
		
		//TODO: add undo support
	}
}