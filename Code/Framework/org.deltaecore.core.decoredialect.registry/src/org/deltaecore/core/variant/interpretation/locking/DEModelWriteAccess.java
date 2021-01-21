package org.deltaecore.core.variant.interpretation.locking;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.christophseidl.util.ecore.EcoreReflectionUtil;

public class DEModelWriteAccess extends NotificationImpl {
	private EObject notifier;
	private EStructuralFeature structuralFeature;
	
	public DEModelWriteAccess(EObject notifier, int structuralFeatureId, int eventType, Object oldValue, Object newValue) {
		this(notifier, structuralFeatureId, eventType, oldValue, newValue, Notification.NO_INDEX);
	}

	public DEModelWriteAccess(EObject notifier, EStructuralFeature structuralFeature, int eventType, Object oldValue, Object newValue) {
		this(notifier, structuralFeature, eventType, oldValue, newValue, Notification.NO_INDEX);
	}
	
	public DEModelWriteAccess(EObject notifier, int structuralFeatureId, int eventType, Object oldValue, Object newValue, int position) {
		this(notifier, EcoreReflectionUtil.findStructuralFeature(notifier, structuralFeatureId), eventType, oldValue, newValue, position);
	}
	
	public DEModelWriteAccess(EObject notifier, EStructuralFeature structuralFeature, int eventType, Object oldValue, Object newValue, int position) {
		super(eventType, oldValue, newValue, position);
		
		this.notifier = notifier;
		this.structuralFeature = structuralFeature;
	}
	
	@Override
	public EObject getNotifier() {
		return notifier;
	}

	@Override
	public EStructuralFeature getFeature() {
		return structuralFeature;
	}

	public boolean permitsWrite(Notification notification) {
		if (getNotifier() != notification.getNotifier()) {
			return false;
		}
		
		if (getFeature() != notification.getFeature()) {
			return false;
		}
		
		if (getEventType() != notification.getEventType()) {
			return false;
		}
		
		if (getOldValue() != notification.getOldValue()) {
			return false;
		}
		
		if (getNewValue() != notification.getNewValue()) {
			return false;
		}
		
		if (getPosition() != notification.getPosition()) {
			return false;
		}
		
		//TODO: Some more
		return true;
	}
}
