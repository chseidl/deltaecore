package org.deltaecore.core.decoredialect.util;

import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;

public class DEDEcoreDialectIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"decoredialect"};
	
	/**
	 * Selected or opened in editor
	 */
	public static IFile getFirstActiveDeltaDialectFile() {
		return doGetFirstActiveFile(FILE_EXTENSIONS);
	}
	
	public static String getCurrentFileExtension() {
		return doGetCurrentFileExtension(FILE_EXTENSIONS);
	}
	
	public static boolean isDeprecatedFileExtension(EObject modelElement) {
		return doIsDeprecatedFileExtension(modelElement, FILE_EXTENSIONS);
	}
}
