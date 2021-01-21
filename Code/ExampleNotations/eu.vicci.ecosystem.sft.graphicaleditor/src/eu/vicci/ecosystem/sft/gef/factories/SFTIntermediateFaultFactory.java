package eu.vicci.ecosystem.sft.gef.factories;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.sft.SFTFactory;
import eu.vicci.ecosystem.sft.SFTIntermediateFault;

public class SFTIntermediateFaultFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return SFTFactory.eINSTANCE.createSFTIntermediateFault();
	}

	@Override
	public Object getObjectType() {
		return SFTIntermediateFault.class;	}

}
