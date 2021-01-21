package org.deltaecore.feature.configuration.impl.custom;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.impl.DEVersionSelectionImpl;

public class DEVersionSelectionImplCustom extends DEVersionSelectionImpl {

	@Override
	public DEFeature getFeature() {
		DEFeature feature = super.getFeature();
		
		if (feature == null) {
			DEVersion version = getVersion();
			
			if (version != null) {
				return version.getFeature();
			}
		}
		
		return feature;
	}

}
