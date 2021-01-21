package org.emftext.language.ecoreid.exception;

public abstract class EcoreIDException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EcoreIDException(String message) {
		super(message);
	}
}
