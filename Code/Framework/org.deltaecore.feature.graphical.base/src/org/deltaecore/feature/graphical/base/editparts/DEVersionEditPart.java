package org.deltaecore.feature.graphical.base.editparts;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeaturePackage;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.DEVersionFigure;
import org.deltaecore.feature.graphical.base.util.DEAbstractAdapter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;

public class DEVersionEditPart extends DEAbstractEditPartWithAdapter {
	public DEVersionEditPart(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
	}

	@Override
	protected DEAbstractAdapter createAdapter(DEAbstractEditPartWithAdapter editPart) {
		return new DEAbstractAdapter(editPart) {
			
			@Override
			protected void react(Notification notification) {
				if (versionNumberChanged(notification)) {
					//Notify the parent feature of changes, e.g., when a name is changed that extends the version area.
					DEVersion version = getModel();
					DEFeature feature = version.getFeature();
					fireDummyNotification(feature, version);
				}
				
				super.react(notification);
			}
			
			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(DEVersion.class);
			}
		};
	}

	private boolean versionNumberChanged(Notification notification) {
		int eventType = notification.getEventType();
		
		if (eventType == Notification.SET || eventType == Notification.UNSET) {
			if (notification.getFeature() == DEFeaturePackage.eINSTANCE.getDEVersion_Number()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public DEVersion getModel() {
		return (DEVersion) super.getModel();
	}

	@Override
	public DEVersionFigure getFigure() {
		return (DEVersionFigure) super.getFigure();
	}

	@Override
	protected IFigure createFigure() {
		DEVersion version = getModel();
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		return new DEVersionFigure(version, graphicalEditor);
	}

	@Override
	public void refreshVisuals() {
		DEVersionFigure figure = getFigure();
		figure.update();
	}
}
