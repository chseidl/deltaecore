package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.creationfactories;

import java.security.InvalidParameterException;

import org.eclipse.gef.requests.CreationFactory;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDConstructor;

public class CFDConnectionCreationFactory implements CreationFactory {

	public Class<? extends CFDConnection> creationClass;
	
	public CFDConnectionCreationFactory(Class<? extends CFDConnection> creationClass) {
		this.creationClass = creationClass;
	}
	
	@Override
	public Object getNewObject() {
		if (creationClass.equals(CFDConnection.class)) {
			return CFDConstructor.createConnection();
		}
		
		throw new InvalidParameterException();
	}

	@Override
	public Object getObjectType() {
		return creationClass;
	}
}
