package eu.vicci.ecosystem.sft.gef.factories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;
import eu.vicci.ecosystem.sft.gef.editparts.SFTBasicFaultEditPart;
import eu.vicci.ecosystem.sft.gef.editparts.SFTConnectionEditPart;
import eu.vicci.ecosystem.sft.gef.editparts.SFTGateEditPart;
import eu.vicci.ecosystem.sft.gef.editparts.SFTIntermediateFaultEditPart;
import eu.vicci.ecosystem.sft.gef.editparts.SFTSoftwareFaultTreeEditPart;
import eu.vicci.ecosystem.sft.util.SFTConnection;

public class SFTEditPartFactory implements EditPartFactory {

	Logger logger = LogManager.getLogger(SFTEditPartFactory.class.getSimpleName());

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if (model instanceof SFTSoftwareFaultTree) {
			part = new SFTSoftwareFaultTreeEditPart();
			logger.trace("createEditPart() - SFTSoftwareFaultTree");
		} else if (model instanceof SFTIntermediateFault) {
			part = new SFTIntermediateFaultEditPart();
			logger.trace("createEditPart() - SFTIntermediateFaultEditPart");
		} else if (model instanceof SFTGate) {
			part = new SFTGateEditPart();
			logger.trace("createEditPart() - SFTGateEditPart");
		} else if (model instanceof SFTBasicFault) {
			part = new SFTBasicFaultEditPart();
			logger.trace("createEditPart() - SFTBasicFaultEditPart");
		} else if (model instanceof SFTConnection) {
			part = new SFTConnectionEditPart();
			logger.trace("createEditPart() - SFTConnectionEditPart");
		}

		if (part != null) {
			part.setModel(model);
		}

		return part;
	}
}