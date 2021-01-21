package eu.vicci.ecosystem.sft.delta;

import java.util.List;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTFactory;
import eu.vicci.ecosystem.sft.SFTFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTGateType;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class SftDeltaDialectInterpreter extends SftAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretRefineBasicFault(DEModelWriter modelWriter, SFTBasicFault basicFault, String gateId, SFTGateType gateType, SFTFault subFault1, SFTFault subFault2) {
		SFTIntermediateFault newIntermediateFault = SFTFactory.eINSTANCE.createSFTIntermediateFault();
		newIntermediateFault.setId(basicFault.getId());
		newIntermediateFault.setDescription(basicFault.getDescription());
		
		SFTGate gate = SFTFactory.eINSTANCE.createSFTGate();
		gate.setId(gateId);
		gate.setGateType(gateType);
		newIntermediateFault.setGate(gate);
		
		List<SFTFault> faults = gate.getFaults();
		faults.add(subFault1);
		faults.add(subFault2);
		
		EcoreUtil.replace(basicFault, newIntermediateFault);
		
		return true;
	}

	@Override
	protected boolean interpretDeleteWithReferences(DEModelWriter modelWriter, SFTFault f) {
		// TODO Auto-generated method stub
		return false;
	}
	
}