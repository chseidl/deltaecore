package eu.vicci.ecosystem.componentfaultdiagram.cfd.test;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGateType;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDConstructor;

public abstract class CFDTester {
	protected CFDDiagram createDiagramSimple1() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();
		
		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", null, new String[]{"Out"}, diagram);
		
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.001, rootComponent);
		
		//Wiring
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		return diagram;
	}

	protected CFDDiagram createDiagramSimple2() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();
		
		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", null, new String[]{"Out"}, diagram);
		
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		CFDGate gate2 = CFDConstructor.createGate("Gate2", CFDGateType.OR, rootComponent);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.001, rootComponent);
		CFDBasicEvent basicEvent3 = CFDConstructor.createBasicEvent("BE3", "BasicEvent3", 0.03, rootComponent);
		
		//Wiring
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(gate2.getOutPorts().get(0), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent3.getOutPorts().get(0), gate2.getInPorts().get(0));
		
		return diagram;
	}
	
	protected CFDDiagram createDiagramSimple3() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();

		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", new String[]{"In"}, new String[]{"Out"}, diagram);
		
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		CFDGate gate2 = CFDConstructor.createGate("Gate2", CFDGateType.OR, rootComponent);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.001, rootComponent);
		
		//Wiring
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(gate2.getOutPorts().get(0), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(rootComponent.getInPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate2.getInPorts().get(0));
		
		return diagram;
	}
	
	protected CFDDiagram createDiagramNested1() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();
		
		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", null, new String[]{"Out"}, diagram);
		
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		
		CFDComponent component2 = CFDConstructor.createComponent("C2", "Component2", null, new String[]{"Out"}, rootComponent);
		
		
		CFDGate gate2 = CFDConstructor.createGate("Gate2", CFDGateType.OR, component2);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.001, component2);
		CFDBasicEvent basicEvent3 = CFDConstructor.createBasicEvent("BE3", "BasicEvent3", 0.03, component2);
		
		//Wiring
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		CFDConstructor.createConnection(component2.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		CFDConstructor.createConnection(gate2.getOutPorts().get(0), component2.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent3.getOutPorts().get(0), gate2.getInPorts().get(0));
		
		return diagram;
	}
	
	protected CFDDiagram createDiagramNested2() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();
		
		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", new String[] {"In"}, new String[]{"Out"}, diagram);
		
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		
		CFDComponent component2 = CFDConstructor.createComponent("C2", "Component2", new String[] {"In"}, new String[]{"Out"}, rootComponent);
		
		
		CFDGate gate2 = CFDConstructor.createGate("Gate2", CFDGateType.OR, component2);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.001, component2);
		
		//Wiring
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		CFDConstructor.createConnection(component2.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		CFDConstructor.createConnection(gate2.getOutPorts().get(0), component2.getOutPorts().get(0));
		CFDConstructor.createConnection(component2.getInPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(rootComponent.getInPorts().get(0), component2.getInPorts().get(0));
		
		return diagram;
	}
	
	protected CFDDiagram createDiagramComplex1() {
		//Create diagram
		CFDDiagram diagram = CFDConstructor.createDiagram();

		//Elements
		CFDComponent rootComponent = CFDConstructor.createRootComponent("RC", "RootComponent", new String[] {"In"}, new String[]{"Out1", "Out2"}, diagram);
		
		CFDIntermediateEvent intermediateEvent1 = CFDConstructor.createIntermediateEvent("IE1", "IntermediateEvent1", rootComponent);
		CFDGate gate1 = CFDConstructor.createGate("Gate1", CFDGateType.AND, rootComponent);
		CFDGate gate2 = CFDConstructor.createGate("Gate2", CFDGateType.OR, rootComponent);
		CFDIntermediateEvent intermediateEvent2 = CFDConstructor.createIntermediateEvent("IE2", "IntermediateEvent2", rootComponent);
		CFDBasicEvent basicEvent1 = CFDConstructor.createBasicEvent("BE1", "BasicEvent1", 0.01, rootComponent);
		
		CFDComponent component2 = CFDConstructor.createComponent("C2", "Component2", new String[] {"In1", "In2"}, new String[]{"Out1", "Out2"}, rootComponent);
		CFDGate gate3 = CFDConstructor.createGate("Gate3", CFDGateType.AND, component2);
		CFDGate gate4 = CFDConstructor.createGate("Gate4", CFDGateType.OR, component2);
		CFDBasicEvent basicEvent2 = CFDConstructor.createBasicEvent("BE2", "BasicEvent2", 0.02, component2);
		
		CFDGate gate5 = CFDConstructor.createGate("Gate5", CFDGateType.OR, rootComponent);
		CFDBasicEvent basicEvent3 = CFDConstructor.createBasicEvent("BE3", "BasicEvent2", 0.03, rootComponent);
		CFDBasicEvent basicEvent4 = CFDConstructor.createBasicEvent("BE4", "BasicEvent2", 0.004, rootComponent);
		
		
		//Wiring
		CFDConstructor.createConnection(intermediateEvent1.getOutPorts().get(0), rootComponent.getOutPorts().get(0));
		CFDConstructor.createConnection(gate1.getOutPorts().get(0), rootComponent.getOutPorts().get(1));
		
		CFDConstructor.createConnection(gate3.getOutPorts().get(0), component2.getOutPorts().get(0));
		CFDConstructor.createConnection(gate4.getOutPorts().get(0), component2.getOutPorts().get(1));
		
		CFDConstructor.createConnection(component2.getInPorts().get(0), gate3.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getInPorts().get(0), gate4.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getInPorts().get(1), gate3.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getInPorts().get(1), gate4.getInPorts().get(0));
		
		CFDConstructor.createConnection(rootComponent.getInPorts().get(0), gate5.getInPorts().get(0));
		
		
		CFDConstructor.createConnection(gate2.getOutPorts().get(0), intermediateEvent1.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getOutPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getOutPorts().get(1), intermediateEvent1.getInPorts().get(0));
		CFDConstructor.createConnection(component2.getOutPorts().get(1), gate1.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent4.getOutPorts().get(0), gate1.getInPorts().get(0));
		
		CFDConstructor.createConnection(intermediateEvent2.getOutPorts().get(0), gate2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent1.getOutPorts().get(0), intermediateEvent2.getInPorts().get(0));
		CFDConstructor.createConnection(gate5.getOutPorts().get(0), intermediateEvent2.getInPorts().get(0));
		CFDConstructor.createConnection(gate5.getOutPorts().get(0), component2.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent3.getOutPorts().get(0), gate5.getInPorts().get(0));
		CFDConstructor.createConnection(basicEvent4.getOutPorts().get(0), component2.getInPorts().get(1));
		
		CFDConstructor.createConnection(basicEvent2.getOutPorts().get(0), gate4.getInPorts().get(0));
		
		return diagram;
	}
}
