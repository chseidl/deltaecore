package org.deltaecore.feature.graphical.base.layouter.version;

import java.util.List;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.deltaecore.feature.DEVersion;

/**
 * Required class for the AbegoLayouter.
 * 
 * @author Florian
 * 
 */
public class DEVersionTreeForTreeLayout extends AbstractTreeForTreeLayout<DEVersion> {

	public DEVersionTreeForTreeLayout(DEVersion initialVersion) {
		super(initialVersion);
	}

	@Override
	public List<DEVersion> getChildrenList(DEVersion version) {
		return version.getSupersedingVersions();
	}

	@Override
	public DEVersion getParent(DEVersion version) {
		return version.getSupersededVersion();
	}
}
