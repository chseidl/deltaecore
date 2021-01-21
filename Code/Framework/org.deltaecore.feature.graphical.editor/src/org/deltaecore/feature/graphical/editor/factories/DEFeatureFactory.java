package org.deltaecore.feature.graphical.editor.factories;

import org.deltaecore.feature.DEFeature;
import org.eclipse.gef.requests.CreationFactory;

public class DEFeatureFactory implements CreationFactory {
	private static final String defaultName = "NewFeature";
	
	@Override
	public DEFeature getNewObject() {
		DEFeature feature = org.deltaecore.feature.DEFeatureFactory.eINSTANCE.createDEFeature();
		
		feature.setName(defaultName);
		feature.setMinCardinality(0);
		feature.setMaxCardinality(1);
		
		return feature;
	}

	@Override
	public Object getObjectType() {
		return DEFeature.class;
	}

}
