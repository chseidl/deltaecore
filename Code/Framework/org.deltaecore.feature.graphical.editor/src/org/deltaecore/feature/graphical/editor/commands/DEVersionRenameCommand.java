package org.deltaecore.feature.graphical.editor.commands;

import org.deltaecore.feature.DEVersion;
import org.eclipse.gef.commands.Command;

public class DEVersionRenameCommand extends Command {
	private DEVersion version;
	private String newNumber;
	private String oldNumber;

	public DEVersionRenameCommand(DEVersion version, String newNumber) {
		this.version = version;
		this.newNumber = newNumber;
	}
	
	@Override
	public void execute() {
		oldNumber = version.getNumber();
		version.setNumber(newNumber);
	}

	@Override
	public void undo() {
		version.setNumber(oldNumber);
	}
}
