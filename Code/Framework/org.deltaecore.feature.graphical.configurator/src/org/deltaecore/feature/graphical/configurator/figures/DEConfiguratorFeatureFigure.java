package org.deltaecore.feature.graphical.configurator.figures;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class DEConfiguratorFeatureFigure extends DEFeatureFigure {

	public DEConfiguratorFeatureFigure(DEFeature feature, DEConfiguratorGraphicalEditor graphicalEditor) {
		super(feature, graphicalEditor);
	}

	@Override
	protected void paintVersionMarks(Graphics graphics) {
		DEFeature feature = getFeature();
		List<DEVersion> versions = feature.getVersions();

		DEConfiguratorGraphicalEditor graphicalEditor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
		DEConfiguration selectedConfiguration = graphicalEditor.getSelectedConfiguration();
		DEConfiguration runningConfiguration = graphicalEditor.getRunningConfiguration();
		
		for (DEVersion version : versions) {
			Rectangle versionMarkRectangle = getVersionMarkRectangle(version);
			
			if (DEConfigurationUtil.configurationContains(selectedConfiguration, version)) {
				DEDrawingUtil.drawSelection(graphics, versionMarkRectangle, this, false);
			} else if (DEConfigurationUtil.configurationContains(runningConfiguration, version)) {
				DEDrawingUtil.drawSelection(graphics, versionMarkRectangle, this, true);
			}
		}
		
		super.paintVersionMarks(graphics);
	}
}
