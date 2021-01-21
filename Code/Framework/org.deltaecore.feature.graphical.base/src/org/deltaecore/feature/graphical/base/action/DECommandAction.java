package org.deltaecore.feature.graphical.base.action;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.eclipse.gef.commands.Command;

public abstract class DECommandAction extends DESelectionDependentAction {
	
	public DECommandAction(DEGraphicalEditor editor, boolean allowMultipleSelection) {
		super(editor, allowMultipleSelection);
	}

	public DECommandAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected void execute(Object acceptedModel) {
		Command command = createCommand(acceptedModel);
		
		DEGraphicalEditor editor = getEditor();
		editor.executeCommand(command);
	}
	
	protected abstract Command createCommand(Object acceptedModel);
}
