package org.deltaecore.util;

import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.eclipse.ui.SelectionUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;
import de.christophseidl.util.ecore.EcoreResolverUtil;

public abstract class DEIOUtil {
	protected static String doGetCurrentFileExtension(String[] fileExtensions) {
		if (fileExtensions == null || fileExtensions.length == 0) {
			return null;
		}
		
		return fileExtensions[0];
	}
	
	protected static boolean doIsDeprecatedFileExtension(EObject modelElement, String[] fileExtensions) {
		if (modelElement == null || fileExtensions == null) {
			return false;
		}
		
		if (fileExtensions.length > 1) {
			String[] deprecatedFileExtensions = Arrays.copyOfRange(fileExtensions, 1, fileExtensions.length);
			IFile file = EcoreResolverUtil.resolveRelativeFileFromEObject(modelElement);
			
			if (DEIOUtil.doIsFile(file, deprecatedFileExtensions)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * File handle operation. File does not have to exist.
	 */
	protected static boolean doIsFile(IFile file, String[] fileExtensions) {
		//NOTE: Explicitly no check for existence of file.
		if (file == null || fileExtensions == null) {
			return false;
		}
		
		for (String fileExtension : fileExtensions) {
			String actualFileExtension = file.getFileExtension();
			
			if (actualFileExtension != null && actualFileExtension.equalsIgnoreCase(fileExtension)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected static IFile doGetFirstFileInSameProject(EObject elementFromSourceResource, String[] fileExtensions) {
		IProject project = EcoreResolverUtil.resolveProjectFromEObject(elementFromSourceResource);
		return doGetFirstFileInProject(project, fileExtensions);
	}
	
	protected static IFile doGetFirstFileInProject(IProject project, String[] fileExtensions) {
		if (fileExtensions == null) {
			return null;
		}
		
		for (String fileExtension : fileExtensions) {
			IFile selectedFile = ResourceUtil.findFirstFileWithExtension(project, fileExtension);
		
			if (selectedFile != null && selectedFile.exists()) {
				return selectedFile;
			}
		}
		
		return null;
	}
	
	protected static IFile doGetFirstActiveFile(String[] fileExtensions) {
		if (fileExtensions == null) {
			return null;
		}
		
		try {
			for (String fileExtension : fileExtensions) {
				IFile selectedFile = SelectionUtil.getFirstActiveIFileWithExtension(fileExtension);
			
				if (selectedFile != null && selectedFile.exists()) {
					return selectedFile;
				}
			}
		} catch(NoClassDefFoundError e) {
			//NOTE: This may happen if the optional dependency to the SelectioUtil cannot be satisfied,
			//which in turn depends on UI functionality that may not be present in headless mode (e.g., shell).
			throw new UnsupportedOperationException("Method not supported in GUI-less mode.");
		}
		
		return null;
	}
	
	//Public for shell script interpreter, refactor?
	public static <T extends EObject> T doLoadAccompanyingModel(EObject elementFromSourceResource, String[] fileExtensions) {
		if (elementFromSourceResource == null) {
			return null;
		}
		
		IFile accompanyingFile = doGetAccompanyingFile(elementFromSourceResource, fileExtensions);
//		String currentFileExtension = doGetCurrentFileExtension(fileExtensions);
		
		if (accompanyingFile != null && accompanyingFile.exists()) {
			Resource resource = elementFromSourceResource.eResource();
			ResourceSet resourceSet = resource == null ? new ResourceSetImpl() : resource.getResourceSet();
			
			return EcoreIOUtil.loadModel(accompanyingFile, resourceSet);
			//NOTE: Load with the resource factory of the current file extension.
//			return EcoreIOUtil.loadModel(accompanyingFile, currentFileExtension);
		}
		
		return null;
	}
	
	protected static IFile doGetAccompanyingFile(EObject elementFromSourceResource, String[] fileExtensions) {
		if (elementFromSourceResource == null || fileExtensions == null) {
			return null;
		}
		
		IFile sourceFile = EcoreResolverUtil.resolveRelativeFileFromEObject(elementFromSourceResource);
		
		if (sourceFile == null) {
			return null;
		}

		for (String fileExtension : fileExtensions) {
			IFile accompanyingFile = ResourceUtil.deriveFile(sourceFile, fileExtension);
		
			if (accompanyingFile != null && accompanyingFile.exists()) {
				return accompanyingFile;
			}
		}
		
		return null;
	}
}
