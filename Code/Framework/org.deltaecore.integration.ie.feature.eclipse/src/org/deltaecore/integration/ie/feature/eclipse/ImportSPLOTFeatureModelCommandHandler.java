package org.deltaecore.integration.ie.feature.eclipse;

import org.deltaecore.integration.ie.feature.DEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.splot.DESPLOTFeatureModelImporter;

public class ImportSPLOTFeatureModelCommandHandler extends AbstractImportFeatureModelCommandHandler {
	public ImportSPLOTFeatureModelCommandHandler() {
		super("xml");
	}

	@Override
	protected DEFeatureModelImporter<?> createFeatureModelImporter() {
		return new DESPLOTFeatureModelImporter();
	}
}
