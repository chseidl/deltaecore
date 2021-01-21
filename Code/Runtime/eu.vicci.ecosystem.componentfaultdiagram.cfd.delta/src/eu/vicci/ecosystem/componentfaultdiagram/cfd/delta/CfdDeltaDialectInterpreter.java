package eu.vicci.ecosystem.componentfaultdiagram.cfd.delta;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDFactory;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPackage;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.util.CFDUtil;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class CfdDeltaDialectInterpreter extends CfdAbstractDeltaDialectInterpreter {
	@Override
	protected boolean interpretConnect(DEModelWriter modelWriter, CFDPort sourcePort, CFDPort targetPort) {
		CFDConnection connection = CFDFactory.eINSTANCE.createCFDConnection();
		
		//Prompts opposite reference to be set as well, which modifies the current model.
		//Hence, use model writer for access.
		modelWriter.setValue(connection, CFDPackage.CFD_CONNECTION__SOURCE_PORT, sourcePort);
		modelWriter.setValue(connection, CFDPackage.CFD_CONNECTION__TARGET_PORT, targetPort);

		CFDComponent containerComponent = CFDUtil.findContainerForConnection(connection);
		modelWriter.addValue(containerComponent, CFDPackage.CFD_COMPONENT__CONNECTIONS, connection);
		
		return true;
	}

	@Override
	protected boolean interpretDisconnect(DEModelWriter modelWriter, CFDPort sourcePort, CFDPort targetPort) {
		CFDConnection connection = CFDUtil.findConnection(sourcePort, targetPort);
		
		if (connection == null) {
			return false;
		}

		//Detach from source and target port
		modelWriter.unsetValue(connection, CFDPackage.CFD_CONNECTION__SOURCE_PORT);
		modelWriter.unsetValue(connection, CFDPackage.CFD_CONNECTION__TARGET_PORT);
		
		//Remove from containment relation
		modelWriter.unsetValue(connection, CFDPackage.CFD_CONNECTION__COMPONENT);
		
		return true;
	}
}