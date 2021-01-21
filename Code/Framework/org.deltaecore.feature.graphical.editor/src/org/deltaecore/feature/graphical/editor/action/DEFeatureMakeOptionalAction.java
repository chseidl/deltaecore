package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureChangeVariationTypeCommand;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.gef.commands.Command;

public class DEFeatureMakeOptionalAction extends DECommandAction {
	public static final String ID = DEFeatureMakeOptionalAction.class.getCanonicalName();
	
	public DEFeatureMakeOptionalAction(DEGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	protected String createText() {
		return "Make Feature Optional";
	}

	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionMakeFeatureOptional.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEFeature) {
			DEFeature feature = (DEFeature) selectedModel;
			DEGroup parentGroup = feature.getParentOfFeature();
			
			if ((!feature.isOptional() || (parentGroup != null && (parentGroup.isOr() || parentGroup.isAlternative()))) && !DEFeatureUtil.isRootFeature(feature)) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	protected Command createCommand(Object acceptedModel) {
		DEFeature feature = (DEFeature) acceptedModel;
		return new DEFeatureChangeVariationTypeCommand(feature, DEFeatureChangeVariationTypeCommand.VariationType.OPTIONAL);
	}
}
