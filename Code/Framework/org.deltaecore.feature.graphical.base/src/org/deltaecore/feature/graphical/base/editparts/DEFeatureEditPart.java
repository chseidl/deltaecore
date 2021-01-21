package org.deltaecore.feature.graphical.base.editparts;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEFeatureFigure;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionLayouterManager;
import org.deltaecore.feature.graphical.base.util.DEAbstractAdapter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;

public class DEFeatureEditPart extends DEAbstractEditPartWithAdapter {
	public DEFeatureEditPart(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected DEAbstractAdapter createAdapter(DEAbstractEditPartWithAdapter editPart) {
		return new DEAbstractAdapter(editPart) {
			
			@Override
			protected void react(Notification notification) {
				refresh();
//				refreshChildren();
				
				super.react(notification);
			}
			
			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(DEFeature.class);
			}
		};
	}
	
	@Override
	public DEFeature getModel() {
		return (DEFeature) super.getModel();
	}
	
	@Override
	protected IFigure createFigure() {
		DEFeature feature = getModel();
		DEGraphicalEditor graphicalEditor = (DEGraphicalEditor) getGraphicalEditor();
		return new DEFeatureFigure(feature, graphicalEditor);
	}

	@Override
	public DEFeatureFigure getFigure() {
		return (DEFeatureFigure) super.getFigure();
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<DEVersion> children = new ArrayList<DEVersion>();
		
		DEFeature feature = getModel();
		List<DEVersion> versions = feature.getVersions();
		children.addAll(versions);
		
		return children;
	}
	
	@Override
	public void refreshVisuals() {
		DEFeatureFigure figure = (DEFeatureFigure) getFigure();
		figure.update();
	}

	@Override
	protected void refreshChildren() {
		DEFeature feature = getModel();
		DEVersionLayouterManager.updateLayouter(feature);
		
		super.refreshChildren();
	}
}
