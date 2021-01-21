package org.deltaecore.core.variant.interpretation.locking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deltaecore.debug.DEDebug;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DEModelLocker {
	private static DEModelLocker instance;
	private Map<EObject, DEModelLockingListener> locks;
	private DEModelWriter activeWriter;
	
	private List<DEModelWriteAccess> scheduledWriteAccesses;
	
	private DEModelLocker() {
		locks = new HashMap<EObject, DEModelLockingListener>();
		activeWriter = null;
		scheduledWriteAccesses = new ArrayList<DEModelWriteAccess>();
	}
	
	private static DEModelLocker getInstance() {
		if (instance == null) {
			instance = new DEModelLocker();
		}
		
		return instance;
	}
	
	
	
	//TODO: Externalize
	private static boolean isRootElement(EObject object) {
		if (object == null) {
			return false;
		}
		
		return (object.eContainer() == null); 
	}
	
	
	
	public static void lockModel(EObject model, DEModelWriter writer) {
		getInstance().doLockModel(model, writer);
	}
	
	public static void lockModels(List<EObject> models, DEModelWriter writer) {
		for (EObject model : models) {
			getInstance().doLockModel(model, writer);
		}
	}
	
	public static void unlockModel(EObject model, DEModelWriter writer) {
		getInstance().doUnlockModel(model, writer);
	}
	
	public static void unlockModels(List<EObject> models, DEModelWriter writer) {
		for (EObject model : models) {
			getInstance().doUnlockModel(model, writer);
		}
	}
	
	public static void activateWriter(DEModelWriter writer) {
		getInstance().doActivateWriter(writer);
	}
	
	public static void deactivateWriter(DEModelWriter writer) {
		getInstance().doDeactivateWriter(writer);
	}

	
	protected static void denyWriteAccess() {
		denyWriteAccess(null);
	}
	
	protected static void denyWriteAccess(Notification notification) {
		denyWriteAccess(notification, null);
	}
	
	protected static void denyWriteAccess(Notification notification, DEModelWriteAccess expectedWriteAccess) {
		String message = ">>> Illegal write access detected";
		
		if (notification != null) {
			message += ":\n";
			message += "  was:                  " + notification;
		}
		
		if (expectedWriteAccess != null) {
			message += "\n";
			message += "  expected: " + expectedWriteAccess;
		}
		
		message += ".";
		
//		throw new UnsupportedOperation(message);
		DEDebug.println(message);	
	}
	
	public static void tryToPerformWriteAccess(Notification notification) {
		getInstance().doTryToPerformWriteAccess(notification);
	}
	
	public void doTryToPerformWriteAccess(Notification notification) {
		if (scheduledWriteAccesses.isEmpty()) {
			denyWriteAccess(notification);
			return;
		}
		
		DEModelWriteAccess nextScheduledWriteAccess = scheduledWriteAccesses.get(0);
		
		if (!nextScheduledWriteAccess.permitsWrite(notification)) {
			denyWriteAccess(notification, nextScheduledWriteAccess);
			return;
		}
		
		//Found a match - remove everything up to here (consuming scheduled optional write accesses passed while searching)
		scheduledWriteAccesses.remove(0);
		DEDebug.println("> Write access granted.");
	}
	
	protected void doLockModel(EObject model, DEModelWriter writer) {
		if (!isRootElement(model)) {
			throw new UnsupportedOperationException("Locking is only allowed on root elements of a model.");
		}
		
		DEModelLockingListener alreadyLockingListener = locks.get(model);
				
		if (alreadyLockingListener != null) {
			DEModelWriter previouslyLockingWriter = alreadyLockingListener.getModelWriter();
		
			if (previouslyLockingWriter == writer) {
				throw new UnsupportedOperationException("Double lock on \"" + model + "\" by same writer.");
			} else {
				throw new UnsupportedOperationException("Trying to lock already locked model \"" + model + "\".");
			}
		}
		
		//Register locking listener
		DEModelLockingListener lockingListener = new DEModelLockingListener(writer);
		model.eAdapters().add(lockingListener);
		
		locks.put(model, lockingListener);
	}
	
	protected void doUnlockModel(EObject model, DEModelWriter writer) {
		DEModelLockingListener previouslyLockingListener = locks.get(model);
		
		if (previouslyLockingListener == null) {
			throw new UnsupportedOperationException("Trying to unlock already unlocked model \"" + model + "\".");
		}

		//Unregister locking listener
		DEModelWriter lockingWriter = previouslyLockingListener.getModelWriter();
		model.eAdapters().remove(lockingWriter);
		
		locks.remove(model);
	}
	
	protected void doActivateWriter(DEModelWriter writer) {
		if (activeWriter != null) {
			throw new UnsupportedOperationException("Trying to activate second writer.");
		}
		
		activeWriter = writer;
	}
	
	protected void doDeactivateWriter(DEModelWriter writer) {
		if (activeWriter == null) {
			throw new UnsupportedOperationException("Trying to deactivate non-activated writer.");
		}
		
		if (activeWriter != writer) {
			throw new UnsupportedOperationException("Trying to deactivate wrong writer.");
		}
		
		activeWriter = null;
	}
	
	
	private static void schedule(DEModelWriteAccess modelWriteAccess) {
		getInstance().scheduledWriteAccesses.add(modelWriteAccess);
	}
	
	protected static void scheduleSetValueWriteAccess(EObject object, EStructuralFeature structuralFeature, Object oldValue, Object newValue) {
		DEModelWriteAccess modelWriteAccess = new DEModelWriteAccess(object, structuralFeature, Notification.SET, oldValue, newValue);
		schedule(modelWriteAccess);
	}
	
	protected static void scheduleUnsetValueWriteAccess(EObject object, EStructuralFeature structuralFeature) {
		//TODO:
		Object oldValue = null;
		DEModelWriteAccess modelWriteAccess = new DEModelWriteAccess(object, structuralFeature, Notification.UNSET, oldValue, null);
		schedule(modelWriteAccess);
	}
	
	protected static void scheduleAddValueWriteAccess(EObject object, EStructuralFeature structuralFeature, Object value, int position) {
		DEModelWriteAccess modelWriteAccess = new DEModelWriteAccess(object, structuralFeature, Notification.ADD, null, value, position);
		schedule(modelWriteAccess);
	}
	
	protected static void scheduleRemoveValueWriteAccess(EObject object, EStructuralFeature structuralFeature, Object value, int position) {
		//With remove, the value is passed as old value and new value is null.
		DEModelWriteAccess modelWriteAccess = new DEModelWriteAccess(object, structuralFeature, Notification.REMOVE, value, null, position);
		schedule(modelWriteAccess);
	}
}
