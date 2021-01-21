package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.creationfactories;

import java.security.InvalidParameterException;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDConstructor;

public class CFDElementCreationFactory implements CreationFactory {

	public Class<? extends CFDElement> creationClass;
	
	public CFDElementCreationFactory(Class<? extends CFDElement> creationClass) {
		this.creationClass = creationClass;
	}
	
	@Override
	public Object getNewObject() {
		if (creationClass.equals(CFDComponent.class)) {
			return CFDConstructor.createComponent();
		}
		
		if (creationClass.equals(CFDIntermediateEvent.class)) {
			return CFDConstructor.createIntermediateEvent();
		}
		
		if (creationClass.equals(CFDBasicEvent.class)) {
			return CFDConstructor.createBasicEvent();
		}
		
		if (creationClass.equals(CFDGate.class)) {
			return CFDConstructor.createGate();
		}

		throw new InvalidParameterException();
	}

	@Override
	public Object getObjectType() {
		return creationClass;
	}
}
