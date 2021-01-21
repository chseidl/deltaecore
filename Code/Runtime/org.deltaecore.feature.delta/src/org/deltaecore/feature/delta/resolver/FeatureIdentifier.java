package org.deltaecore.feature.delta.resolver;

import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.eclipse.emf.ecore.EObject;

//- features: name
//- groups: featureName:groupIndex
//- versions: featureName@versionNumber

public class FeatureIdentifier {
	private static enum MatchingMode {
		FEATURE,
		GROUP,
		VERSION
	}
	
	private MatchingMode matchingMode;
	
	private String searchedFeatureName;
	private Integer searchedGroupIndex;
	private String searchedVersionNumber;
	
	public FeatureIdentifier(String rawIdentifier) {
		parseRawIdentifier(rawIdentifier);
	}
	
	private void parseRawIdentifier(String rawIdentifier) {
		if (rawIdentifier.contains(":")) {
			matchingMode = MatchingMode.GROUP;
			
			String[] rawIdentifierParts = rawIdentifier.split(":");
			searchedFeatureName = rawIdentifierParts[0];
			try {
				searchedGroupIndex = Integer.parseInt(rawIdentifierParts[1]);
			} catch(NumberFormatException e) {
				searchedGroupIndex = null;
			}
			searchedVersionNumber = null;
		} else if (rawIdentifier.contains("@")) {
			matchingMode = MatchingMode.VERSION;

			String[] rawIdentifierParts = rawIdentifier.split("@");
			searchedFeatureName = rawIdentifierParts[0];
			searchedGroupIndex = null;
			searchedVersionNumber = rawIdentifierParts[1];
		} else {
			matchingMode = MatchingMode.FEATURE;
			searchedFeatureName = rawIdentifier;
			searchedGroupIndex = null;
			searchedVersionNumber = null;
		}
		
//		System.out.println("Identifier: " + matchingMode + ", " + searchedFeatureName + ", " + searchedGroupIndex + ", " + searchedVersionNumber);
	}
	
	public static String deresolve(EObject element) {
		if (element instanceof DEFeature) {
			DEFeature feature = (DEFeature) element;
			return feature.getName();
		}
		
		if (element instanceof DEGroup) {
			DEGroup group = (DEGroup) element;
			DEFeature feature = (DEFeature) group.eContainer();
			List<DEGroup> groups = feature.getGroups();
			
			int index = groups.indexOf(group);
			
			return feature.getName() + ":" + index;
		}
		
		if (element instanceof DEVersion) {
			DEVersion version = (DEVersion) element;
			DEFeature feature = version.getFeature();
			
			return feature.getName() + "@" + version.getNumber();
		}
		
		return "?";
	}
	
	public EObject resolve(EObject model) {
		if (searchedFeatureName == null) {
			return null;
		}
		
		if (matchingMode == MatchingMode.GROUP && (searchedGroupIndex == null || searchedGroupIndex < 0)) {
			return null;
		}
		
		if (matchingMode == MatchingMode.VERSION && searchedVersionNumber == null) {
			return null;
		}
		
		//naive implementation
		Iterator<EObject> iterator = model.eAllContents();
		
		while (iterator.hasNext()) {
			EObject element = iterator.next();
			
			if (element instanceof DEFeature) {
				DEFeature feature = (DEFeature) element;
				
				if (searchedFeatureName.equals(feature.getName())) {
					switch(matchingMode) {
						case FEATURE:
							return feature;
					
						case GROUP: {
							List<DEGroup> groups = feature.getGroups();
							
							if (searchedGroupIndex >= groups.size()) {
								return null;
							}
							
							return groups.get(searchedGroupIndex);
						}
					
						case VERSION: {
							List<DEVersion> versions = feature.getVersions();
							
							for (DEVersion version : versions) {
								if (searchedVersionNumber.equals(version.getNumber())) {
									return version;
								}
							}
							
							return null;
						}
					}
				}
			}
		}
		
		return null;
	}
}
