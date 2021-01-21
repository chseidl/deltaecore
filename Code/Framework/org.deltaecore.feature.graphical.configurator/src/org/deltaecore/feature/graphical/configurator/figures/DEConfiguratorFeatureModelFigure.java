package org.deltaecore.feature.graphical.configurator.figures;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.graphical.base.figures.DEFeatureModelFigure;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class DEConfiguratorFeatureModelFigure extends DEFeatureModelFigure {

	public DEConfiguratorFeatureModelFigure(DEFeatureModel featureModel, DEConfiguratorGraphicalEditor graphicalEditor) {
		super(featureModel, graphicalEditor);
	}

	@Override
	protected void drawFeatureMarks(DEFeature feature, Graphics graphics) {
		DEConfiguratorGraphicalEditor graphicalEditor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
		
		DEConfiguration selectedConfiguration = graphicalEditor.getSelectedConfiguration();
		DEConfiguration runningConfiguration = graphicalEditor.getRunningConfiguration();
	
		Rectangle featureMarkRectangle = getFeatureMarkRectangle(feature);
		
		if (DEConfigurationUtil.configurationContains(selectedConfiguration, feature)) {
			DEDrawingUtil.drawSelection(graphics, featureMarkRectangle, this, false);
		} else if (DEConfigurationUtil.configurationContains(runningConfiguration, feature)) {
			DEDrawingUtil.drawSelection(graphics, featureMarkRectangle, this, true);
		}
		
		super.drawFeatureMarks(feature, graphics);
	}
}
