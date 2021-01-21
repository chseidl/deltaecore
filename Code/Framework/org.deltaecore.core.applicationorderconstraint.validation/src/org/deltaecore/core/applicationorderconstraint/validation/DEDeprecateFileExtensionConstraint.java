package org.deltaecore.core.applicationorderconstraint.validation;

import org.deltaecore.core.applicationorderconstraint.DEApplicationOrderConstraintModel;
import org.deltaecore.core.applicationorderconstraint.util.DEApplicationOrderConstraintIOUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEDeprecateFileExtensionConstraint extends AbstractConstraint<DEApplicationOrderConstraintModel> {

	@Override
	protected IStatus doValidate(DEApplicationOrderConstraintModel applicationOrderConstraintModel) {
		if (DEApplicationOrderConstraintIOUtil.isDeprecatedFileExtension(applicationOrderConstraintModel)) {
			String message = "The file extension is deprecated. Use \"" + DEApplicationOrderConstraintIOUtil.getCurrentFileExtension() + "\" instead.";
			return createWarningStatus(message, applicationOrderConstraintModel); 
		}
		
		return createSuccessStatus();
	}
}
