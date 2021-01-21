package org.deltaecore.feature.configuration.impl.custom;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.configuration.impl.DEConfigurationFactoryImpl;

public class DEConfigurationFactoryImplCustom extends DEConfigurationFactoryImpl {

	@Override
	public DEConfiguration createDEConfiguration() {
		return new DEConfigurationImplCustom();
	}

	@Override
	public DEVersionSelection createDEVersionSelection() {
		return new DEVersionSelectionImplCustom();
	}
}
