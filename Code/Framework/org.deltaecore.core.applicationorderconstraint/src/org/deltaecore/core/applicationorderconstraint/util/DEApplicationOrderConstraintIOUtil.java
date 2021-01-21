package org.deltaecore.core.applicationorderconstraint.util;

import java.util.List;

import org.deltaecore.core.applicationorderconstraint.DEApplicationOrderConstraintModel;
import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEApplicationOrderConstraintIOUtil extends DEIOUtil {
	public static final String[] FILE_EXTENSIONS = {"deapplicationorderconstraints", "aoc"};
	
	public static DEApplicationOrderConstraintModel loadApplicationOrderConstraintModel(List<DEDelta> deltas) {
		if (deltas.isEmpty()) {
			return null;
		}
		
		DEDelta delta = deltas.get(0);
		return loadApplicationOrderConstraintModel(delta);
	}
	
	//TODO: This should be refactored some time (integrate into parent?) 
	public static DEApplicationOrderConstraintModel loadApplicationOrderConstraintModel(DEDelta delta) {
		if (delta == null) {
			return null;
		}
		
		//Find the first file encountered with extension *.aoc in the project root.
		IFile applicationOrderConstraintFile = doGetFirstFileInSameProject(delta, FILE_EXTENSIONS);
		
		if (applicationOrderConstraintFile == null) {
			return null;
		}
		
		Resource resource = delta.eResource();
		ResourceSet resourceSet = resource.getResourceSet();
		
		//NOTE: Circumvent bug with caching AOC file by unloading any previous instance.
        URI uri = EcoreIOUtil.createURIFromFile(applicationOrderConstraintFile);
        Resource existingResource = resourceSet.getResource(uri, false);
        
        if (existingResource != null) {
            existingResource.unload();
        }
        
		DEApplicationOrderConstraintModel aocModel = EcoreIOUtil.loadModel(applicationOrderConstraintFile, resourceSet);
		
		return aocModel;
	}
	
	public static String getCurrentFileExtension() {
		return doGetCurrentFileExtension(FILE_EXTENSIONS);
	}
	
	public static boolean isDeprecatedFileExtension(EObject modelElement) {
		return doIsDeprecatedFileExtension(modelElement, FILE_EXTENSIONS);
	}
}
