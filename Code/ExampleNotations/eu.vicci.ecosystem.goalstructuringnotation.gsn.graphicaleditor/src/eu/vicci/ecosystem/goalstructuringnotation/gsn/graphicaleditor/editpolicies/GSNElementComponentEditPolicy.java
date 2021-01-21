package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNElementDeleteCommand;

public class GSNElementComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		GSNElement element = (GSNElement) getHost().getModel();
		
		return new GSNElementDeleteCommand(element);
	}
}