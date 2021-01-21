package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEFeature;
import org.eclipse.gef.commands.Command;

public class DEFeatureRenameCommand extends Command {
	private DEFeature feature;
	private String newName;
	private String oldName;

	public DEFeatureRenameCommand(DEFeature feature, String newName) {
		this.feature = feature;
		this.newName = newName;
	}
	
	@Override
	public void execute() {
		oldName = feature.getName();
		feature.setName(newName);
	}

	@Override
	public void undo() {
		feature.setName(oldName);
	}
}
