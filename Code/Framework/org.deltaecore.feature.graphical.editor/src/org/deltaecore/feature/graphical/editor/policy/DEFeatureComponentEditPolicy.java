package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureDeleteCommand;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class DEFeatureComponentEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		EditPart editPart = getHost();
		DEFeature feature = (DEFeature) editPart.getModel();
		
		return new DEFeatureDeleteCommand(feature);
	}
}
