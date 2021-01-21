package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;

public class DEVersionCreatePredecessorAction extends DEAbstractVersionCreateAction {
	
	public static final String ID = DEVersionCreatePredecessorAction.class.getCanonicalName();
	
	public DEVersionCreatePredecessorAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Predecessor Version";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreatePredecessorVersion.png";
	}
	
	@Override
	protected DEVersionCreateCommand doCreateCommand(DEVersion version, Object acceptedModel) {
		DEVersion selectedVersion = (DEVersion) acceptedModel;
		return new DEVersionCreateCommand(version, selectedVersion, false, true);
	}
}
