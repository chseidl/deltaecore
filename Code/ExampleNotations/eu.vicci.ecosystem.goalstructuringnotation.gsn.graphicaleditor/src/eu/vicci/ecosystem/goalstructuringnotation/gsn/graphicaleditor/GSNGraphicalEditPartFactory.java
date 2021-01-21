package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor;

import java.security.InvalidParameterException;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts.GSNConnectionEditPart;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts.GSNElementEditPart;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editparts.GSNModelEditPart;

public class GSNGraphicalEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object object) {
		if (object instanceof GSNModel) {
			GSNModel model = (GSNModel) object;
			return new GSNModelEditPart(model);
		}
		
		if (object instanceof GSNConcreteElement) {
			GSNConcreteElement element = (GSNConcreteElement) object;
			return new GSNElementEditPart(element);
		}
		
		if (object instanceof GSNConnection) {
			GSNConnection connection = (GSNConnection) object;
			return new GSNConnectionEditPart(connection);
		}
		
		throw new InvalidParameterException();
	}
}