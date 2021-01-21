package org.deltaecore.feature.generator.string;

import java.util.List;

import org.deltaecore.feature.DEVersion;

public class DEVersionNumberGenerator extends DEStringGenerator<DEVersion, DEInternalVersionRepresentation> {
	@Override
	protected DEInternalVersionRepresentation doGenerate(DEVersion version, int attemptNumber) {
		if (attemptNumber > 10) {
//			DEDebug.println("Version: " + attemptNumber);
			//TODO: Here is a workaround for a problem with repetitive version numbers in very large feature models. Fix this some time.
			return new DEInternalVersionRepresentation(getRandom().nextInt(10) + 1, getRandom().nextInt(100));
		}
		
		//Have to look at siblings if plausible versioning is requested
		DEVersion supersededVersion = version.getSupersededVersion();
		boolean logicalPredecessorVersionIsSibling = false;
		
		DEInternalVersionRepresentation logicalPredecessorVersionRepresentation = getPreviousInternalRepresentation(supersededVersion);
		
		if (supersededVersion != null) {
			List<DEVersion> siblingVersions = supersededVersion.getSupersedingVersions();
			
			int index = siblingVersions.indexOf(version);
			
			if (index > 0) {
				DEVersion versionPredecessor = siblingVersions.get(index - 1);
				logicalPredecessorVersionRepresentation = getPreviousInternalRepresentation(versionPredecessor);
				logicalPredecessorVersionIsSibling = true;
			}
			
		}
		
		if (logicalPredecessorVersionRepresentation != null) {
			int major = logicalPredecessorVersionRepresentation.getMajor();
			int minor = logicalPredecessorVersionRepresentation.getMinor();
			
			if (logicalPredecessorVersionIsSibling || (!logicalPredecessorVersionIsSibling && getRandom().percentualChance(0.20))) {
				//Increase major and reset minor
				major++;
				minor = 0;
			} else {
				//Increase minor and keep major
				minor += 1 + getRandom().nextInt(3);
			}
			
			return new DEInternalVersionRepresentation(major, minor);
		}
		

		if (getRandom().percentualChance(0.13)) {
			//Small chance of a pre-release version
			int minor = getRandom().nextInt(9) + 1;
			return new DEInternalVersionRepresentation(0, minor);
		} else {
			return new DEInternalVersionRepresentation(1, 0);
		}
	}
}
