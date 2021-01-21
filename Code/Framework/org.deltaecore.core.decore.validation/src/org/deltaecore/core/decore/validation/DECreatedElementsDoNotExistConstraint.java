package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DECreatedElementsDoNotExistConstraint extends AbstractConstraint<DEDeltaBlock> {
	@Override
	protected IStatus doValidate(DEDeltaBlock block) {
		List<DERelativeFilePath> relativeFilePaths = block.getCreatedElementRelativeFilePaths();
		return checkRelativeFilePathsDoNotExist(relativeFilePaths);
	}
	
	protected IStatus checkRelativeFilePathsDoNotExist(List<DERelativeFilePath> relativeFilePaths) {
		for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
			IFile file = DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeFilePath);
			
			if (file != null && file.exists()) {
				String message = "File already exists at specified location.";
				return createWarningStatus(message, relativeFilePath);
			}
		}
		
		return createSuccessStatus();
	}
}
