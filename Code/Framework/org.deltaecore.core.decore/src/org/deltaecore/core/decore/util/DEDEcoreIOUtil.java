package org.deltaecore.core.decore.util;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEDEcoreIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"decore"};
	
	public static String getDefaultFileExtension() {
		return FILE_EXTENSIONS[0];
	}
	
	public static boolean isDeltaModuleFile(IFile file) {
		return doIsFile(file, FILE_EXTENSIONS);
	}
	
	public static EObject loadModelFromRelativeFilePath(DERelativeFilePath relativeFilePath) {
		Resource resource = relativeFilePath.eResource();
		ResourceSet resourceSet = resource != null ? resource.getResourceSet() : new ResourceSetImpl();
		
		return loadModelFromRelativeFilePath(relativeFilePath, resourceSet);
	}
	
	public static EObject loadModelFromRelativeFilePath(DERelativeFilePath relativeFilePath, ResourceSet resourceSet) {
		IFile file = DEDEcoreResolverUtil.getFileFromRelativeFilePath(relativeFilePath);
		
		if (!file.exists()) {
			return null;
		}
		
		return EcoreIOUtil.loadModel(file, resourceSet);
	}
	
	//This is meant as the single point for loading a delta so that possibly necessary customizations of the
	//loading procedure can be implemented in this single method. 
	public static DEDelta loadDeltaFromRelativeFilePath(DERelativeFilePath relativeFilePath, ResourceSet resourceSet) {
		IFile file = DEDEcoreResolverUtil.getFileFromRelativeFilePath(relativeFilePath);
		
		if (!file.exists()) {
			return null;
		}
		
		return (DEDelta) EcoreIOUtil.loadModel(file, resourceSet);
	}
	
	public static Resource createResourceFromRelativeFilePath(DERelativeFilePath relativeFilePath, ResourceSet resourceSet, boolean allowOverride) {
		IFile file = DEDEcoreResolverUtil.getFileFromRelativeFilePath(relativeFilePath);
		return EcoreIOUtil.createResource(file, resourceSet, allowOverride);
	}
	
	public static Resource getResourceFromRelativeFilePath(DERelativeFilePath relativeFilePath, ResourceSet resourceSet, boolean loadOnDemand) {
		IFile file = DEDEcoreResolverUtil.getFileFromRelativeFilePath(relativeFilePath);
		
		if (!file.exists()) {
			return null;
		}
		
		return EcoreIOUtil.getResource(file, resourceSet, loadOnDemand);
	}
}
