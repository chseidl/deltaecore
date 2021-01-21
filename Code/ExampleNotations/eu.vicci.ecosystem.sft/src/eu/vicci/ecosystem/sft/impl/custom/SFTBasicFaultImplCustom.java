package eu.vicci.ecosystem.sft.impl.custom;

import eu.vicci.ecosystem.sft.impl.SFTBasicFaultImpl;

public class SFTBasicFaultImplCustom extends SFTBasicFaultImpl {

	@Override
	public boolean isLikely() {
		return (getProbability() > 0.5);
	}

}
