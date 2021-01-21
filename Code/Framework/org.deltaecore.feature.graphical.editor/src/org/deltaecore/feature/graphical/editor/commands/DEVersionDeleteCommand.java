package org.deltaecore.feature.graphical.editor.commands;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.util.DEVersionUtil;
import org.eclipse.gef.commands.Command;

public class DEVersionDeleteCommand extends Command {
	private DEVersion version;
	
	private DEFeature parentFeature; 
	private int indexInFeature;
	private DEVersion oldPredecessor;
	private List<DEVersion> oldSuccessors;
	
	public DEVersionDeleteCommand(DEVersion version) {
		this.version = version;
		
		oldPredecessor = null;
		oldSuccessors = new ArrayList<DEVersion>();
		indexInFeature = -1;
	}
	
	@Override
	public void execute() {
		//Save state
		parentFeature = version.getFeature();
		
		oldPredecessor = version.getSupersededVersion();
		oldSuccessors.clear();
		oldSuccessors.addAll(version.getSupersedingVersions());
		
		//NOTE: Order is mandatory here as removal of version sends out notifications that triggers further
		//updates at which point the wiring has to be in order.
		DEVersionUtil.unwireVersion(version);
		indexInFeature = DEVersionUtil.removeVersion(version);
	}
	
	@Override
	public void undo() {
		//NOTE: Order is mandatory here as addition of version sends out notifications that triggers further
		//updates at which point the wiring has to be in order.
		DEVersionUtil.rewireVersion(version, oldPredecessor, oldSuccessors);
		DEVersionUtil.addVersion(version, parentFeature, indexInFeature);
	}
}
