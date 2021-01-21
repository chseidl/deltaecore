/**
 * 
 */
package eu.vicci.ecosystem.sft.treelayout;

import java.util.ArrayList;
import java.util.List;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTIdentifiable;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;

/**
 * Required class for AbegoLayouter. Provides method for accessing children and parent nodes
 * 
 * @author Florian
 * 
 */
public class SFTAsTreeForTreeLayout extends AbstractTreeForTreeLayout<SFTIdentifiable> {

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public SFTAsTreeForTreeLayout(SFTIdentifiable root) {
		super(root);
		logger.trace("Create tree for layout process...");
	}

	@Override
	public List<SFTIdentifiable> getChildrenList(SFTIdentifiable node) {

		ArrayList<SFTIdentifiable> children = new ArrayList<SFTIdentifiable>();

		if (node instanceof SFTIntermediateFault) {
			children.add(((SFTIntermediateFault) node).getGate());
		}
		else if (node instanceof SFTGate) {
			for (SFTIdentifiable t : ((SFTGate) node).getFaults()) {
				children.add(t);
			}
		}

		logger.trace(node.getId() + ", " + children.size());

		return children;
	}

	@Override
	public SFTIdentifiable getParent(SFTIdentifiable node) {

		if (node instanceof SFTIntermediateFault)
			((SFTIntermediateFault) node).getParent();
		else if (node instanceof SFTGate)
			((SFTGate) node).getParent();
		else if (node instanceof SFTBasicFault)
			((SFTBasicFault) node).getParent();
		return null;
	}

}
