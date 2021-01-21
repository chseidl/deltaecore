package org.deltaecore.feature.graphical.editor.policy;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editparts.DEAbstractEditPart;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureCreateCommand;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class DEAbstractLayoutEditPolicy extends XYLayoutEditPolicy {
	private boolean allowFeatureCreation;
	private boolean allowVersionCreation;
	
	
	public DEAbstractLayoutEditPolicy(boolean allowFeatureCreation, boolean allowVersionCreation) {
		this.allowFeatureCreation = allowFeatureCreation;
		this.allowVersionCreation = allowVersionCreation;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		
		if (allowFeatureCreation && newObject instanceof DEFeature) {
			DEFeature feature = (DEFeature) newObject;

			DEAbstractEditPart editPart = (DEAbstractEditPart) getHost();
			Object parentObject = editPart.getModel();
			
			return new DEFeatureCreateCommand(feature, parentObject);
		}
		
		if (allowVersionCreation && newObject instanceof DEVersion) {
			DEVersion version = (DEVersion) newObject;

			DEAbstractEditPart editPart = (DEAbstractEditPart) getHost();
			Object parentObject = editPart.getModel();
			
			return new DEVersionCreateCommand(version, parentObject);
		}
		
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new DESelectionEditPolicy(child);
	}
}
