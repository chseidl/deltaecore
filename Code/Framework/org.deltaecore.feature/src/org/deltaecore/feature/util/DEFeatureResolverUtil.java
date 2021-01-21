package org.deltaecore.feature.util;

import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.eclipse.emf.ecore.EObject;

public class DEFeatureResolverUtil {
	public static DEFeature resolveFeature(String identifier, DEFeatureModel featureModel) {
		if (identifier.startsWith("\"") && identifier.endsWith("\"")) {
			identifier = identifier.substring(1, identifier.length() - 1);
		}
		
		Iterator<EObject> iterator = featureModel.eAllContents();
		
		while(iterator.hasNext()) {
			EObject element = iterator.next();
			
			if (element instanceof DEFeature) {
				DEFeature feature = (DEFeature) element;
				String name = feature.getName();
				
				if (identifier.equals(name)) {
					return feature;
				}
			}
		}
		
		return null;
	}
	
	public static String deresolveFeature(DEFeature feature) {
		String identifier = feature.getName();

		//NOTE: Disabled as this causes problems with printing to concrete syntax where two sets of quotes are added.
//		//Standard TEXT token regular expression
//		String textTokenRegularExpression = "[A-Za-z_][A-Za-z0-9_]*";
//		
//		if (!identifier.matches(textTokenRegularExpression)) {
//			identifier = "\"" + identifier + "\"";
//		}
		
		return identifier;
	}

	
	public static DEVersion resolveVersion(String identifier, DEFeature feature) {
		if (identifier == null) {
			return null;
		}
		
		List<DEVersion> versions = feature.getVersions();
		
		for (DEVersion version : versions) {
			String number = version.getNumber();
			
			if (identifier.equals(number)) {
				return version;
			}
		}
		
		return null;
	}
	
	public static String deresolveVersion(DEVersion version) {
		return version.getNumber();
	}
}
