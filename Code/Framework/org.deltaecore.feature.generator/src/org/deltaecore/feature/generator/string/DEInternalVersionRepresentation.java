package org.deltaecore.feature.generator.string;

public class DEInternalVersionRepresentation extends DEInternalStringGeneratorData {
	private int major;
	private int minor;
	
	public DEInternalVersionRepresentation(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}
	
	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof DEInternalVersionRepresentation) {
			DEInternalVersionRepresentation typedOther = (DEInternalVersionRepresentation) other;
			
			return (typedOther.major == major && typedOther.minor == minor);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return major + "." + minor;
	}
}
