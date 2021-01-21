package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEVersionCreateCommand;
import org.deltaecore.feature.util.DEFeatureUtil;

public class DEVersionCreateAction extends DEAbstractVersionCreateAction {
	
	public static final String ID = DEVersionCreateAction.class.getCanonicalName();
	
	public DEVersionCreateAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Version";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreateVersion.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEFeature) {
			DEFeature feature = (DEFeature) selectedModel;
			
			if (!DEFeatureUtil.hasVersions(feature)) {
				return true;
			}
		}
		
		//NOTE: Intended not to call super as this should only be useable on features. 
		return false;
	}
	
	@Override
	protected DEVersionCreateCommand doCreateCommand(DEVersion version, Object acceptedModel) {
		return new DEVersionCreateCommand(version, acceptedModel);
	}
}
