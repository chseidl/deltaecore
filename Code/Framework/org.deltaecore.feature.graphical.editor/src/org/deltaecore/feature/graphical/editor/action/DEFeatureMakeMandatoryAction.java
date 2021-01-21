package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureChangeVariationTypeCommand;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.gef.commands.Command;

public class DEFeatureMakeMandatoryAction extends DECommandAction {
	public static final String ID = DEFeatureMakeMandatoryAction.class.getCanonicalName();
	
	public DEFeatureMakeMandatoryAction(DEGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	protected String createText() {
		return "Make Feature Mandatory";
	}

	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionMakeFeatureMandatory.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEFeature) {
			DEFeature feature = (DEFeature) selectedModel;
			
			if (!feature.isMandatory() && !DEFeatureUtil.isRootFeature(feature)) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	protected Command createCommand(Object acceptedModel) {
		DEFeature feature = (DEFeature) acceptedModel;
		return new DEFeatureChangeVariationTypeCommand(feature, DEFeatureChangeVariationTypeCommand.VariationType.MANDATORY);
	}
}
