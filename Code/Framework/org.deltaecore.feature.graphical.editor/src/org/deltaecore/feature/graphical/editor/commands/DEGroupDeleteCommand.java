package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEGroup;

public class DEGroupDeleteCommand extends DEGroupAbstractCreateDeleteCommand {
	public DEGroupDeleteCommand(DEGroup group) {
		super(group, group.getParentOfGroup());
	}
	
	@Override
	public void execute() {
		removeFromParent();		
	}

	@Override
	public void undo() {
		addToParent();
	}
}
