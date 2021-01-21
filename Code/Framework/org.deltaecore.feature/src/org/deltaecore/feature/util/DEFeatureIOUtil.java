package org.deltaecore.feature.util;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEFeatureIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"defeaturemodel", "feature"};
	
	public static String getDefaultFileExtension() {
		return FILE_EXTENSIONS[0];
	}
	
	public static DEFeatureModel loadAccompanyingFeatureModel(EObject elementInSourceResource) {
		return doLoadAccompanyingModel(elementInSourceResource, FILE_EXTENSIONS);
	}
	
	public static DEFeatureModel loadFeatureModelFromProjectRoot(EObject elementFromProject) {
		
		for (String fileExtension : FILE_EXTENSIONS) {
			DEFeatureModel featureModel = EcoreIOUtil.loadModelFromProjectRoot(fileExtension, elementFromProject);
			
			if (featureModel != null) {
				return featureModel;
			}
		}
		
		return null;
	}
	
	/**
	 * Selected or opened in editor
	 */
	public static IFile getFirstActiveFeatureModelFile() {
		return doGetFirstActiveFile(FILE_EXTENSIONS);
	}
}
