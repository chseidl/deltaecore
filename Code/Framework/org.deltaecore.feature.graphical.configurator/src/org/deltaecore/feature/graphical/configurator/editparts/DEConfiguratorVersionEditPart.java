package org.deltaecore.feature.graphical.configurator.editparts;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.graphical.base.editparts.DEVersionEditPart;
import org.deltaecore.feature.graphical.configurator.editor.DEConfiguratorGraphicalEditor;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public class DEConfiguratorVersionEditPart extends DEVersionEditPart {
	public DEConfiguratorVersionEditPart(DEConfiguratorGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	public void performRequest(Request request) {
		//React to double click
		if (request.getType() == RequestConstants.REQ_OPEN) {
			DEVersion version = getModel();
			
			DEConfiguratorGraphicalEditor editor = (DEConfiguratorGraphicalEditor) getGraphicalEditor();
			DEConfiguration configuration = editor.getSelectedConfiguration();
			
			if (DEConfigurationUtil.configurationContains(configuration, version)) {
				DEConfigurationUtil.removeVersionFromConfiguration(version, configuration);
			} else {
				DEConfigurationUtil.addVersionToConfiguration(version, configuration, true);
			}
		}
	}
}
