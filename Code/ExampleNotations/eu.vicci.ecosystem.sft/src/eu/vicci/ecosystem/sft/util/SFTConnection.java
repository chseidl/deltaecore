package eu.vicci.ecosystem.sft.util;

public class SFTConnection {

	private String gate = null;
	private String fault = null;

	public SFTConnection() {

	}

	public SFTConnection(String gate, String fault) {
		this.gate = gate;
		this.fault = fault;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}
}
