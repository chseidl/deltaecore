package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNElementRenameCommand;

public class GSNElementDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		GSNConcreteElement element = (GSNConcreteElement) getHost().getModel(); 
		String newDescription = (String) request.getCellEditor().getValue();
		
		return new GSNElementRenameCommand(element, newDescription);
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
//		String value = (String) request.getCellEditor().getValue();
//		
//		GSNGoalFigure faultFigure = (GSNGoalFigure) getHostFigure();
//		faultFigure.getNameLabel().setText(value);
//		faultFigure.setT
		
		//TODO:
	}
}
