package org.deltaecore.feature.graphical.base.editparts;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEFeatureModelFigure;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.util.DEAbstractAdapter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;

public class DEFeatureModelEditPart extends DEAbstractEditPartWithAdapter {
	
	public DEFeatureModelEditPart(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected DEAbstractAdapter createAdapter(DEAbstractEditPartWithAdapter editPart) {
		return new DEAbstractAdapter(editPart) {
			@Override
			protected void react(Notification notification) {
				refresh();
				refreshVisualsRecursively(DEFeatureModelEditPart.this);
				super.react(notification);
			}
			
			@Override
			protected void notifyFeatureModel() {
				//This IS the feature model, no more notifications.
				//NOTE: Do not call super as it sends out a notification to the feature model,
				//which would cause an infinite loop.
			}
			
			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(DEFeatureModel.class);
			}
		};
	}
	

	@Override
	public void refresh() {
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		DEFeatureLayouterManager featureLayouterManager = graphicalEditor.getFeatureLayouterManager();
		featureLayouterManager.update();
		
		super.refresh();
	}
	
	protected static void refreshVisualsRecursively(EditPart editPart) {
		if (editPart instanceof DEAbstractEditPart) {
			DEAbstractEditPart abstractEditPart = (DEAbstractEditPart) editPart;
			abstractEditPart.refreshVisuals();
		}
		
		List<?> children = editPart.getChildren();
		
		for (Object child : children) {
			if (child instanceof EditPart) {
				EditPart childEditPart = (EditPart) child;
				refreshVisualsRecursively(childEditPart);
			}
		}
	}
	
	@Override
	protected IFigure createFigure() {
		DEFeatureModel featureModel = getModel();
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		return new DEFeatureModelFigure(featureModel, graphicalEditor);
	}

	@Override
	public DEFeatureModel getModel() {
		return (DEFeatureModel) super.getModel(); 
	}
	
	/**
	 * Called from outside. Responsible for building a list with all model elements. Gathers elements and handles also the initial layout step!
	 * 
	 * @return A list with all model elements.
	 */
	@Override
	protected List<Object> getModelChildren() {
		DEFeatureModel featureModel = getModel();
		List<Object> modelChildren = new ArrayList<Object>();

		traverseModel(modelChildren, featureModel);
		
		return modelChildren;
	}

	/**
	 * 
	 * @param modelChildren
	 * @param featureModel
	 * @return True if at least one model element (the root feature) is available.
	 */
	private boolean traverseModel(List<Object> modelChildren, DEFeatureModel featureModel) {
		modelChildren.clear();
		
		DEFeature rootFeature = featureModel.getRootFeature();

		if (rootFeature == null) {
			return false;
		}

		// get root feature
		modelChildren.add(rootFeature);

		// iterate the groups
		List<DEGroup> groups = rootFeature.getGroups();
		
		if (!groups.isEmpty()) {
			// get all groups (have to be listed before features to be drawn behind them)
			collectGroups(modelChildren, rootFeature);
			
			// get all features with versions
			collectFeatures(modelChildren, rootFeature.getGroups());
		}

		return true;
	}

	
	/**
	 * This method iterates the model elements in order to gather the features. The versions are added separately in the feature.
	 * 
	 * @param modelChildren
	 * @param groups
	 */
	private void collectFeatures(List<Object> modelChildren, List<DEGroup> groups) {
		for (DEGroup group : groups) {
			for (DEFeature feature : group.getFeatures()) {
				modelChildren.add(feature);
				collectFeatures(modelChildren, feature.getGroups());
			}
		}
	}
	
	/**
	 * This method iterates the tree in order to gather the group types
	 * 
	 * @param modelChildren
	 * @param rootFeature
	 */
	private void collectGroups(List<Object> modelChildren, DEFeature rootFeature) {
		for (DEGroup group : rootFeature.getGroups()) {
			//Need ALL groups here - even those that do not have a graphical representation (for marking their bounds).
			modelChildren.add(group);

			for (DEFeature currentFeature : group.getFeatures()) {
				collectGroups(modelChildren, currentFeature);
			}
		}
	}
}
