package eu.vicci.ecosystem.sft.gef.editparts;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;
import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;
import eu.vicci.ecosystem.sft.treelayout.AbegoLayouter;
import eu.vicci.ecosystem.sft.util.SFTConnection;

/**
 * This class represents the SFT from the model. {@link #getModelChildren() is the method that returns an ArrayList with all
 * elements that will be drawn by the application. The {@link #layouter} is responsible for the correct y values of the
 * single components ({@see eu.vicci.ecosystem.softwarefaulttree.gef.utils.Layouter}).
 * 
 * @author Fla
 * 
 */
public class SFTSoftwareFaultTreeEditPart extends SFTAbstractEditPart {

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
	private AbegoLayouter abegoLayouter = null;

	public SFTSoftwareFaultTreeEditPart() {
		super();
		abegoLayouter = AbegoLayouter.getInstance();
	}

	@Override
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setLayoutManager(new FreeformLayout());
		return layer;
	}

	/**
	 * Called from outside. Responsible for building a list with all model elements.
	 * 
	 * @return A list with all model elements. First part: Nodes; Second (last) part: Connections
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	protected List getModelChildren() {

		abegoLayouter.init();

		List retVal = new ArrayList();
		SFTSoftwareFaultTree sft = (SFTSoftwareFaultTree) getModel();

		// traverse tree for nodes and connections
		startTreeAnalysis(retVal, sft);

		abegoLayouter.layout(sft);
		abegoLayouter.printMap();
		abegoLayouter.shiftTreeXY(ConstantsEnum.TREE_SHIFT_LEFT, ConstantsEnum.TREE_SHIFT_DOWN);

		return retVal;
	}

	/**
	 * startTreeAnalysis(...) traverses the SFT for connections and nodes. Note that the order order
	 * "first connections, then nodes" is important. If it is changed, the polylines representing the nodes will be drawn
	 * above the figures representing the nodes.
	 * 
	 * @param retVal
	 * @param sft
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void startTreeAnalysis(List retVal, SFTSoftwareFaultTree sft) {

		// add rootFault: Basic or Intermediate
		if (isIntermediateFault(sft.getRootFault())) {

			SFTIntermediateFault rootFault = (SFTIntermediateFault) sft.getRootFault();

			SFTGate gate = rootFault.getGate();

			startTraversalConnections(retVal, sft, gate);

			// because of the z-index of the different components the root fault needs to be added after the traversing the
			// connections, otherwise it is painted under the first line representing the connection
			addIntermediateFault(retVal, rootFault);

			startTraversalNodes(retVal, sft, gate);

		} else if (isBasicFault(sft.getRootFault())) {
			retVal.add(((SFTBasicFault) sft.getRootFault()));
			logger.trace("getModelChildren() - add Intermediate Basic Fault");
		}
	}

	/*
	 * Handles nodes
	 */

	@SuppressWarnings("rawtypes")
	private void startTraversalNodes(List retVal, SFTSoftwareFaultTree sft, SFTGate gate) {
		logger.trace("startTraversal " + gate);

		addGate(retVal, gate);

		recurseNodes(retVal, sft, gate);
	}

/**
	 * This method checks the gate according to it's type. For each child either a basic fault or an intermediate fault is
	 * added. Furthermore, for each child that is no basic fault and therefore is an intermediate fault,
	 * {@link #recurseNodes(List, SFTSoftwareFaultTree, SFTGate) is invoked.
	 * 
	 * @param retVal
	 * @param sft
	 */
	@SuppressWarnings("rawtypes")
	private void recurseNodes(List retVal, SFTSoftwareFaultTree sft, SFTGate gate) {

		List<SFTFault> faults = gate.getFaults();

		for (SFTFault f : faults) {
			if (isIntermediateFault(f)) {

				addIntermediateFault(retVal, f);

				SFTIntermediateFault intermediateFault = (SFTIntermediateFault) f;
				addGate(retVal, intermediateFault.getGate());

				recurseNodes(retVal, sft, intermediateFault.getGate());
			}
			if (isBasicFault(f)) {
				addBasicFault(retVal, f);
			}
		}
	}

	/*
	 * Handles connections
	 */

	@SuppressWarnings("rawtypes")
	private void startTraversalConnections(List retVal, SFTSoftwareFaultTree sft, SFTGate gate) {
		logger.trace("startTraversal " + gate);

		addConnection(retVal, gate.getId(), sft.getRootFault().getId());

		recurseConnections(retVal, sft, gate);
	}

	@SuppressWarnings({ "rawtypes" })
	private void recurseConnections(List retVal, SFTSoftwareFaultTree sft, SFTGate gate) {
		logger.trace(gate);

		addConnection(retVal, gate.getId(), gate.getParent().getId());

		List<SFTFault> faults = gate.getFaults();
		for (SFTFault f : faults) {

			addConnection(retVal, gate.getId(), f.getId());

			if (isIntermediateFault(f)) {

				recurseConnections(retVal, sft, ((SFTIntermediateFault) f).getGate());
			}
		}
	}

	/*
	 * Handling of the primitive types
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addConnection(List retVal, String gate, String fault) {
		retVal.add(new SFTConnection(gate, fault));
		logger.trace("getModelChildren() - addConnection");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addBasicFault(List retVal, SFTFault f) {
		retVal.add((SFTBasicFault) f);
		logger.trace("getModelChildren() - add Basic Root Fault");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addIntermediateFault(List retVal, SFTFault f) {
		retVal.add((SFTIntermediateFault) f);
		logger.trace("getModelChildren() - add Intermediate Root Fault");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addGate(List retVal, SFTGate gate) {
		retVal.add(gate);
		logger.trace("getModelChildren() - add Gate");
	}

	private boolean isBasicFault(SFTFault f) {
		if (f instanceof SFTBasicFault)
			return true;
		else
			return false;
	}

	private boolean isIntermediateFault(SFTFault f) {
		if (f instanceof SFTIntermediateFault) {
			return true;
		} else
			return false;
	}
}
