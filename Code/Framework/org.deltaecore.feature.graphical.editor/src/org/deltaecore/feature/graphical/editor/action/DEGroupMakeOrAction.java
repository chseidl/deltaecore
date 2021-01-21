package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEGroupChangeVariationTypeCommand;
import org.eclipse.gef.commands.Command;

public class DEGroupMakeOrAction extends DECommandAction {
	public static final String ID = DEGroupMakeOrAction.class.getCanonicalName();
	
	public DEGroupMakeOrAction(DEGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	protected String createText() {
		return "Make Group Or";
	}

	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionMakeGroupOr.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEGroup) {
			DEGroup group = (DEGroup) selectedModel;
			
			if (!group.isOr()) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	protected Command createCommand(Object acceptedModel) {
		DEGroup group = (DEGroup) acceptedModel;
		return new DEGroupChangeVariationTypeCommand(group, DEGroupChangeVariationTypeCommand.VariationType.OR);
	}
}
