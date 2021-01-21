package org.deltaecore.suite.variant;

import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.variant.DEVariantCreator;
import org.deltaecore.core.variant.requirements.DEDeltaAbstractDeltasInInputException;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.variant.DEConfigurationVariantGenerator;
import org.deltaecore.suite.variant.util.DEConfigurationEvaluator;
import org.eclipse.emf.ecore.resource.Resource;

public class DEConfigurationDeltaModuleVariantGenerator extends DEConfigurationVariantGenerator {
	private DEVariantCreator deltaVariantCreator;
	
	public DEConfigurationDeltaModuleVariantGenerator() {
		deltaVariantCreator = createVariantCreator();
	}
	
	protected DEVariantCreator createVariantCreator() {
		return new DEVariantCreator();
	}
	
	@Override
	public List<Resource> createVariantFromConfiguration(DEConfiguration configuration) throws DEDeltaAbstractDeltasInInputException, DEDeltaRequirementsCycleException {
		List<DEDelta> deltasList = DEConfigurationEvaluator.getDeltasListForConfiguration(configuration);
		return deltaVariantCreator.createVariantFromDeltas(deltasList);
	}
	
//	protected List<Resource> createVariantFromConfiguration(DEConfiguration configuration, DEMappingModel mapping) throws DEDeltaAbstractDeltasInInputException, DEDeltaRequirementsCycleException {
//	List<DEDelta> deltasList = DEConfigurationEvaluator.getDeltasListForConfiguration(configuration, mapping);
//	return deltaVariantCreator.createVariantFromDeltas(deltasList);
//}
//
//protected void createAndSaveVariantFromConfiguration(DEConfiguration configuration, DEMappingModel mapping, IFolder variantFolder) throws DEDeltaAbstractDeltasInInputException, DEDeltaRequirementsCycleException {
//	List<DEDelta> deltasList = DEConfigurationEvaluator.getDeltasListForConfiguration(configuration, mapping);
//	deltaVariantCreator.createAndSaveVariantFromDeltas(deltasList, variantFolder);
//}
}
