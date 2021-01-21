package org.deltaecore.feature.graphical.base.factories;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureEditPart;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.deltaecore.feature.graphical.base.editparts.DEGroupEditPart;
import org.deltaecore.feature.graphical.base.editparts.DEVersionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class DEEditPartFactory implements EditPartFactory {
	private DEGraphicalEditor graphicalEditor;
	
	public DEEditPartFactory(DEGraphicalEditor graphicalEditor) {
		this.graphicalEditor = graphicalEditor;
	}
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = doCreateEditPart(model);

		if (part != null) {
			part.setModel(model);
		}

		return part;
	}

	protected EditPart doCreateEditPart(Object model) {
		if (model instanceof DEFeatureModel) {
			return new DEFeatureModelEditPart(graphicalEditor);
		}
		
		if (model instanceof DEFeature) {
			return new DEFeatureEditPart(graphicalEditor);
		}
		
		if (model instanceof DEGroup) {
			return new DEGroupEditPart(graphicalEditor);
		}
		
		if (model instanceof DEVersion) {
			return new DEVersionEditPart(graphicalEditor);
		}
		
		return null;
	}

	protected DEGraphicalEditor getGraphicalEditor() {
		return graphicalEditor;
	}
}
