package org.deltaecore.feature.constraint.util;

import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;

public class DEConstraintIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"deconstraints", "constraints"};
	
	public static String getDefaultFileExtension() {
		return FILE_EXTENSIONS[0];
	}
	
	public static IFile getAccompanyingConstraintFile(EObject elementInSourceResource) {
		return doGetAccompanyingFile(elementInSourceResource, FILE_EXTENSIONS);
	}
	
	public static DEConstraintModel loadAccompanyingConstraintModel(EObject elementInSourceResource) {
		return doLoadAccompanyingModel(elementInSourceResource, FILE_EXTENSIONS);
	}
	
	public static String getCurrentFileExtension() {
		return doGetCurrentFileExtension(FILE_EXTENSIONS);
	}
	
	public static boolean isDeprecatedFileExtension(EObject modelElement) {
		return doIsDeprecatedFileExtension(modelElement, FILE_EXTENSIONS);
	}
}
