package eu.vicci.ecosystem.sft.treelayout;

import org.abego.treelayout.NodeExtentProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTIdentifiable;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.gef.util.ConstantsEnum;

/**
 * Manages the different extents of the figures 
 * 
 * @author Florian
 *
 */
public class SFTNodeExtentProvider implements NodeExtentProvider<SFTIdentifiable> {

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public SFTNodeExtentProvider() {
		logger.trace("SFTNodeExtendProvider created.");
	}

	@Override
	public double getHeight(SFTIdentifiable part) {

		if (part instanceof SFTGate) {
			return ConstantsEnum.H_GATE;
		} else if (part instanceof SFTBasicFault) {
			return ConstantsEnum.H_BASICFAULT;
		} else if (part instanceof SFTIntermediateFault) {
			return ConstantsEnum.H_INTERMEDIATEFAULT;
		}

		return 100;
	}

	@Override
	public double getWidth(SFTIdentifiable part) {

		if (part instanceof SFTGate) {
			return ConstantsEnum.W_GATE;
		} else if (part instanceof SFTBasicFault) {
			return ConstantsEnum.W_BASICFAULT;
		} else if (part instanceof SFTIntermediateFault) {
			return ConstantsEnum.W_INTERMEDIATEFAULT;
		}

		return 100;
	}
}
