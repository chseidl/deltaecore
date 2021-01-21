package org.deltaecore.feature.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;

public class DEVersionUtil {
	public static DEVersion getRootVersion(DEFeature feature) {
		//Make sure that it is the initial version not just the one declared first in the model
		List<DEVersion> versions = feature.getVersions();
		
		for (DEVersion version : versions) {
			if (isRootVersion(version)) {
				return version;
			}
		}
		
		return null;
	}
	
	
	public static boolean isRootVersion(DEVersion version) {
		return version.getSupersededVersion() == null;
	}
	
	public static DEVersion getLastVersionOnMostRecentBranch(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		DEVersion lastVersion = null;
		
		for (DEVersion version : versions) {
			if (isLastVersionOnBranch(version)) {
				lastVersion = version;
			}
		}
		
		return lastVersion;
	}

	public static void wireAndAddVersionAsRoot(DEVersion version, DEFeature parentFeature) {
		if (version == null || parentFeature == null) {
			throw new InvalidParameterException();
		}
		
		//NOTE: Order mandatory for possible notifications.
		wireVersionAsRoot(version, parentFeature);
		addVersion(version, parentFeature);
	}
	
	public static void wireAndAddVersionAfter(DEVersion version, DEVersion addAfterVersion) {
		if (version == null || addAfterVersion == null) {
			throw new InvalidParameterException();
		}
		
		//NOTE: Order mandatory for possible notifications.
		wireVersionAfter(version, addAfterVersion);
		addVersionAfter(version, addAfterVersion);
	}
	
	public static void wireAndAddVersionAfterOnNewBranch(DEVersion version, DEVersion addAfterVersion) {
		if (version == null || addAfterVersion == null) {
			throw new InvalidParameterException();
		}
		
		//NOTE: Order mandatory for possible notifications.
		wireVersionAfterOnNewBranch(version, addAfterVersion);
		addVersionAfter(version, addAfterVersion);
	}
	
	public static void wireAndAddVersionBefore(DEVersion version, DEVersion addBeforeVersion) {
		if (version == null || addBeforeVersion == null) {
			throw new InvalidParameterException();
		}
		
		//NOTE: Order mandatory for possible notifications.
		wireVersionBefore(version, addBeforeVersion);
		addVersionBefore(version, addBeforeVersion);
	}
	
	
	public static void addVersionAfter(DEVersion version, DEVersion addAfterVersion) {
		if (version == null || addAfterVersion == null) {
			throw new InvalidParameterException();
		}
		
		DEFeature parentFeature = addAfterVersion.getFeature();
		List<DEVersion> versions = parentFeature.getVersions();
		int index = versions.indexOf(addAfterVersion) + 1;
		addVersion(version, parentFeature, index);
	}
	
	public static void addVersionBefore(DEVersion version, DEVersion addBeforeVersion) {
		if (version == null || addBeforeVersion == null) {
			throw new InvalidParameterException();
		}
		
		DEFeature parentFeature = addBeforeVersion.getFeature();
		List<DEVersion> versions = parentFeature.getVersions();
		int index = versions.indexOf(addBeforeVersion);
		addVersion(version, parentFeature, index);
	}
	
	public static void addVersion(DEVersion version, DEFeature parentFeature) {
		addVersion(version, parentFeature, -1);
	}
	
	public static void addVersion(DEVersion version, DEFeature parentFeature, int index) {
		List<DEVersion> versions = parentFeature.getVersions();
		
		if (index == -1) {
			versions.add(version);
		} else if (index < 0) {
			versions.add(0, version);
		} else if (index >= versions.size()) {
			versions.add(version);
		} else {
			versions.add(index, version);
		}
	}
	
	public static int removeVersion(DEVersion version) {
		DEFeature parentFeature = version.getFeature();
		
		if (parentFeature == null) {
			return -1;
		}
		
		List<DEVersion> versions = parentFeature.getVersions();
		int index = versions.indexOf(version);
		versions.remove(version);
		return index;
	}
	
	public static void wireVersionAsRoot(DEVersion version, DEFeature parentFeature) {
		if (version == null || parentFeature == null) {
			throw new InvalidParameterException();
		}
		
		DEVersion oldRootVersion = getRootVersion(parentFeature);
		
		if (oldRootVersion != null) {
			oldRootVersion.setSupersededVersion(version);
		}
	}
	
	public static void wireVersionAfter(DEVersion version, DEVersion insertAfterVersion) {
		if (version == null || insertAfterVersion == null) {
			throw new InvalidParameterException();
		}
		
		DEVersion predecessor = insertAfterVersion;
		List<DEVersion> successors = insertAfterVersion.getSupersedingVersions();
		
		rewireVersion(version, predecessor, successors);
	}
	
	public static void wireVersionAfterOnNewBranch(DEVersion version, DEVersion insertAfterVersion) {
		if (version == null || insertAfterVersion == null) {
			throw new InvalidParameterException();
		}
		
		DEVersion predecessor = insertAfterVersion;
		rewireVersion(version, predecessor, null);
	}
	
	public static void wireVersionBefore(DEVersion version, DEVersion insertBeforeVersion) {
		wireVersionBefore(version, insertBeforeVersion, false);
	}
	
	public static void wireVersionBefore(DEVersion version, DEVersion insertBeforeVersion, boolean insertBeforeBranching) {
		if (version == null || insertBeforeVersion == null) {
			throw new InvalidParameterException();
		}
		
		DEVersion predecessor = insertBeforeVersion.getSupersededVersion();
		
		if (predecessor == null) {
			DEFeature parentFeature = insertBeforeVersion.getFeature();
			wireVersionAsRoot(version, parentFeature);
		} else {
			List<DEVersion> successors = insertBeforeBranching ? predecessor.getSupersedingVersions() : Collections.singletonList(insertBeforeVersion);
			rewireVersion(version, predecessor, successors);
		}
	}
	
	public static void rewireVersion(DEVersion version, DEVersion newPredecessor, List<DEVersion> newSuccessors) {
		if (version == null) {
			return;
		}
		
		//Defensive copy as the list may change due to inverse references!
		List<DEVersion> newSuccessorsCopy = new ArrayList<DEVersion>();
		
		if (newSuccessors != null) {
			newSuccessorsCopy.addAll(newSuccessors);
		}
		
		version.setSupersededVersion(newPredecessor);
		
		for (DEVersion newSuccessor : newSuccessorsCopy) {
			newSuccessor.setSupersededVersion(version);
		}
	}
	
	public static void unwireAndRemoveVersion(DEVersion version) {
		DEVersionUtil.unwireVersion(version);
		DEVersionUtil.removeVersion(version);
	}
	
	public static void unwireVersion(DEVersion version) {
		List<DEVersion> successors = version.getSupersedingVersions();
		//Defensive copy as the list may change due to inverse references!
		List<DEVersion> oldSuccessors = new ArrayList<DEVersion>(successors);
		
		DEVersion oldPredecessor = version.getSupersededVersion();
		version.setSupersededVersion(null);
		
		for (DEVersion oldSuccessor : oldSuccessors) {
			oldSuccessor.setSupersededVersion(oldPredecessor);
		}
	}
	
	public static boolean isLastVersionOnBranch(DEVersion version) {
		List<DEVersion> successors = version.getSupersedingVersions();
		return successors.isEmpty();
	}
}
