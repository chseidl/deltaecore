package eu.vicci.ecosystem.sft.gef.factories;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.sft.SFTFactory;
import eu.vicci.ecosystem.sft.SFTGate;

public class SFTGateFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return SFTFactory.eINSTANCE.createSFTGate();
	}

	@Override
	public Object getObjectType() {
		return SFTGate.class;	}


}
