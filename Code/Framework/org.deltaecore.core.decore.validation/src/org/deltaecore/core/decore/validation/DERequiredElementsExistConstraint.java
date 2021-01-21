package org.deltaecore.core.decore.validation;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.eclipse.core.runtime.IStatus;

public class DERequiredElementsExistConstraint extends DEAbstractDeltaBlockRelativeFilePathsExistsConstraint {
	@Override
	protected IStatus doValidate(DEDeltaBlock block) {
		List<DERelativeFilePath> relativeFilePaths = block.getRequiredElementRelativeFilePaths();
		return checkRelativeFilePathsExist(relativeFilePaths);
	}
}
