package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands.CFDElementDeleteCommand;

public class CFDElementComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		CFDElement element = (CFDElement) getHost().getModel();
		
		return new CFDElementDeleteCommand(element);
	}
}