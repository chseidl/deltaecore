package org.deltaecore.feature.graphical.configurator.editparts;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.deltaecore.feature.graphical.configurator.figures.DEConfiguratorFeatureModelFigure;
import org.eclipse.draw2d.IFigure;

public class DEConfiguratorFeatureModelEditPart extends DEFeatureModelEditPart {
	public DEConfiguratorFeatureModelEditPart(DEConfiguratorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected IFigure createFigure() {
		DEFeatureModel featureModel = getModel();
		DEConfiguratorGraphicalEditor graphicalEditor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
		return new DEConfiguratorFeatureModelFigure(featureModel, graphicalEditor);
	}
}
