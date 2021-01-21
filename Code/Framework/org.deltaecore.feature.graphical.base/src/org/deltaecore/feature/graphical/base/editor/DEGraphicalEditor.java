package org.deltaecore.feature.graphical.base.editor;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;
import org.deltaecore.feature.graphical.base.action.DEExportImageAction;
import org.deltaecore.feature.graphical.base.editparts.DERootEditPart;
import org.deltaecore.feature.graphical.base.factories.DEEditPartFactory;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.gef.ui.actions.ActionRegistry;

import de.christophseidl.util.gef.editor.EcoreGraphicalEditor;

public abstract class DEGraphicalEditor extends EcoreGraphicalEditor<DEFeatureModel> {
	private static DEGraphicalEditorTheme theme = new DEGraphicalEditorTheme();

	public static DEGraphicalEditorTheme getTheme() {
		return theme;
	}
	
	private DEFeatureLayouterManager featureLayouterManager;
	private DEConstraintModel constraintModel;
	
	@Override
	protected void setDataModel(DEFeatureModel featureModel) {
		featureLayouterManager = new DEFeatureLayouterManager(featureModel);
		constraintModel = DEConstraintIOUtil.loadAccompanyingConstraintModel(featureModel);
		super.setDataModel(featureModel);
	}
	
	@Override
	protected abstract DEGraphicalViewer doCreateGraphicalViewer();
	
	@Override
	protected void registerActions(ActionRegistry actionRegistry) {
		super.registerActions(actionRegistry);
		
		actionRegistry.registerAction(new DEExportImageAction(this));
	}
	
	@Override
	protected DERootEditPart createRootEditPart() {
		return new DERootEditPart();
	}
	
	@Override
	protected DEEditPartFactory createEditPartFactory() {
		return new DEEditPartFactory(this);
	}
	
	
	public DEGraphicalViewer getViewer() {
		return (DEGraphicalViewer) super.getGraphicalViewer();
	}
	
	public DEFeatureModel getFeatureModel() {
		return getDataModel();
	}
	
	public DEConstraintModel getConstraintModel() {
		return constraintModel;
	}
	
	public DEFeatureLayouterManager getFeatureLayouterManager() {
		return featureLayouterManager;
	}
	
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	public boolean isSaveAsAllowed() {
//		return true;
//	}
//	
//	@Override
//	public void doSaveAs() {
//		Resource resource = getResource();
//		IContainer container = EcoreResolverUtil.resolveContainerFromResource(resource);
//		
//		IFile saveAsFile = container.getFile(new Path("Dummy.defeaturemodel"));
//		
//		Resource saveAsResource = EcoreIOUtil.copyResourceTo(resource, saveAsFile);
//		
//		setResource(saveAsResource);
//		performSave();
//	}
}
