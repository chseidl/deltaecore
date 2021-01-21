package org.deltaecore.feature.graphical.editor.action;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.action.DECommandAction;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.editor.commands.DEFeatureCreateCommand;
import org.deltaecore.feature.graphical.editor.factories.DEFeatureFactory;
import org.eclipse.gef.commands.Command;

public class DEFeatureCreateAction extends DECommandAction {
	
	public static final String ID = DEFeatureCreateAction.class.getCanonicalName();
	
	private static DEFeatureFactory featureFactory = new DEFeatureFactory();
	
	public DEFeatureCreateAction(DEGraphicalEditor editor) {
		super(editor);
	}

	@Override
	protected String createText() {
		return "Create Feature";
	}
	
	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionCreateFeature.png";
	}
	
	@Override
	protected boolean acceptsSelectedModel(Object selectedModel) {
		if (selectedModel instanceof DEFeature || selectedModel instanceof DEGroup) {
			return true;
		}
		
		if (selectedModel instanceof DEFeatureModel) {
			DEFeatureModel featureModel = (DEFeatureModel) selectedModel;
			DEFeature rootFeature = featureModel.getRootFeature();
			
			if (rootFeature == null) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected Command createCommand(Object acceptedModel) {
		DEFeature feature = featureFactory.getNewObject();
		return new DEFeatureCreateCommand(feature, acceptedModel);
	}

	@Override
	public void updateEnabledState() {
		// TODO Auto-generated method stub
		super.updateEnabledState();
	}
}
