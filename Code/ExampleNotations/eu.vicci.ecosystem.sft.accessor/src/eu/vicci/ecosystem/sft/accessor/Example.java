package eu.vicci.ecosystem.sft.accessor;

import eu.vicci.ecosystem.sft.SFTFactory;
import eu.vicci.ecosystem.sft.SFTGate;
import eu.vicci.ecosystem.sft.SFTGateType;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;

public class Example {
	//Use a single instance for the entire editor.
	private static final SFTWriteAccessor writeAccessor = new SFTDirectWriteAccessor();
	
	public static SFTWriteAccessor getWriteAccessor() {
		return writeAccessor;
	}
	
	public void run() {
		//Channel _all_ write accesses to the model through the writeAccessor (read is unproblematic).
		SFTSoftwareFaultTree softwareFaultTree = SFTFactory.eINSTANCE.createSFTSoftwareFaultTree();
		
		SFTIntermediateFault rootFault = SFTFactory.eINSTANCE.createSFTIntermediateFault();
		//Passing parameters of modify operations with a null value results in an unchanged value.
		getWriteAccessor().modifyIntermediateFault(rootFault, "RF", "Root Fault", "The Root Fault");
		getWriteAccessor().setRootFault(rootFault, softwareFaultTree);
		
		SFTGate gate1 = SFTFactory.eINSTANCE.createSFTGate();
		getWriteAccessor().modifyGate(gate1, "G1", SFTGateType.OR);
		getWriteAccessor().addGate(gate1, rootFault);
		
		//And so on...
		
		System.out.println(rootFault);
	}
	
	public static void main(String[] args) {
		Example example = new Example();
		
		example.run();
	}

}
