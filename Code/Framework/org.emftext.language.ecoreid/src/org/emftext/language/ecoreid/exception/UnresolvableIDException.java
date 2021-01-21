package org.emftext.language.ecoreid.exception;

import org.emftext.language.ecoreid.EcoreID;

public class UnresolvableIDException extends EcoreIDException {
	private static final long serialVersionUID = 1L;

	private EcoreID ecoreID;
	
	public UnresolvableIDException(EcoreID ecoreID) {
		super(createMessage());
		
		this.ecoreID = ecoreID;
	}
	
	private static String createMessage() {
		return "Could not resolve Ecore ID.";
	}

	public EcoreID getEcoreID() {
		return ecoreID;
	}
}
