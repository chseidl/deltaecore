package org.deltaecore.feature.graphical.editor.action;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureCreateCommand;
import org.deltaecore.feature.graphical.editor.factories.DEFeatureFactory;
import org.eclipse.gef.commands.Command;

public class DEFeatureCreateInNewGroupAction extends DECommandAction {
	
	public static final String ID = DEFeatureCreateInNewGroupAction.class.getCanonicalName();
	
	private static DEFeatureFactory featureFactory = new DEFeatureFactory();
	
	public DEFeatureCreateInNewGroupAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Feature In New Group";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreateFeatureInNewGroup.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEFeature) {
			DEFeature feature = (DEFeature) selectedModel;
			
			List<DEGroup> groups = feature.getGroups();
			
			if (!groups.isEmpty()) {
				return true;
			}
		}
		
		if (selectedModel instanceof DEGroup) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected Command createCommand(Object acceptedModel) {
		DEFeature feature = featureFactory.getNewObject();
		return new DEFeatureCreateCommand(feature, acceptedModel, true);
	}
}
