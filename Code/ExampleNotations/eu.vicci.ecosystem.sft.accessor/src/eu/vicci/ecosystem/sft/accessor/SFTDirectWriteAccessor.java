package eu.vicci.ecosystem.sft.accessor;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTGateType;
import eu.vicci.ecosystem.sft.SFTIdentifiable;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;

public class SFTDirectWriteAccessor extends SFTWriteAccessor {
	
	@Override
	public void setRootFault(SFTFault rootFault, SFTSoftwareFaultTree softwareFaultTree) {
		softwareFaultTree.setRootFault(rootFault);
	}
	
	
	
	@Override
	public void addFault(SFTFault fault, SFTGate parentGate) {
		parentGate.getFaults().add(fault);
	}
	
	@Override
	public void addGate(SFTGate gate, SFTIntermediateFault parentIntermediateFault) {
		parentIntermediateFault.setGate(gate);
	}
	
	
	
	@Override
	protected void modifyIdentifiable(SFTIdentifiable identifiable, String id) {
		if (id != null) {
			identifiable.setId(id);
		}
	}
	
	@Override
	public void modifyFault(SFTFault fault, String id, String name, String description) {
		modifyIdentifiable(fault, id);
		
		if (name != null) {
			fault.setName(name);
		}
		
		if (description != null) {
			fault.setDescription(description);
		}
	}
	
	@Override
	public void modifyBasicFault(SFTBasicFault basicFault, String id, String name, String description, Double probability) {
		modifyFault(basicFault, id, name, description);
		
		if (probability != null) {
			basicFault.setProbability(probability);
		}
	}
	
	@Override
	public void modifyGate(SFTGate gate, String id, SFTGateType gateType) {
		modifyIdentifiable(gate, id);
		
		if (gateType != null) {
			gate.setGateType(gateType);
		}
	}
}
