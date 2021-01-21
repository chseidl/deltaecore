package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEGroupChangeVariationTypeCommand;
import org.eclipse.gef.commands.Command;

public class DEGroupMakeAlternativeAction extends DECommandAction {
	public static final String ID = DEGroupMakeAlternativeAction.class.getCanonicalName();
	
	public DEGroupMakeAlternativeAction(DEGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	protected String createText() {
		return "Make Group Alternative";
	}

	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionMakeGroupAlternative.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEGroup) {
			DEGroup group = (DEGroup) selectedModel;
			
			if (!group.isAlternative()) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	protected Command createCommand(Object acceptedModel) {
		DEGroup group = (DEGroup) acceptedModel;
		return new DEGroupChangeVariationTypeCommand(group, DEGroupChangeVariationTypeCommand.VariationType.ALTERNATIVE);
	}
}
