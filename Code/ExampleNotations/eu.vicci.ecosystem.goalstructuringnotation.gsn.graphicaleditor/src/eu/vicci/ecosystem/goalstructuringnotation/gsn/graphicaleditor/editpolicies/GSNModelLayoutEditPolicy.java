package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNElementChangeConstraintCommand;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNElementCreateCommand;

public class GSNModelLayoutEditPolicy extends XYLayoutEditPolicy {
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		
	    if (newObject instanceof GSNElement) {
	    	GSNModel model = (GSNModel) getHost().getModel();
	    	GSNConcreteElement element = (GSNConcreteElement) newObject;
	    	Point location = request.getLocation();
	    	Dimension size = request.getSize();
	    	
	    	if (size == null) {
	    		GSNElementType type = element.getType();
	    		size = getDefaultDimension(type);
	    	}
	    	
	    	return new GSNElementCreateCommand(model, element, location, size);
	    }
	    
	    return null;
	}

	public static Dimension getDefaultDimension(GSNElementType type) {
		switch (type.getValue()) {
			case GSNElementType.SOLUTION_VALUE:
				return new Dimension(90, 90);
		}
		
		return new Dimension(100, 70);
	}
	
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object rawConstraint) {
		GSNElement element = (GSNElement) child.getModel();
		Rectangle newConstraint = (Rectangle) rawConstraint;
		
		return new GSNElementChangeConstraintCommand(element, newConstraint);
	}
}
