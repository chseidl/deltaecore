package org.deltaecore.feature.variant;

import java.util.List;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.resource.Resource;

public interface DEVariantGenerator {
	public void createAndSaveVariantFromConfiguration(DEConfiguration configuration, IFolder variantFolder) throws Exception;
	public List<Resource> createVariantFromConfiguration(DEConfiguration configuration) throws Exception;
}