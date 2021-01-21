package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands.CFDElementChangeConstraintCommand;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands.CFDElementCreateCommand;

public class CFDDiagramLayoutEditPolicy extends XYLayoutEditPolicy {
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		
	    if (newObject instanceof CFDElement) {
	    	CFDDiagram model = (CFDDiagram) getHost().getModel();
	    	CFDComponent rootComponent = model.getRootComponent();
	    	CFDElement element = (CFDElement) newObject;
	    	Point location = request.getLocation();
	    	Dimension size = request.getSize();
	    	
	    	if (size == null) {
	    		size = getDefaultDimension(element);
	    	}
	    	
	    	return new CFDElementCreateCommand(rootComponent, element, location, size);
	    }
	    
	    return null;
	}

	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object rawConstraint) {
		CFDElement element = (CFDElement) child.getModel();
		Rectangle newConstraint = (Rectangle) rawConstraint;
		
		return new CFDElementChangeConstraintCommand(element, newConstraint);
	}

	private Dimension getDefaultDimension(CFDElement element) {
		if (element instanceof CFDGate) {
			return new Dimension(50, 50);
		}
		
		return new Dimension(100, 70);
	}
}
