package eu.vicci.ecosystem.sft.gef.factories;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.sft.SFTBasicFault;
import eu.vicci.ecosystem.sft.SFTFactory;

public class SFTBasicFaultFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return SFTFactory.eINSTANCE.createSFTBasicFault();
	}

	@Override
	public Object getObjectType() {
		return SFTBasicFault.class;
	}

}
