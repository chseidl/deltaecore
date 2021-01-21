package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.util.DEDEcoreIOUtil;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DERequireOnlyDeltaModulesConstraint extends AbstractConstraint<DEDeltaBlock> {

	@Override
	protected IStatus doValidate(DEDeltaBlock deltaBlock) {
		List<DERelativeFilePath> relativeFilePaths = deltaBlock.getRequiredElementRelativeFilePaths();
		
		for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
			IFile file = DEDEcoreResolverUtil.getFileFromRelativeFilePath(relativeFilePath);

			if (!DEDEcoreIOUtil.isDeltaModuleFile(file)) {
				//String message = "The use of resources other than delta modules in the \"requires\" block is deprecated and support will be removed in future versions of DeltaEcore. Use the \"modifies\" block instead (below the requires block).";
				String message = "The use of resources other than delta modules in the \"requires\" block is no longer supported. Use the \"modifies\" block instead (below the requires block).";
				return createErrorStatus(message, relativeFilePath);
			}
		}
		
		return createSuccessStatus(); 
	}
}
