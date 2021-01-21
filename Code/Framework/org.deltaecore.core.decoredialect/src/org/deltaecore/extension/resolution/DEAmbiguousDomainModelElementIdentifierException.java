package org.deltaecore.extension.resolution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public class DEAmbiguousDomainModelElementIdentifierException extends Exception {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private List<EObject> matches;
	
	public DEAmbiguousDomainModelElementIdentifierException(String identifier, List<EObject> matches) {
		super(createMessage(identifier, matches));
		
		this.identifier = identifier;
		this.matches = new ArrayList<EObject>(matches);
	}
	
	private static String createMessage(String identifier, List<EObject> matches) {
		String message = "The identifier \"" + identifier + "\" is ambiguous and matches multiple elements: ";
		
		boolean isFirst = true;
		
		for (EObject element : matches) {
			if (!isFirst) {
				message += ", ";
			}
			
			message += element;
			
			isFirst = false;
		}
		
		return message;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<EObject> getMatches() {
		return matches;
	}
}
