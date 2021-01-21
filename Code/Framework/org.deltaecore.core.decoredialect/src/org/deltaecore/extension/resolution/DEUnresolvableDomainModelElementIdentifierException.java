package org.deltaecore.extension.resolution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

public class DEUnresolvableDomainModelElementIdentifierException extends Exception {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private List<Resource> resources;
	
	public DEUnresolvableDomainModelElementIdentifierException(String identifier, List<Resource> resources) {
		super(createMessage(identifier, resources));
		
		this.identifier = identifier;
		this.resources = new ArrayList<Resource>(resources);
	}
	
	private static String createMessage(String identifier, List<Resource> resources) {
		String message = "The identifier \"" + identifier + "\" is unresolvable within the following resources: ";
		
		boolean isFirst = true;
		
		for (Resource resource : resources) {
			if (!isFirst) {
				message += ", ";
			}
			
			message += resource.getURI().lastSegment();
			
			isFirst = false;
		}
		
		return message;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<Resource> getResources() {
		return resources;
	}
}
