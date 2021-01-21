package org.deltaecore.feature.graphical.configurator.factories;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.factories.DEEditPartFactory;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.deltaecore.feature.graphical.configurator.editparts.DEConfiguratorFeatureEditPart;
import org.deltaecore.feature.graphical.configurator.editparts.DEConfiguratorFeatureModelEditPart;
import org.deltaecore.feature.graphical.configurator.editparts.DEConfiguratorVersionEditPart;
import org.eclipse.gef.EditPart;

public class DEConfiguratorEditPartFactory extends DEEditPartFactory {

	public DEConfiguratorEditPartFactory(DEConfiguratorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	protected EditPart doCreateEditPart(Object model) {
		DEConfiguratorGraphicalEditor graphicalEditor = getGraphicalEditor();
		
		if (model instanceof DEFeatureModel) {
			return new DEConfiguratorFeatureModelEditPart(graphicalEditor);
		}
		
		if (model instanceof DEFeature) {
			return new DEConfiguratorFeatureEditPart(graphicalEditor);
		}
		
		if (model instanceof DEVersion) {
			return new DEConfiguratorVersionEditPart(graphicalEditor);
		}
		
		return super.doCreateEditPart(model);
	}

	@Override
	protected DEConfiguratorGraphicalEditor getGraphicalEditor() {
		return (DEConfiguratorGraphicalEditor) super.getGraphicalEditor();
	}
}
