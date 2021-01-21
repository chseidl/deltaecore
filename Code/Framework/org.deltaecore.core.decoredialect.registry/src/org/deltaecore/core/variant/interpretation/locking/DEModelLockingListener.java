package org.deltaecore.core.variant.interpretation.locking;

import org.deltaecore.debug.DEDebug;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

public class DEModelLockingListener extends EContentAdapter {
	private DEModelWriter modelWriter;
	
	public DEModelLockingListener(DEModelWriter modelWriter) {
		this.modelWriter = modelWriter;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);

//		DEDebug.println(">>> Model Access: " + notification);
		
		int eventType = notification.getEventType();

		//Disregard uncritical accesses.
		if (eventType == Notification.REMOVING_ADAPTER || eventType == Notification.RESOLVE) {
//			DEDebug.println(">>> Model Access is uncritical: " + notification);
			return;
		}
		
		if (eventType == Notification.ADD_MANY) {
			//TODO: Create multiple adds
		}
		
		if (eventType == Notification.REMOVE_MANY) {
			//TODO: Create multiple removes
		}
		
		if (eventType == Notification.MOVE) {
			//TODO: How to handle this?
		}

		//Check critical write accesses.
		if (eventType == Notification.ADD || eventType == Notification.REMOVE || eventType == Notification.SET || eventType == Notification.UNSET) {
			DEDebug.println(">>> Model Access is CRITICAL: " + notification);
			DEModelLocker.tryToPerformWriteAccess(notification);
			return;
		}
		
//		DEDebug.println(">>> Model Access is undetermined: " + notification);
		
		//No idea what event this is - deny just to be safe.
		DEModelLocker.denyWriteAccess();
	}

	public DEModelWriter getModelWriter() {
		return modelWriter;
	}

}
