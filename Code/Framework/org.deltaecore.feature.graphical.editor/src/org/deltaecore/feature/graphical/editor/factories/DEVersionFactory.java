package org.deltaecore.feature.graphical.editor.factories;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.eclipse.gef.requests.CreationFactory;

public class DEVersionFactory implements CreationFactory {
	private static final String defaultNumber = "NewVersion";
	
	@Override
	public DEVersion getNewObject() {
		DEVersion feature = org.deltaecore.feature.DEFeatureFactory.eINSTANCE.createDEVersion();
		
		feature.setNumber(defaultNumber);
		
		return feature;
	}

	@Override
	public Object getObjectType() {
		return DEFeature.class;
	}

}
