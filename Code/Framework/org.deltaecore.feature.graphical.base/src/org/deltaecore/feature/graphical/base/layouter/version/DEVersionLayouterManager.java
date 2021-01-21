package org.deltaecore.feature.graphical.base.layouter.version;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;

/**
 * Singleton (this can only be a singleton as the respective layouters are created per version instance, i.e., unique even over model bounds)
 * 
 * Each Feature with versions needs such a tree in order to organize the versions. In other words, each version tree has one layouter. They are
 * organized with the help of this class
 * 
 * The reason for each VersionTree having its own layouter is that these very layouters are accessed during redrawing in order to get the coordinates
 * of the single elements. A recalculation is not performed until there is a change in the layout.
 */
public class DEVersionLayouterManager {
	private static DEVersionLayouterManager instance = null;
	
	protected static DEVersionLayouterManager getInstance() {
		if (instance == null) {
			instance = new DEVersionLayouterManager();
		}
		
		return instance;
	}
	
	
	private Map<DEFeature, DEVersionTreeLayouter> featureToLayouterMap;

	private DEVersionLayouterManager() {
		featureToLayouterMap = new HashMap<DEFeature, DEVersionTreeLayouter>();
	}

	
	/**
	 * This method retrieves the appropriate layouter from a map. If there is no such layouter, a new one will be created and returned.
	 */
	public static DEVersionTreeLayouter getLayouter(DEFeature feature) {
		return getInstance().doGetLayouter(feature);
	}
	
	private DEVersionTreeLayouter doGetLayouter(DEFeature feature) {
		if (!featureToLayouterMap.containsKey(feature)) {
			doUpdateLayouter(feature);
		}
		
		return featureToLayouterMap.get(feature);
	}
	
	public static void updateLayouter(DEFeature feature) {
		getInstance().doUpdateLayouter(feature);
	}
	
	private void doUpdateLayouter(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		
		if (!versions.isEmpty()) {
			featureToLayouterMap.put(feature, new DEVersionTreeLayouter(feature));
		}
	}
}
