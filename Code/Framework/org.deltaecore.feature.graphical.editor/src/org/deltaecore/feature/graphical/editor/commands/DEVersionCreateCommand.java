package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.util.DEVersionUtil;
import org.eclipse.gef.commands.Command;

//TODO: Support branching
public class DEVersionCreateCommand extends Command {
	private DEVersion version;
	private Object parentObject;
	
	private boolean wireAndAddAfter;
	private boolean useSameBranch;
	
	public DEVersionCreateCommand(DEVersion version, Object parentObject) {
		initialize(version, parentObject, true, true);
	}
	
	public DEVersionCreateCommand(DEVersion version, DEVersion parentVersion, boolean wireAndAddAfter) {
		initialize(version, parentVersion, wireAndAddAfter, true);
	}
	
	public DEVersionCreateCommand(DEVersion version, DEVersion parentVersion, boolean wireAndAddAfter, boolean useNewBranch) {
		initialize(version, parentVersion, wireAndAddAfter, useNewBranch);
	}
	
	private void initialize(DEVersion version, Object parentObject, boolean wireAndAddAfter, boolean useSameBranch) {
		this.version = version;
		this.parentObject = parentObject;
		this.wireAndAddAfter = wireAndAddAfter;
		this.useSameBranch = useSameBranch;
	}
	
	@Override
	public void execute() {
		if (parentObject instanceof DEFeature) {
			DEFeature parentFeature = (DEFeature) parentObject;
			addToParentFeature(parentFeature);
		}
		
		if (parentObject instanceof DEVersion) {
			DEVersion parentVersion = (DEVersion) parentObject;
			addToParentVersion(parentVersion);
		}
	}

	@Override
	public void undo() {
		DEVersionUtil.unwireAndRemoveVersion(version);
	}
	
	protected void addToParentFeature(DEFeature parentFeature) {
		DEVersion selectedVersion = DEVersionUtil.getLastVersionOnMostRecentBranch(parentFeature);
		doAddToParentFeature(parentFeature, selectedVersion);
	}
	
	protected void addToParentVersion(DEVersion parentVersion) {
		DEFeature parentFeature = parentVersion.getFeature();
		doAddToParentFeature(parentFeature, parentVersion);
	}

	protected void doAddToParentFeature(DEFeature parentFeature, DEVersion selectedVersion) {
		if (selectedVersion == null) {
			DEVersionUtil.wireVersionAsRoot(version, parentFeature);
		} else {
			if (wireAndAddAfter) {
				if (useSameBranch) {
					DEVersionUtil.wireVersionAfter(version, selectedVersion);
				} else {
					DEVersionUtil.wireVersionAfterOnNewBranch(version, selectedVersion);
				}
			} else {
				if (useSameBranch) {
					DEVersionUtil.wireVersionBefore(version, selectedVersion);
				} else {
					DEVersionUtil.wireVersionBefore(version, selectedVersion, true);
				}
			}
		}
		
		DEVersionUtil.addVersion(version, parentFeature);
	}
}
