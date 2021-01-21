package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.commands;

import org.eclipse.gef.commands.Command;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;

public class CFDCausalConnectionCreateCommand extends Command {

	private CFDOutPort sourcePort;
	private CFDInPort targetPort;
	private CFDComponent component;
	
	//private CFDCausalConnection causalConnection;
	
	private CFDConnection causalConnection;
	
	public CFDCausalConnectionCreateCommand(CFDOutPort sourcePort, CFDComponent component, CFDConnection causalConnection) {
		this.sourcePort = sourcePort;
		this.component = component;
		this.causalConnection = causalConnection;
	}
	
	@Override
	public boolean canExecute() {
		return sourcePort != null && targetPort != null && causalConnection != null;
	}

	@Override
	public void execute() {
		causalConnection.setSourcePort(sourcePort);
		causalConnection.setTargetPort(targetPort);
		causalConnection.setComponent(component);
	}

	@Override
	public void undo() {
		causalConnection.setSourcePort(null);
		causalConnection.setTargetPort(null);
		causalConnection.setComponent(null);
	}

	public void setTargetPort(CFDInPort targetPort) {
		this.targetPort = targetPort;
	}
}
