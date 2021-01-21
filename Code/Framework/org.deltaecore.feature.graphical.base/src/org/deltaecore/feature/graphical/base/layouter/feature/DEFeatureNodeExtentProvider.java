package org.deltaecore.feature.graphical.base.layouter.feature;

import org.abego.treelayout.NodeExtentProvider;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;

/**
 * Manages the different extents between two levels of the final tree.
 * 
 * @author Florian
 */
public class DEFeatureNodeExtentProvider implements NodeExtentProvider<DEFeature> {

	@Override
	public double getWidth(DEFeature feature) {
		return DEGeometryUtil.calculateFeatureWidth(feature);
	}

	@Override
	public double getHeight(DEFeature feature) {
		return DEGeometryUtil.calculateFeatureHeight(feature);
	}
}
