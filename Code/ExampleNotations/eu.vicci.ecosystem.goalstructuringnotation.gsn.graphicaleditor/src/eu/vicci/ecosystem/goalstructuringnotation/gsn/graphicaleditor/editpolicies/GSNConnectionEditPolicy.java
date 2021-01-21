package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.editpolicies;

import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.commands.GSNConnectionDeleteCommand;

public class GSNConnectionEditPolicy extends ConnectionEditPolicy {

	@Override
	protected GSNConnectionDeleteCommand getDeleteCommand(GroupRequest request) {
		GSNConnection connection = (GSNConnection) getHost().getModel();
		
		return new GSNConnectionDeleteCommand(connection);
	}
}
