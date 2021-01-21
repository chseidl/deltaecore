package eu.vicci.ecosystem.componentfaultdiagram.cfd.delta.resolver;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDObject;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDUtil;

public class CFDIdentifier {
	public static final String PORT_NAME_SEPARATOR = ":";
	
	private String elementID;
	private String portName;
	
	public CFDIdentifier() {
		elementID = null;
		portName = null;
	}
	
	public CFDIdentifier(String rawIdentifier) {
		this();
		
		//Parse that shit!
		if (rawIdentifier != null && rawIdentifier.contains(PORT_NAME_SEPARATOR)) {
			//Escape the separator - split uses a regular expression, which interprets (some) special characters.
			String[] parts = rawIdentifier.split(Pattern.quote(PORT_NAME_SEPARATOR));
			
			if (parts.length != 2) {
				throw new UnsupportedOperationException();
			}
			
			elementID = parts[0];
			portName = parts[1];
		} else {
			if (!rawIdentifier.isEmpty()) {
				elementID = rawIdentifier;
			}
		}
	}
	
	
	public CFDIdentifier(CFDObject object) {
		this();
		
		if (object instanceof CFDPort) {
			CFDPort port = (CFDPort) object;
			portName = port.getName();
			
			CFDElement element = port.getElement();
			elementID = element.getId();
		}
		
		if (object instanceof CFDElement) {
			CFDElement element = (CFDElement) object;
			elementID = element.getId();
		}
	}
	
	public void setPortName(String portName) {
		this.portName = portName;
	}

	public CFDObject findMatch(CFDDiagram diagram) {
		//Empty IDs reference the diagram itself.
		if (elementID == null) {
			return diagram;
		}
		
		Iterator<EObject> iterator = diagram.eAllContents();
		
		while(iterator.hasNext()) {
			EObject eObject = iterator.next();
			
			if (eObject instanceof CFDElement) {
				CFDElement currentElement = (CFDElement) eObject;
				String currentElementID = currentElement.getId();
				
				if (currentElementID != null && currentElementID.equals(elementID)) {
					//Only remains to be seen if a port is targeted or not.
					if (portName != null) {
						return CFDUtil.findPort(portName, currentElement);
					}
					
					return currentElement;
				}
			}
		}

		return null;
	}
	
	public boolean matches(CFDObject object) {
		//Easy way out but probably doesn't scale well.
		CFDIdentifier objectIdentifier = new CFDIdentifier(object); 
		
		return equals(objectIdentifier);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof CFDIdentifier) {
			CFDIdentifier typedOther = (CFDIdentifier) other;
			return doesEqual(typedOther);
		}
		
		return false;
	}
	
	protected boolean doesEqual(CFDIdentifier other) {
		if (other == null) {
			return false;
		}
		
		if ((elementID == null && other.elementID != null) || (!elementID.equals(other.elementID))) {
			return false;
		}
		
		if ((portName == null && other.portName != null) || (!portName.equals(other.portName))) {
			return false;
		}
			
		
		return true;
	}
	
	@Override
	public String toString() {
		String output = "";
		
		if (elementID != null) {
			output += elementID;
		}
		
		if (portName != null) {
			output += PORT_NAME_SEPARATOR + portName;
		}
		
		return output;
	}
}
