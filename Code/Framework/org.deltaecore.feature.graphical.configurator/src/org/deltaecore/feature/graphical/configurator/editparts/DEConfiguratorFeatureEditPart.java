package org.deltaecore.feature.graphical.configurator.editparts;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureEditPart;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.deltaecore.feature.graphical.configurator.figures.DEConfiguratorFeatureFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public class DEConfiguratorFeatureEditPart extends DEFeatureEditPart {
	public DEConfiguratorFeatureEditPart(DEConfiguratorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected IFigure createFigure() {
		DEFeature feature = getModel();
		DEConfiguratorGraphicalEditor graphicalEditor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
		return new DEConfiguratorFeatureFigure(feature, graphicalEditor);
	}
	
	@Override
	public void performRequest(Request request) {
		//React to double click
		if (request.getType() == RequestConstants.REQ_OPEN) {
			DEFeature feature = getModel();

			DEConfiguratorGraphicalEditor editor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
			DEConfiguration configuration = editor.getSelectedConfiguration();

			if (DEConfigurationUtil.configurationContains(configuration, feature)) {
				DEConfigurationUtil.removeFeatureFromConfiguration(feature, configuration);
			} else {
				DEConfigurationUtil.addFeatureToConfiguration(feature, configuration, true);
			}
		}
	}
}
