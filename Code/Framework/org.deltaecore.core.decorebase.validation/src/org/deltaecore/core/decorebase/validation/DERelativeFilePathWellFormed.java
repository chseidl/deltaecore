package org.deltaecore.core.decorebase.validation;

import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DERelativeFilePathWellFormed extends AbstractConstraint<DERelativeFilePath> {

	@Override
	protected IStatus doValidate(DERelativeFilePath relativeFilePath) {
		try {
			DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeFilePath);
		} catch (Exception e) {
			return createErrorStatus("The file path is not valid.", relativeFilePath);
		}
		
		return createSuccessStatus();
	}
}
