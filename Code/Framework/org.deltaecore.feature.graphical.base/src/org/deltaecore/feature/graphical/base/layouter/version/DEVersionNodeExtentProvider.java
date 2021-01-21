package org.deltaecore.feature.graphical.base.layouter.version;

import org.abego.treelayout.NodeExtentProvider;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;

/**
 * This class is responsible for defining the extents in x and y direction between two levels of a version tree
 * 
 * @author Florian
 * 
 */
public class DEVersionNodeExtentProvider implements NodeExtentProvider<DEVersion> {

	@Override
	public double getWidth(DEVersion version) {
		return DEGeometryUtil.calculateVersionWidth(version);
	}
	
	@Override
	public double getHeight(DEVersion version) {
		return DEGeometryUtil.calculateVersionHeight(version);
	}
}
