package eu.vicci.ecosystem.sft.treelayout;

import java.awt.geom.Rectangle2D.Double;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.draw2d.geometry.Point;

import eu.vicci.ecosystem.sft.SFTIdentifiable;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;

/**
 * Singleton
 * 
 * http://code.google.com/p/treelayout/
 * 
 * @author Florian
 *
 */

public class AbegoLayouter {

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	private static AbegoLayouter instance;
	private TreeLayout<SFTIdentifiable> treeLayout;
	private Map<String, SFTIdentifiable> idToPartMap;

	public static AbegoLayouter getInstance() {
		if (instance == null)
			instance = new AbegoLayouter();
		return instance;
	}

	public void init() {
		idToPartMap = new HashMap<String, SFTIdentifiable>();
	}

	/**
	 * Here, the SFT is layouted. An internal representation, called treeLayout, keeps track of the exact positions of the tree elements
	 * @param sft
	 */
	public void layout(SFTSoftwareFaultTree sft) {
		TreeForTreeLayout<SFTIdentifiable> tree = new SFTAsTreeForTreeLayout(sft.getRootFault());
		SFTNodeExtentProvider extents = new SFTNodeExtentProvider();
		DefaultConfiguration<SFTIdentifiable> config = new DefaultConfiguration<SFTIdentifiable>(50, 50);
		treeLayout = new TreeLayout<SFTIdentifiable>(tree, extents, config);

		setUpMap();
	}

	/**
	 * Creates a map<id, SFTIdentifiable> for later lookups from the figures
	 */
	private void setUpMap() {
		logger.trace("Build map for later lookup of coordinates ");
		for (Entry<SFTIdentifiable, Double> entry : treeLayout.getNodeBounds().entrySet()) {
			idToPartMap.put(entry.getKey().getId(), entry.getKey());
		}
	}

	/**
	 * output method for convenience
	 */
	public void printMap() {
		for (Entry<SFTIdentifiable, Double> entry : treeLayout.getNodeBounds().entrySet()) {
			logger.trace("Bounds: " + entry.getKey() + "/" + entry.getValue());
		}

	}

	public TreeLayout<?> getTreeLayout() {
		return treeLayout;
	}
/**
 * 
 * @param key id of the element
 * @return Point(left,top) of the element
 */
	public Point getCoordinates(String key) {
		Map<SFTIdentifiable, Double> elements = treeLayout.getNodeBounds();

		if (idToPartMap.containsKey(key)) {
			logger.trace(key + " recognized.");
			SFTIdentifiable tmp = idToPartMap.get(key);
			int x = (int) elements.get(tmp).getX();
			int y = (int) elements.get(tmp).getY();
			Point res = new Point(x, y);
			return res;

		} else {
			logger.trace(key + " not recognized.");
			return new Point(10, 10);
		}
	}

	/**
	 * 
	 * @param key id of the element
	 * @return a point representing the center of the figure
	 */
	public Point getCenterOfPart(String key) {

		Map<SFTIdentifiable, Double> elements = treeLayout.getNodeBounds();

		if (idToPartMap.containsKey(key)) {
			logger.trace(key + " recognized.");
			SFTIdentifiable tmp = idToPartMap.get(key);
			int x = (int) elements.get(tmp).getCenterX();
			int y = (int) elements.get(tmp).getCenterY();
			Point res = new Point(x, y);
			return res;

		} else {
			logger.trace(key + " not recognized.");
			return new Point(10, 10);
		}

	}

	public void shiftTreeXY(int add_x, int add_y) {

		Map<SFTIdentifiable, Double> elements = treeLayout.getNodeBounds();

		for (SFTIdentifiable tmp : elements.keySet()) {
			logger.trace(tmp);
			int x = (int) elements.get(tmp).getX();
			int y = (int) elements.get(tmp).getY();

			elements.get(tmp).setRect(x + add_x, y + add_y, elements.get(tmp).getWidth(), elements.get(tmp).getHeight());
		}
	}

}
