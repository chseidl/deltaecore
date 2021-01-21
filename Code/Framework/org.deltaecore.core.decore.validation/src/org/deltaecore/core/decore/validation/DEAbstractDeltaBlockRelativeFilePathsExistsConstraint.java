package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public abstract class DEAbstractDeltaBlockRelativeFilePathsExistsConstraint extends AbstractConstraint<DEDeltaBlock> {
	protected IStatus checkRelativeFilePathsExist(List<DERelativeFilePath> relativeFilePaths) {
		for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
			if (!doesRelativeFilePathExist(relativeFilePath)) {
				return createFileDoesNotExistErrorStatus(relativeFilePath);
			}
		}
		
		return createSuccessStatus();
	}
	
	protected IStatus createFileDoesNotExistErrorStatus(DERelativeFilePath relativeFilePath) {
		String message = "File does not exist at specified location.";
		return createErrorStatus(message, relativeFilePath);
	}
	
	protected boolean doesRelativeFilePathExist(DERelativeFilePath relativeFilePath) {
		IFile file = DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeFilePath);
		
		if (file == null || !file.exists()) {
			return false;
		}
		
		return true;
	}
}
