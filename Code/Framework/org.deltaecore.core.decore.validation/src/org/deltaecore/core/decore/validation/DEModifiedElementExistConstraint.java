package org.deltaecore.core.decore.validation;

import java.util.Collections;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.variant.requirements.DEDeltaRequirementsCompleter;
import org.deltaecore.debug.DEDebug;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

public class DEModifiedElementExistConstraint extends DEAbstractDeltaBlockRelativeFilePathsExistsConstraint {
	private static DEDeltaRequirementsCompleter deltaCompleter = new DEDeltaRequirementsCompleter();
	
	@Override
	protected IStatus doValidate(DEDeltaBlock block) {
		try {
		DEDelta delta = block.getDelta();
		List<DEDelta> deltas = deltaCompleter.completeRequirements(Collections.singletonList(delta));
		
			List<DERelativeFilePath> relativeFilePaths = block.getModifiedElementRelativeFilePaths();
			
			for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
				
				if (!doesRelativeFilePathExist(relativeFilePath) && !isRelativeFilePathCreated(relativeFilePath, deltas)) {
					String message = "File does not exist at specified location and was not created by any previous delta module.";
					return createErrorStatus(message, relativeFilePath);
				}
				
				DEDebug.println("File \"" + relativeFilePath.getRawRelativeFilePath() + "\" exists or will be created.");
			}
		} catch (DEDeltaRequirementsCycleException e) {
			//TODO: This is nonsense
			createErrorStatus("Cycle in required deltas.", null);
		}

		return createSuccessStatus();
	}
	
	protected IStatus checkRelativeFilePathsCreated(List<DERelativeFilePath> relativeFilePaths) {
		for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
			IFile file = DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeFilePath);
			
			if (file == null || !file.exists()) {
				String message = "File does not exist at specified location.";
				return createErrorStatus(message, relativeFilePath);
			}
		}
		
		return createSuccessStatus();
	}

	protected boolean isRelativeFilePathCreated(DERelativeFilePath relativeFilePath, List<DEDelta> deltas) {
		IFile file = DEDEcoreBaseUtil.getFileFromRelativeFilePath(relativeFilePath);
		IPath fileLocation = file.getLocation();
		
		List<DERelativeFilePath> createdRelativeFilePaths = DEDEcoreResolverUtil.collectCreatedElementRelativeFilePaths(deltas);
		
		for (DERelativeFilePath createdRelativeFilePath : createdRelativeFilePaths) {
			IFile createdFile = DEDEcoreBaseUtil.getFileFromRelativeFilePath(createdRelativeFilePath);
			
			IPath createdFileLocation = createdFile.getLocation();
			
			if (fileLocation.equals(createdFileLocation)) {
				return true;
			}
		}
		
		return false;
	}
}
