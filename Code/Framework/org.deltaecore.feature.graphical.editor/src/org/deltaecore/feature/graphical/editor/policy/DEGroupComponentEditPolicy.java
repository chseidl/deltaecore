package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.editor.commands.DEGroupDeleteCommand;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class DEGroupComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		EditPart editPart = getHost();
		DEGroup group = (DEGroup) editPart.getModel();
		
		return new DEGroupDeleteCommand(group);
	}
}
