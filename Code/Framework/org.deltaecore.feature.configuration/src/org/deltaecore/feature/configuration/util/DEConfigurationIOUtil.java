package org.deltaecore.feature.configuration.util;

import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;

public class DEConfigurationIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"deconfiguration", "configuration"};
	
	/**
	 * Selected or opened in editor
	 */
	public static IFile getFirstActiveConfigurationFile() {
		return doGetFirstActiveFile(FILE_EXTENSIONS);
	}
	
	public static String getCurrentFileExtension() {
		return doGetCurrentFileExtension(FILE_EXTENSIONS);
	}
	
	public static boolean isDeprecatedFileExtension(EObject modelElement) {
		return doIsDeprecatedFileExtension(modelElement, FILE_EXTENSIONS);
	}
}
