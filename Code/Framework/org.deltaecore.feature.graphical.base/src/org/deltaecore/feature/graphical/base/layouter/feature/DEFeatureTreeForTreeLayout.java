/**
 * 
 */
package org.deltaecore.feature.graphical.base.layouter.feature;

import java.util.ArrayList;
import java.util.List;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;

/**
 * Required class for AbegoLayouter. Provides methods for accessing children and parent nodes.
 * 
 * @author Florian
 * 
 */
public class DEFeatureTreeForTreeLayout extends AbstractTreeForTreeLayout<DEFeature> {

	public DEFeatureTreeForTreeLayout(DEFeature root) {
		super(root);
	}

	@Override
	public List<DEFeature> getChildrenList(DEFeature feature) {
		ArrayList<DEFeature> children = new ArrayList<DEFeature>();

		if (feature == null) {
			return children;
		}
		
		List<DEGroup> groups = feature.getGroups();
		
		for (DEGroup group : groups) {
			List<DEFeature> features = group.getFeatures();
			children.addAll(features);
		}

		return children;
	}

	@Override
	public DEFeature getParent(DEFeature node) {
		return node.getParentOfFeature().getParentOfGroup();
	}
}
