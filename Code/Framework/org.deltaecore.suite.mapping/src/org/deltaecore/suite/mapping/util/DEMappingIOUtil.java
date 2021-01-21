package org.deltaecore.suite.mapping.util;

import org.deltaecore.suite.mapping.DEMappingModel;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.emf.ecore.EObject;

public class DEMappingIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"demapping", "mapping"};
	
	public static DEMappingModel loadAccompanyingMappingModel(EObject elementFromSourceResource) {
		return doLoadAccompanyingModel(elementFromSourceResource, FILE_EXTENSIONS);
	}
	
	public static String getCurrentFileExtension() {
		return doGetCurrentFileExtension(FILE_EXTENSIONS);
	}
	
	public static boolean isDeprecatedFileExtension(EObject modelElement) {
		return doIsDeprecatedFileExtension(modelElement, FILE_EXTENSIONS);
	}
}
