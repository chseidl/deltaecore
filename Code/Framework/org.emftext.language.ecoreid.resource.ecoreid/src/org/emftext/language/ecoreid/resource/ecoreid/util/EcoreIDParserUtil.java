package org.emftext.language.ecoreid.resource.ecoreid.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emftext.language.ecoreid.EcoreID;
import org.emftext.language.ecoreid.EcoreIDList;
import org.emftext.language.ecoreid.resource.ecoreid.mopp.EcoreidResourceFactory;
import org.emftext.language.ecoreid.util.EcoreIDResolverUtil;

public class EcoreIDParserUtil {
	
	public static List<EModelElement> resolveEcoreIDList(String rawEcoreIDListString, EPackage ePackage) {
		EcoreIDList ecoreIDList = parseEcoreIDListString(rawEcoreIDListString);
		List<EModelElement> modelElements = new ArrayList<EModelElement>();
		
		if (ecoreIDList == null) {
//			System.err.println("Couldn't parse \"" + rawEcoreIDListString + "\".");
			return modelElements;
		}
		
		List<EcoreID> ecoreIDs = ecoreIDList.getEcoreIDs();
		
		for (EcoreID ecoreID : ecoreIDs) {
//			try {
				EModelElement eModelElement = EcoreIDResolverUtil.resolveEcoreID(ecoreID, ePackage);
				
				if (eModelElement != null) {
					modelElements.add(eModelElement);
				}
//			} catch(Exception e) {
////				System.err.println("Couldn't resolve \"" + ecoreID + "\".");
//			}
		}
		
		return modelElements;
	}
	
	public static EModelElement resolveEcoreIDString(String ecoreIDString, EPackage ePackage) {
		EcoreID ecoreID = parseEcoreIDString(ecoreIDString);
		return EcoreIDResolverUtil.resolveEcoreID(ecoreID, ePackage);
	}
	
	public static EcoreIDList parseEcoreIDListString(String ecoreIDListString) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI("Temp.ecoreid"));

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecoreid", new EcoreidResourceFactory());
		
		byte[] bytes = ecoreIDListString.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		
		try {
			resource.load(inputStream, Collections.EMPTY_MAP);
			
			List<EObject> contents = resource.getContents();
			
			if (!contents.isEmpty()) {
				EObject eObject = contents.get(0);
				
				if (eObject instanceof EcoreIDList) {
					return (EcoreIDList) eObject;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static EcoreID parseEcoreIDString(String ecoreIDString) {
		EcoreIDList ecoreIDList = parseEcoreIDListString(ecoreIDString);
		
		if (ecoreIDList == null) {
			return null;
		}
		
		List<EcoreID> ecoreIDs = ecoreIDList.getEcoreIDs();
		
		if (ecoreIDs.isEmpty()) {
			return null;
		}
		
		return ecoreIDs.get(0);
	}
}
