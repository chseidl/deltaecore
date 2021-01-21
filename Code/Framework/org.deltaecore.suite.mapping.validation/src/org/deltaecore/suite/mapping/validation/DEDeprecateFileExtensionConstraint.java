package org.deltaecore.suite.mapping.validation;

import org.deltaecore.suite.mapping.DEMappingModel;
import org.deltaecore.suite.mapping.util.DEMappingIOUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEDeprecateFileExtensionConstraint extends AbstractConstraint<DEMappingModel> {

	@Override
	protected IStatus doValidate(DEMappingModel mappingModel) {
		if (DEMappingIOUtil.isDeprecatedFileExtension(mappingModel)) {
			String message = "The file extension is deprecated. Use \"" + DEMappingIOUtil.getCurrentFileExtension() + "\" instead.";
			return createWarningStatus(message, mappingModel); 
		}
		
		return createSuccessStatus();
	}
}
