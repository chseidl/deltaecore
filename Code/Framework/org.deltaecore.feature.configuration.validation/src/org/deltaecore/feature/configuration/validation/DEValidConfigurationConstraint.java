package org.deltaecore.feature.configuration.validation;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationChecker;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEValidConfigurationConstraint extends AbstractConstraint<DEConfiguration> {

	@Override
	protected IStatus doValidate(DEConfiguration configuration) {
		DEConfigurationChecker configurationChecker = new DEConfigurationChecker();
		boolean isValid = configurationChecker.isConfigurationValid(configuration);
		
		if (!isValid) {
			String errorMessage = configurationChecker.getErrorMessage();
			return createErrorStatus("Configuration invalid: " + errorMessage, configuration);
		}
		
		return createSuccessStatus();
	}

}
