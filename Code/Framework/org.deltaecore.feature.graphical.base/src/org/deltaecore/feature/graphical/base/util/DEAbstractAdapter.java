package org.deltaecore.feature.graphical.base.util;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.graphical.base.editparts.DEAbstractEditPartWithAdapter;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public abstract class DEAbstractAdapter implements Adapter {
	private DEAbstractEditPartWithAdapter editPart;
	
	public DEAbstractAdapter(DEAbstractEditPartWithAdapter editPart) {
		this.editPart = editPart;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		
		react(notification);
	}

	protected void react(Notification notification) {
		update();
		notifyFeatureModel();
	}
	
	protected void update() {
		//Refresh the model children
		editPart.refresh();
	}
	
	protected void notifyFeatureModel() {
		//Send a notification to the feature model.
		EObject model = editPart.getModel();
		EObject rootContainer = EcoreUtil.getRootContainer(model);
		
		if (rootContainer instanceof DEFeatureModel) {
			DEFeatureModel featureModel = (DEFeatureModel) rootContainer;
			
			fireDummyNotification(featureModel, model);
		}
	}
	
	protected void fireDummyNotification(EObject notified, EObject notifier) {
		//The concrete nature of the notification is irrelevant and, in this case, nonsense.
		Notification featureModelNotification = new ENotificationImpl((InternalEObject) notifier, 0, null, true, true);
		notified.eNotify(featureModelNotification);
	}
	
	@Override
	public Notifier getTarget() {
		return editPart.getModel();
	}

	@Override
	public void setTarget(Notifier newTarget) {
	}

	protected DEAbstractEditPartWithAdapter getEditPart() {
		return editPart;
	}
}
