package org.deltaecore.feature.generator.string;

public class DEInternalFeatureNameRepresentation extends DEInternalStringGeneratorData {
	private String qualifier;
	private String baseName;
	private String appendix;
	private String salt;
	
	public DEInternalFeatureNameRepresentation(String qualifier, String baseName, String appendix, String salt) {
		this.qualifier = qualifier;
		this.baseName = baseName;
		this.appendix = appendix;
		this.salt = salt;
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	public String getBaseName() {
		return baseName;
	}
	
	public String getAppendix() {
		return appendix;
	}

	public String getSalt() {
		return salt;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof DEInternalFeatureNameRepresentation) {
			DEInternalFeatureNameRepresentation typedOther = (DEInternalFeatureNameRepresentation) other;
			
			if (areDifferent(qualifier, typedOther.qualifier) || areDifferent(baseName, typedOther.baseName) || areDifferent(appendix, typedOther.appendix) || areDifferent(salt, typedOther.salt)) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	private static boolean areDifferent(String s1, String s2) {
		if ((s1 == null && s2 != null) || (s1 != null && !s1.equals(s2))) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		String name = "";
		
		if (qualifier != null) {
			name += qualifier;
		}
		
		name += baseName;
		
		if (appendix != null) {
			name += appendix;
		}
		
		if (salt != null) {
			name += " (" + salt +")";
		}
		
		return name;
	}
}
