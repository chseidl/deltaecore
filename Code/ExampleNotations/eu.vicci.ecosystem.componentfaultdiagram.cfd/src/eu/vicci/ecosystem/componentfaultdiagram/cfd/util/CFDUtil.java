package eu.vicci.ecosystem.componentfaultdiagram.cfd.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDObject;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;

public class CFDUtil {

	public CFDUtil() {
	}

	public static CFDComponent getComponentForPort(CFDPort port) {
		CFDElement element = port.getElement();
		
		//If this IS the owning component, return it...
		if (element instanceof CFDComponent) {
			return (CFDComponent) element;
		}
		
		//... otherwise use the real owning component for this element.
		return element.getComponent();
	}
	
	public static CFDInPort findInPort(String portName, CFDElement element) {
		List<CFDInPort> inPorts = element.getInPorts();
		
		for (CFDInPort inPort : inPorts) {
			if (inPort.getName().equals(portName)) {
				return inPort;
			}
		}
		
		return null;
	}
	
	public static CFDOutPort findOutPort(String portName, CFDElement element) {
		List<CFDOutPort> outPorts = element.getOutPorts();
		
		for (CFDOutPort outPort : outPorts) {
			if (outPort.getName().equals(portName)) {
				return outPort;
			}
		}
		
		return null;
	}
	
	public static CFDPort findPort(String portName, CFDElement element) {
		CFDInPort inPort = CFDUtil.findInPort(portName, element);
		
		if (inPort != null) {
			return inPort;
		}
		
		CFDOutPort outPort = CFDUtil.findOutPort(portName, element);
		
		if (outPort != null) {
			return outPort;
		}
		
		return null;
	}
	
	//Causal connections may only reside on the level of the owning component for a port
	//Populating connections may reside one level higher if the port is the inner port.
	
	public static List<CFDConnection> findOutgoingConnectionsForPort(CFDPort searchedPort) {
		//Outgoing connection means that the specified port is the source/inner port.
		List<CFDConnection> connections = getAllConnections(searchedPort);
		List<CFDConnection> matchingConnections = new ArrayList<CFDConnection>();
		
		for (CFDConnection connection : connections) {
			CFDPort sourcePort = connection.getSourcePort();
			
			if (sourcePort == searchedPort) {
				matchingConnections.add(connection);
			}
		}
		
		return matchingConnections;
	}
	
	public static List<CFDConnection> findIncomingConnectionsForPort(CFDPort searchedPort) {
		//Incoming connection means that the specified port is the target/outer port.
		List<CFDConnection> connections = getAllConnections(searchedPort);
		List<CFDConnection> matchingConnections = new ArrayList<CFDConnection>();
		
		for (CFDConnection connection : connections) {
			CFDPort targetPort = connection.getTargetPort();
			
			if (targetPort == searchedPort) {
				matchingConnections.add(connection);
			}
		}
		
		return matchingConnections;
	}
	
	public static CFDConnection findConnection(CFDPort searchedSourcePort, CFDPort searchedTargetPort) {
		if (searchedSourcePort == null || searchedTargetPort == null) {
			return null;
		}
		
		CFDDiagram diagram = (CFDDiagram) EcoreUtil.getRootContainer(searchedSourcePort);
		Iterator<EObject> iterator = diagram.eAllContents();
		
		while(iterator.hasNext()) {
			EObject eObject = iterator.next();
			
			if (eObject instanceof CFDConnection) {
				CFDConnection connection = (CFDConnection) eObject;
				
				CFDPort sourcePort = connection.getSourcePort();
				CFDPort targetPort = connection.getTargetPort();
				
				//Pointer equality intended.
				if (sourcePort == searchedSourcePort && targetPort == searchedTargetPort) {
					return connection;
				}
			}
		}
		
		return null;
	}
	
	private static List<CFDConnection> getAllConnections(CFDObject object) {
		EObject rootContainer = EcoreUtil.getRootContainer(object);
		
		TreeIterator<EObject> iterator = rootContainer.eAllContents();
		List<CFDConnection> allConnections = new ArrayList<CFDConnection>();
		
		while (iterator.hasNext()) {
			EObject next = iterator.next();
			
			if (next instanceof CFDConnection) {
				CFDConnection connection = (CFDConnection) next;
				
				allConnections.add(connection);
			}
		}
		
		return allConnections;
	}
	
	public static boolean isRootComponent(CFDComponent component) {
		EObject container = component.eContainer();
		
		if (container instanceof CFDDiagram) {
			CFDDiagram diagram = (CFDDiagram) container;
			
			CFDComponent rootComponent = diagram.getRootComponent();
			
			return (rootComponent == component);
		}
		
		return false;
	}
	
	public static boolean isPortOfRootComponent(CFDPort port) {
		CFDElement element = port.getElement();
		
		if (element instanceof CFDComponent) {
			CFDComponent component = (CFDComponent) element;
			
			if (isRootComponent(component)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static CFDComponent findContainerForConnection(CFDConnection connection) {
		CFDPort sourcePort = connection.getSourcePort();
		CFDPort targetPort = connection.getTargetPort();
		
		if (sourcePort instanceof CFDOutPort && targetPort instanceof CFDInPort) {
			return findContainerForCausalConnection(connection);
		}
		
		if ((sourcePort instanceof CFDInPort && targetPort instanceof CFDInPort) || (sourcePort instanceof CFDOutPort && targetPort instanceof CFDOutPort)) {
			return findContainerForPopulatingConnection(connection);
		}
		
		throw new InvalidParameterException();
	}
	
	private static CFDComponent findContainerForCausalConnection(CFDConnection connection) {
		CFDPort sourcePort = connection.getSourcePort();
		CFDPort targetPort = connection.getTargetPort();
		
		CFDElement sourceElement = sourcePort.getElement();
		CFDElement targetElement = targetPort.getElement();
		
		CFDComponent sourceComponent = sourceElement.getComponent();
		CFDComponent targetComponent = targetElement.getComponent();
		
		if (sourceComponent == targetComponent) {
			return sourceComponent;
		}
		
		throw new InvalidParameterException("Unable to determine container for causal connection " + connection + " as the combination of source and target port is invalid.");
	}
	
	private static CFDComponent findContainerForPopulatingConnection(CFDConnection connection) {
		CFDPort sourcePort = connection.getSourcePort();
		CFDPort targetPort = connection.getTargetPort();
		
		CFDElement sourceElement = sourcePort.getElement();
		CFDElement targetElement = targetPort.getElement();
		
		if (targetElement instanceof CFDComponent) {
			CFDComponent targetComponent = (CFDComponent) targetElement;
			
			if (sourceElement.getComponent() == targetComponent) {
				return targetComponent;
			}
		}
		
		if (sourceElement instanceof CFDComponent) {
			CFDComponent sourceComponent = (CFDComponent) sourceElement;
			
			if (targetElement.getComponent() == sourceComponent) {
				return sourceComponent;
			}
		}
		
		throw new InvalidParameterException("Unable to determine container for populating connection " + connection + " as the combination of source and target port is invalid.");
	}
}
