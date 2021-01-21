package org.deltaecore.feature.delta;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureFactory;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class FeatureDeltaDialectInterpreter extends FeatureAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretCreateFeature(DEModelWriter modelWriter, String name, Integer minCardinality, DEGroup parentGroup, String initialVersionNumber) {
		DEFeature feature = DEFeatureFactory.eINSTANCE.createDEFeature();
		feature.setName(name);
		feature.setMinCardinality(minCardinality);
		feature.setMaxCardinality(1);
		
		createVersion(initialVersionNumber, feature);
		
		parentGroup.getFeatures().add(feature);
		
		return true;
	}

	@Override
	protected boolean interpretCreateGroup(DEModelWriter modelWriter, Integer minCardinality, Integer maxCardinality, DEFeature parentFeature) {
		DEGroup group = DEFeatureFactory.eINSTANCE.createDEGroup();
		group.setMinCardinality(minCardinality);
		group.setMaxCardinality(maxCardinality);
		
		parentFeature.getGroups().add(group);
		
		return true;
	}

	@Override
	protected boolean interpretCreateVersion(DEModelWriter modelWriter, String number, DEVersion predecessorVersion) {
		DEFeature feature = predecessorVersion.getFeature();
		DEVersion version = createVersion(number, feature);
		
		version.setSupersededVersion(predecessorVersion);
			
		return true;
	}
	
	private static DEVersion createVersion(String number, DEFeature feature) {
		DEVersion version = DEFeatureFactory.eINSTANCE.createDEVersion();
		version.setNumber(number);
		feature.getVersions().add(version);
		
		return version;
	}
}