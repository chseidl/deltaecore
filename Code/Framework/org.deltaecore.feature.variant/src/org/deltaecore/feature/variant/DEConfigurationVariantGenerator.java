package org.deltaecore.feature.variant;

import java.util.List;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.resource.Resource;

import de.christophseidl.util.ecore.EcoreIOUtil;

public abstract class DEConfigurationVariantGenerator implements DEVariantGenerator {

	@Override
	public void createAndSaveVariantFromConfiguration(DEConfiguration configuration, IFolder variantFolder) throws Exception {
		List<Resource> affectedResources = createVariantFromConfiguration(configuration);
		
		//Write all affected resources to disk
		EcoreIOUtil.saveResourcesAs(affectedResources, variantFolder);
	}
	
	@Override
	public abstract List<Resource> createVariantFromConfiguration(DEConfiguration configuration) throws Exception;
}
