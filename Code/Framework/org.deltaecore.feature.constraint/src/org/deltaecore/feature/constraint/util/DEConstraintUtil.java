package org.deltaecore.feature.constraint.util;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.expression.DERelativeVersionRestriction;
import org.deltaecore.feature.expression.DERelativeVersionRestrictionOperator;
import org.deltaecore.feature.expression.DEVersionRangeRestriction;
import org.deltaecore.feature.expression.DEVersionRestriction;

public class DEConstraintUtil {
	public static boolean versionSatisfiesVersionRestriction(DEVersion version, DEVersionRestriction versionRestriction) {
		List<DEVersion> versionsSatisfyingVersionRestriction = getVersionsSatisfyingVersionRestriction(versionRestriction);
		
		return versionsSatisfyingVersionRestriction.contains(version);
	}
	
	
	public static List<DEVersion> getVersionsSatisfyingVersionRestriction(DEVersionRestriction versionRestriction) {
		if (versionRestriction instanceof DEVersionRangeRestriction) {
			DEVersionRangeRestriction versionRangeRestriction = (DEVersionRangeRestriction) versionRestriction;
			
			return getVersionsSatisfyingVersionRangeRestriction(versionRangeRestriction);
		}
		
		if (versionRestriction instanceof DERelativeVersionRestriction) {
			DERelativeVersionRestriction relativeVersionRestriction = (DERelativeVersionRestriction) versionRestriction;
			
			return getVersionsSatisfyingRelativeVersionRestriction(relativeVersionRestriction);
		}
		
		throw new UnsupportedOperationException("Don't know how to handle " + versionRestriction.getClass().getSimpleName() + ".");
	}
	
	protected static List<DEVersion> getVersionsSatisfyingVersionRangeRestriction(DEVersionRangeRestriction versionRangeRestriction) {
		List<DEVersion> versions = new ArrayList<DEVersion>();
		
		DEVersion lowerVersion = versionRangeRestriction.getLowerVersion();
		DEVersion upperVersion = versionRangeRestriction.getUpperVersion();
		
		boolean lowerIsIncluded = versionRangeRestriction.isLowerIncluded();
		boolean upperIsIncluded = versionRangeRestriction.isUpperIncluded();
		
		if (upperIsIncluded) {
			versions.add(upperVersion);
		}
		
		DEVersion version = upperVersion.getSupersededVersion();
		
		while (version != null && version != lowerVersion) {
			versions.add(0, version);
			
			version = version.getSupersededVersion();
		}
		
		if (lowerIsIncluded) {
			versions.add(0, lowerVersion);
		}
		
		return versions;
	}
	
	protected static List<DEVersion> getVersionsSatisfyingRelativeVersionRestriction(DERelativeVersionRestriction relativeVersionRestriction) {
		List<DEVersion> versions = new ArrayList<DEVersion>();
		
		DERelativeVersionRestrictionOperator operator = relativeVersionRestriction.getOperator();
		DEVersion version = relativeVersionRestriction.getVersion();
		
		if (operator == DERelativeVersionRestrictionOperator.EQUAL || operator == DERelativeVersionRestrictionOperator.IMPLICIT_EQUAL || 
				operator == DERelativeVersionRestrictionOperator.LESS_THAN_OR_EQUAL || operator == DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL) {
			versions.add(version);
		}
		
		if (operator == DERelativeVersionRestrictionOperator.LESS_THAN || operator == DERelativeVersionRestrictionOperator.LESS_THAN_OR_EQUAL) {
			versions.addAll(collectAllSupersededVersions(version));
		}

		if (operator == DERelativeVersionRestrictionOperator.GREATER_THAN || operator == DERelativeVersionRestrictionOperator.GREATER_THAN_OR_EQUAL) {
			versions.addAll(collectAllSupersedingVersions(version));
		}
		
		return versions;
	}
	
	protected static List<DEVersion> collectAllSupersededVersions(DEVersion version) {
		List<DEVersion> allSupersededVersions = new ArrayList<DEVersion>();
		
		version = version.getSupersededVersion();
		
		while (version != null) {
			allSupersededVersions.add(0, version);
			
			version = version.getSupersededVersion();
		}
		
		return allSupersededVersions;
	}
	
	protected static List<DEVersion> collectAllSupersedingVersions(DEVersion version) {
		List<DEVersion> allSupersedingVersions = new ArrayList<DEVersion>();
		doCollectAllSupersedingVersions(version, allSupersedingVersions);
		return allSupersedingVersions;
	}
	
	protected static void doCollectAllSupersedingVersions(DEVersion version, List<DEVersion> allSupersedingVersions) {
		List<DEVersion> supersedingVersions = version.getSupersedingVersions();
		
		for (DEVersion supersedingVersion : supersedingVersions) {
			if (!allSupersedingVersions.contains(supersedingVersion)) {
				allSupersedingVersions.add(supersedingVersion);
			}
			
			doCollectAllSupersedingVersions(supersedingVersion, allSupersedingVersions);
		}
	}
}
