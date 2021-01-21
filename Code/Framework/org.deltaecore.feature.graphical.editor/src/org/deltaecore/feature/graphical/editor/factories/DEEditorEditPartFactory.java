package org.deltaecore.feature.graphical.editor.factories;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.factories.DEEditPartFactory;
import org.deltaecore.feature.graphical.editor.editor.DEEditorGraphicalEditor;
import org.deltaecore.feature.graphical.editor.editparts.DEEditorFeatureEditPart;
import org.deltaecore.feature.graphical.editor.editparts.DEEditorFeatureModelEditPart;
import org.deltaecore.feature.graphical.editor.editparts.DEEditorGroupEditPart;
import org.deltaecore.feature.graphical.editor.editparts.DEEditorVersionEditPart;
import org.eclipse.gef.EditPart;

public class DEEditorEditPartFactory extends DEEditPartFactory {

	public DEEditorEditPartFactory(DEEditorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	protected EditPart doCreateEditPart(Object model) {
		DEEditorGraphicalEditor graphicalEditor = getGraphicalEditor();
		
		if (model instanceof DEFeatureModel) {
			return new DEEditorFeatureModelEditPart(graphicalEditor);
		}
		
		if (model instanceof DEFeature) {
			return new DEEditorFeatureEditPart(graphicalEditor);
		}
		
		if (model instanceof DEGroup) {
			return new DEEditorGroupEditPart(graphicalEditor);
		}
		
		if (model instanceof DEVersion) {
			return new DEEditorVersionEditPart(graphicalEditor);
		}
		
		return super.doCreateEditPart(model);
	}

	@Override
	protected DEEditorGraphicalEditor getGraphicalEditor() {
		return (DEEditorGraphicalEditor) super.getGraphicalEditor();
	}
}
