package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.editor.commands.DEVersionDeleteCommand;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class DEVersionComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		EditPart editPart = getHost();
		DEVersion version = (DEVersion) editPart.getModel();
		
		return new DEVersionDeleteCommand(version);
	}
}
