package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;
import org.deltaecore.feature.util.DEVersionUtil;

public class DEVersionCreateSuccessorOnNewBranchAction extends DEAbstractVersionCreateAction {
	
	public static final String ID = DEVersionCreateSuccessorOnNewBranchAction.class.getCanonicalName();
	
	public DEVersionCreateSuccessorOnNewBranchAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Successor Version On New Branch";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreateSuccessorVersionOnNewBranch.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEVersion) {
			DEVersion version = (DEVersion) selectedModel;
			
			if (!DEVersionUtil.isLastVersionOnBranch(version)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected DEVersionCreateCommand doCreateCommand(DEVersion version, Object acceptedModel) {
		DEVersion selectedVersion = (DEVersion) acceptedModel;
		return new DEVersionCreateCommand(version, selectedVersion, true, false);
	}
}
