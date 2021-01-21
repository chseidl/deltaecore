package org.emftext.language.ecoreid.resource.ecoreid.exception;

import org.emftext.language.ecoreid.exception.EcoreIDException;

public class UnparsableEcoreIDException extends EcoreIDException {
	private static final long serialVersionUID = 1L;
	
	private String rawEcoreID;
	
	public UnparsableEcoreIDException(String rawEcoreID) {
		super(createMessage(rawEcoreID));
		
		this.rawEcoreID = rawEcoreID;
	}
	
	private static String createMessage(String rawEcoreID) {
		return "Could not parse Ecore ID \"" + rawEcoreID + "\".";
	}

	public String getRawEcoreID() {
		return rawEcoreID;
	}
}
