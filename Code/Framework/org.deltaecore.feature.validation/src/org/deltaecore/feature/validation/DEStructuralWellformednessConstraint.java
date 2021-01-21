package org.deltaecore.feature.validation;

import java.util.List;

import org.deltaecore.feature.DECardinalityBasedElement;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEStructuralWellformednessConstraint extends AbstractConstraint<DECardinalityBasedElement> {

	@Override
	protected IStatus doValidate(DECardinalityBasedElement cardinalityBasedElement) {
		if (cardinalityBasedElement instanceof DEFeature) {
			DEFeature feature = (DEFeature) cardinalityBasedElement;
			List<DEVersion> versions = feature.getVersions();
			
			//At least one version
			if (versions.isEmpty()) {
				String message = "A feature has to declare at least one version.";
				return createErrorStatus(message, feature);
			}
			
			if (!hasTreeStructuredVersions(feature)) {
				String message = "The versions of a feature have to be arranged as a tree (exactly one initial version, no cycles, all superseded/superseding versions belong to the same feature).";
				return createErrorStatus(message, feature);
			}
		}
		
		if (cardinalityBasedElement instanceof DEGroup) {
			//At least one child feature
			DEGroup group = (DEGroup) cardinalityBasedElement;
			
			List<DEFeature> features = group.getFeatures();
			
			if (features.isEmpty()) {
				String message = "A group has to contain at least one feature.";
				return createErrorStatus(message, group);
			}
		}
		
		return createSuccessStatus();
	}

	private static boolean hasTreeStructuredVersions(DEFeature feature) {
		//Versions have to form a tree
		//- Only one initial version
		//- No cycles
		//- all referenced versions belong to same feature
		boolean hasValidInitialVersion = hasValidInitialVersion(feature);
		boolean isFreeOfVersionCycles = isFreeOfVersionCycles(feature);
		boolean allReferencedVersionsBelongToSameFeature = allReferencedVersionsBelongToSameFeature(feature);
		
		return hasValidInitialVersion && isFreeOfVersionCycles && allReferencedVersionsBelongToSameFeature;
	}
	
	private static boolean hasValidInitialVersion(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		boolean hasInitial = false;
		
		for (DEVersion version : versions) {
			boolean isInitial = (version.getSupersededVersion() == null);
			
			if (isInitial) {
				if (hasInitial) {
					//Second initial version
					return false;
				} else {
					hasInitial = true;
				}
			}
		}
		
		return hasInitial;
	}
	
	private static boolean isFreeOfVersionCycles(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		
		for (DEVersion version : versions) {
			DEVersion currentVersion = version.getSupersededVersion();
					
			while(currentVersion != null) {
				//Version has itself as superseded version -> cycle.
				if (currentVersion == version) {
					return false;
				}
				
				currentVersion = currentVersion.getSupersededVersion();
			}
		}
		
		return true;
	}
	
	private static boolean allReferencedVersionsBelongToSameFeature(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		
		for (DEVersion version : versions) {
			DEVersion supersededVersion = version.getSupersededVersion();
			if (supersededVersion != null && supersededVersion.getFeature() != feature) {
				return false;
			}
			
			List<DEVersion> supersedingVersions = version.getSupersedingVersions();
			for (DEVersion supersedingVersion : supersedingVersions) {
				if (supersedingVersion.getFeature() != feature) {
					return false;
				}
			}
		}
		
		return true;
	}
}
