package org.deltaecore.feature.configuration.validation;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationIOUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEDeprecateFileExtensionConstraint extends AbstractConstraint<DEConfiguration> {

	@Override
	protected IStatus doValidate(DEConfiguration configuration) {
		if (DEConfigurationIOUtil.isDeprecatedFileExtension(configuration)) {
			String message = "The file extension is deprecated. Use \"" + DEConfigurationIOUtil.getCurrentFileExtension() + "\" instead.";
			return createWarningStatus(message, configuration); 
		}
		
		return createSuccessStatus();
	}
}
