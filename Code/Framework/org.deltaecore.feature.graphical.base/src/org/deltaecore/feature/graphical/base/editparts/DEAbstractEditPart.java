package org.deltaecore.feature.graphical.base.editparts;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class DEAbstractEditPart extends AbstractGraphicalEditPart {
	private DEGraphicalEditor graphicalEditor;
	
	public DEAbstractEditPart(DEGraphicalEditor graphicalEditor) {
		this.graphicalEditor = graphicalEditor;
	}

	@Override
	protected void createEditPolicies() {
	}

	public DEGraphicalEditor getGraphicalEditor() {
		return graphicalEditor;
	}
	
	//NOTE: Need to expose this for external adapter, which cannot be refactored due to type check and the limits of type erasure in generics.
	@Override
	public void refreshVisuals() {
		super.refreshVisuals();
	}
	
	public DEFeatureModelEditPart getFeatureModelEditPart() {
		if (this instanceof DEFeatureModelEditPart) {
			return (DEFeatureModelEditPart) this;
		}
		
		EditPart parent = getParent();
		
		if (parent instanceof DEAbstractEditPart) {
			DEAbstractEditPart parentAbstractEditPart = (DEAbstractEditPart) parent;
			
			return parentAbstractEditPart.getFeatureModelEditPart();
		}
		
		return null;
	}
}
