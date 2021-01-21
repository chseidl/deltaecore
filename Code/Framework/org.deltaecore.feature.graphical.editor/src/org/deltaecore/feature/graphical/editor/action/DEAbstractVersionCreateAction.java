package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;
import org.deltaecore.feature.graphical.editor.factories.DEVersionFactory;
import org.eclipse.gef.commands.Command;

public abstract class DEAbstractVersionCreateAction extends DECommandAction {
	private static final DEVersionFactory versionFactory = new DEVersionFactory();
	
	public DEAbstractVersionCreateAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected Command createCommand(Object acceptedModel) {
		DEVersion version = versionFactory.getNewObject();
		return doCreateCommand(version, acceptedModel);
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEVersion) {
			return true;
		}
		
		return false;
	}
	
	protected abstract DEVersionCreateCommand doCreateCommand(DEVersion version, Object acceptedModel);
}
