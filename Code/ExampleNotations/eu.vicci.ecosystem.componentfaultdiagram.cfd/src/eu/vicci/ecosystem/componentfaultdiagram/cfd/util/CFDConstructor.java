package eu.vicci.ecosystem.componentfaultdiagram.cfd.util;

import java.util.List;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDFactory;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGateType;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;

public class CFDConstructor {
	public static final String DEFAULT_IN_PORT_NAME = "In";
	public static final String DEFAULT_OUT_PORT_NAME = "Out";
	
	private static CFDFactory factory = CFDFactory.eINSTANCE;
	
	public static CFDDiagram createDiagram() {
		CFDDiagram diagram = factory.createCFDDiagram();

		return diagram;
	}
	
	public static CFDComponent createComponent() {
		return createComponent(null, null, null);
	}
	
	public static CFDComponent createComponent(String id, String name, CFDComponent container) {
		return createComponent(id, name, null, null, container);
	}
	
	public static CFDComponent createComponent(String id, String name, String[] inPortNames, String[] outPortNames, CFDComponent container) {
		CFDComponent component = factory.createCFDComponent();
		
		component.setId(id);
		component.setName(name);
		component.setComponent(container);
		
		if (inPortNames != null) {
			for (String inPortName : inPortNames) {
				createInPort(inPortName, component);
			}
		}
		
		if (outPortNames != null) {
			for (String outPortName : outPortNames) {
				createOutPort(outPortName, component);
			}
		}
		
		return component;
	}
	
	public static CFDComponent createRootComponent(CFDDiagram diagram) {
		return createRootComponent(null, null, diagram);
	}
	
	public static CFDComponent createRootComponent(String id, String name, CFDDiagram diagram) {
		return createRootComponent(id, name, null, null, diagram);
	}
	
	public static CFDComponent createRootComponent(String id, String name, String[] inPortNames, String[] outPortNames, CFDDiagram diagram) {
		CFDComponent rootComponent = createComponent(id, name, inPortNames, outPortNames, null);
		
		diagram.setRootComponent(rootComponent);
		
		return rootComponent;
	}
	
	public static CFDIntermediateEvent createIntermediateEvent() {
		return createIntermediateEvent(null,  null,  null);
	}
	
	public static CFDIntermediateEvent createIntermediateEvent(String id, String name, CFDComponent container) {
		CFDIntermediateEvent intermediateEvent = factory.createCFDIntermediateEvent();
		
		addDefaultInPort(intermediateEvent);
		addDefaultOutPort(intermediateEvent);

		intermediateEvent.setId(id);
		intermediateEvent.setName(name);
		intermediateEvent.setComponent(container);
		
		return intermediateEvent;
	}
	
	public static CFDBasicEvent createBasicEvent() {
		return createBasicEvent(null, null, null, null);
	}
	public static CFDBasicEvent createBasicEvent(String id, String name, Double probability, CFDComponent container) {
		CFDBasicEvent basicEvent = factory.createCFDBasicEvent();
		
		addDefaultOutPort(basicEvent);
		
		basicEvent.setId(id);
		basicEvent.setName(name);
		basicEvent.setProbability(probability);
		basicEvent.setComponent(container);
		
		return basicEvent;
	}
	
	public static CFDGate createGate() {
		return createGate(null, null, null);
	}
	
	public static CFDGate createGate(String id, CFDGateType type, CFDComponent container) {
		CFDGate gate = factory.createCFDGate();
		
		gate.setId(id);
		gate.setGateType(type);
		gate.setComponent(container);
		
		addDefaultInPort(gate);
		addDefaultOutPort(gate);
		
		return gate;
	}
	
	public static CFDInPort createInPort() {
		return createInPort(null, null);
	}
	
	public static CFDInPort createInPort(String name, CFDElement element) {
		CFDInPort inPort = factory.createCFDInPort();
		
		inPort.setName(name);
		inPort.setElement(element);
		
		return inPort;
	}
	
	
	public static CFDOutPort createOutPort() {
		return createOutPort(null, null);
	}
	
	public static CFDOutPort createOutPort(String name, CFDElement element) {
		CFDOutPort outPort = factory.createCFDOutPort();
		
		outPort.setName(name);
		outPort.setElement(element);
		
		return outPort;
	}
	
	public static CFDConnection createConnection() {
		return createConnection(null, null);
	}
	
//	public static CFDConnection createConnection(CFDOutPort sourcePort, CFDInPort targetPort) {
//		return doCreateConnection(sourcePort, targetPort);
//	}
	
	public static CFDConnection createConnection(CFDPort sourcePort, CFDPort targetPort) {
		CFDConnection connection = factory.createCFDConnection();
		
		connection.setSourcePort(sourcePort);
		connection.setTargetPort(targetPort);
		
		CFDComponent container = CFDUtil.findContainerForConnection(connection);
		connection.setComponent(container);
		
		return connection;
	}
	
//	public static CFDConnection createPopulatingConnection(CFDPort innerPort, CFDPort outerPort) {
//		throw new UnsupportedOperationException("Migrate and remember inner/outer vs. source/target problem");
////		CFDPopulatingConnection connection = factory.createCFDPopulatingConnection();
////		
////		//Both ports have to be either in or out ports, no mixes. 
////		if (!((innerPort instanceof CFDInPort && outerPort instanceof CFDInPort) || (innerPort instanceof CFDOutPort && outerPort instanceof CFDOutPort))) {
////			System.err.println("Warning: Created invalid populating connection with mix of in and out ports.");
////		}
////		
////		connection.setInnerPort(innerPort);
////		connection.setOuterPort(outerPort);
////		
////		CFDComponent container = CFDUtil.findContainerForPopulatingConnection(connection);
////		connection.setComponent(container);
////		
////		return connection;
//	}
	
	public static void addDefaultInPort(CFDElement element) {
		List<CFDInPort> inPorts = element.getInPorts();
		
		//Check if there already is a default in port.
		for (CFDInPort inPort : inPorts) {
			String name = inPort.getName();
			
			if (name != null && name.equals(DEFAULT_IN_PORT_NAME)) {
				return;
			}
		}
		
		CFDConstructor.createInPort(DEFAULT_IN_PORT_NAME, element);
	}
	
	public static void addDefaultOutPort(CFDElement element) {
		List<CFDOutPort> outPorts = element.getOutPorts();
		
		//Check if there already is a default out port.
		for (CFDOutPort outPort : outPorts) {
			String name = outPort.getName();
			
			if (name != null && name.equals(DEFAULT_OUT_PORT_NAME)) {
				return;
			}
		}
		
		CFDConstructor.createOutPort(DEFAULT_OUT_PORT_NAME, element);
	}
}
