package org.deltaecore.core.applicationorderconstraint.validation;

import org.deltaecore.core.applicationorderconstraint.DEConstrainedGroup;
import org.deltaecore.core.decore.util.DEDEcoreIOUtil;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.decorebase.validation.DERelativeFilePathWellFormed;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public class DERelativeDeltaFilePathExists extends DERelativeFilePathWellFormed {

	@Override
	protected IStatus doValidate(DERelativeFilePath relativeDeltaPath) {
		IStatus status = super.doValidate(relativeDeltaPath);
		
		EObject eContainer = relativeDeltaPath.eContainer();
		
		//This is context dependent
		if (status.isOK() && eContainer instanceof DEConstrainedGroup) {
			IFile file = DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeDeltaPath);
			
			if (!DEDEcoreIOUtil.isDeltaModuleFile(file) || !file.exists()) {
				String message = "File at specified location is not a DeltaEcore delta module.";
				return createErrorStatus(message, relativeDeltaPath);
			}
			
			return createSuccessStatus();
		}
		
		return status;
	}
}
