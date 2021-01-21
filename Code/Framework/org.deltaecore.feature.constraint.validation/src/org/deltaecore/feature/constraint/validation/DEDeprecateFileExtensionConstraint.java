package org.deltaecore.feature.constraint.validation;

import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEDeprecateFileExtensionConstraint extends AbstractConstraint<DEConstraintModel> {

	@Override
	protected IStatus doValidate(DEConstraintModel constraintModel) {
		if (DEConstraintIOUtil.isDeprecatedFileExtension(constraintModel)) {
			String message = "The file extension is deprecated. Use \"" + DEConstraintIOUtil.getCurrentFileExtension() + "\" instead.";
			return createWarningStatus(message, constraintModel); 
		}
		
		return createSuccessStatus();
	}
}
