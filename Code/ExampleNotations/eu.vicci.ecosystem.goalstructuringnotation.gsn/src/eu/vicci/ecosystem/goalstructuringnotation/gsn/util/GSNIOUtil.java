package eu.vicci.ecosystem.goalstructuringnotation.gsn.util;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNPackage;

public class GSNIOUtil {
	public static GSNModel loadGSNModel(File file) {
		//NOTE NOTE NOTE
		//Need to load from ABSOLUTE PATH - otherwise resolving of proxies in other resources will fail!
		
		GSNPackage.eINSTANCE.eClass();
		
	    Map<String, Object> extensionToFactoryMap = Registry.INSTANCE.getExtensionToFactoryMap();
	    
	  	extensionToFactoryMap.put("gsn", new XMIResourceFactoryImpl());
	  		
	    ResourceSet resourceSet = new ResourceSetImpl();

	    Resource resource = resourceSet.getResource(URI.createFileURI(file.toString()), true);
	    
	    try {
	    	resource.load(Collections.EMPTY_MAP);
	    	
	    	List<EObject> contents = resource.getContents();
	    	
	    	if (contents == null || contents.isEmpty()) {
	    		return null;
	    	}
	    	
//	    	EcoreUtil.resolveAll(resourceSet);
	    	
	    	EObject object = contents.get(0);
	    	
	    	if (object instanceof GSNModel) {
	    		return (GSNModel) object;
	    	}
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	    
	    System.err.println("Failed to load CFD from \"" + file + "\".");
	    return null;
	}
	
	public static boolean saveComponentFaultDiagramAs(GSNModel model, File file) {
		GSNPackage.eINSTANCE.eClass();
		
	    Map<String, Object> extensionToFactoryMap = Registry.INSTANCE.getExtensionToFactoryMap();
	    extensionToFactoryMap.put("gsn", new XMIResourceFactoryImpl());
	    
	    ResourceSet resourceSet = new ResourceSetImpl();
	    Resource resource = resourceSet.createResource(URI.createFileURI(file.toString()));
	    resource.getContents().add(model);

		return doSaveComponentFaultDiagram(model, resource);
	}
	
	public static boolean saveGSNModel(GSNModel model) {
	    Resource resource = model.eResource();
	    return doSaveComponentFaultDiagram(model, resource);
	}
	
	private static boolean doSaveComponentFaultDiagram(GSNModel model, Resource resource) {
		try {
			//TODO: faulty for save as!
//			resource.getContents().
			resource.save(Collections.EMPTY_MAP);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.err.println("Failed to save CFD.");
		return false;
	}
}
