package eu.vicci.ecosystem.sft.accessor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTFault;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTGateType;
import eu.vicci.ecosystem.sft.SFTIdentifiable;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;

public abstract class SFTWriteAccessor {
	public abstract void setRootFault(SFTFault rootFault, SFTSoftwareFaultTree softwareFaultTree);
	
	
	public abstract void addFault(SFTFault fault, SFTGate parentGate);
	
	public void addBasicFault(SFTBasicFault basicFault, SFTGate parentGate) {
		addFault(basicFault, parentGate);
	}
	
	public void addIntermediateFault(SFTIntermediateFault intermediateFault, SFTGate parentGate) {
		addFault(intermediateFault, parentGate);
	}
	
	public abstract void addGate(SFTGate gate, SFTIntermediateFault parentIntermediateFault);

	
	
	protected abstract void modifyIdentifiable(SFTIdentifiable identifiable, String id);

	public abstract void modifyFault(SFTFault fault, String id, String name, String description);
	public abstract void modifyBasicFault(SFTBasicFault basicFault, String id, String name, String description, Double probability);
	
	public void modifyIntermediateFault(SFTIntermediateFault intermediateFault, String id, String name, String description) {
		modifyFault(intermediateFault, id, name, description);
	}

	public abstract void modifyGate(SFTGate gate, String id, SFTGateType gateType);
	
	
	protected void removeObject(EObject object) {
		EcoreUtil.remove(object);
	}
	
	public void removeFault(SFTFault fault) {
		removeObject(fault);
	}
	
	public void removeBasicFault(SFTBasicFault basicFault) {
		removeFault(basicFault);
	}
	
	public void removeIntermediateFault(SFTIntermediateFault intermediateFault) {
		removeFault(intermediateFault);
	}
	
	public void removeGate(SFTGate gate) {
		removeObject(gate);
	}
}
