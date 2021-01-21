package org.deltaecore.integration.ie.feature.eclipse;

import org.deltaecore.integration.ie.feature.DEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.featureide.DEFeatureIDEFeatureModelImporter;

public class ImportFeatureIDEFeatureModelCommandHandler extends AbstractImportFeatureModelCommandHandler {
	public ImportFeatureIDEFeatureModelCommandHandler() {
		super("xml");
	}

	@Override
	protected DEFeatureModelImporter<?> createFeatureModelImporter() {
		return new DEFeatureIDEFeatureModelImporter();
	}
}
