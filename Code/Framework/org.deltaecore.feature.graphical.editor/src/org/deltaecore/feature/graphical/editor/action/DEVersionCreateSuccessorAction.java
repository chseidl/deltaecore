package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;

public class DEVersionCreateSuccessorAction extends DEAbstractVersionCreateAction {
	
	public static final String ID = DEVersionCreateSuccessorAction.class.getCanonicalName();
	
	public DEVersionCreateSuccessorAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Successor Version";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreateSuccessorVersion.png";
	}
	
	@Override
	protected DEVersionCreateCommand doCreateCommand(DEVersion version, Object acceptedModel) {
		DEVersion selectedVersion = (DEVersion) acceptedModel;
		return new DEVersionCreateCommand(version, selectedVersion, true);
	}
}
