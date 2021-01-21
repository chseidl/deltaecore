package org.emftext.language.ecoreid.exception;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.emftext.language.ecoreid.EcoreID;

public class AmbiguousIDException extends EcoreIDException {
	private static final long serialVersionUID = 1L;

	private EcoreID ecoreID;
	private List<ENamedElement> possibleElements;
	
	public AmbiguousIDException(EcoreID ecoreID, List<ENamedElement> possibleElements) {
		super(createMessage());
		
		this.ecoreID = ecoreID;
		this.possibleElements = new ArrayList<ENamedElement>(possibleElements);
	}
	
	private static String createMessage() {
		return "The ecore ID is ambiguous.";
	}

	public EcoreID getEcoreID() {
		return ecoreID;
	}
	
	public List<ENamedElement> getPossibleElements() {
		return possibleElements;
	}
}
