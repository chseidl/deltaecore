package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDNamedElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands.CFDNamedElementRenameCommand;

public class CFDNamedElementDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		CFDNamedElement namedElement = (CFDNamedElement) getHost().getModel(); 
		String newDescription = (String) request.getCellEditor().getValue();
		
		return new CFDNamedElementRenameCommand(namedElement, newDescription);
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}
}
