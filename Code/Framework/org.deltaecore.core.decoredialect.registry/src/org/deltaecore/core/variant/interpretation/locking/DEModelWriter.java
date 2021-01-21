package org.deltaecore.core.variant.interpretation.locking;

import java.security.InvalidParameterException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

public class DEModelWriter {
	private long accessorThreadId;
	
	//Don't want subclassing
	private DEModelWriter() {
		//Lock the creating thread ID for later comparison.
		//Concurrency caused by access from other threads can seriously
		//mess up the locking. Thus, prevent it altogether.
		Thread currentThread = Thread.currentThread();
		accessorThreadId = currentThread.getId();
	}
	
	public static DEModelWriter createInstance() {
		return new DEModelWriter();
	}

	public long getAccessorThreadId() {
		return accessorThreadId;
	}
	
	private static void checkNotNull(EObject object) {
		if (object == null) {
			throw new InvalidParameterException("Object must not be null.");
		}
	}
	
	private static EStructuralFeature getChangeableStructuralFeatureOrFail(EObject eObject, int structuralFeatureId) {
		checkNotNull(eObject);
		
		EClass eClass = eObject.eClass();
		EStructuralFeature structuralFeature = eClass.getEStructuralFeature(structuralFeatureId);
		
		//Semantics of structuralFeature.isUnsettable() is unclear. Ecore meta model defines eSuperTypes of
		//EClass as unsettable even though values can be added and removed.
//		if (!structuralFeature.isChangeable() || structuralFeature.isUnsettable()) {
		if (!structuralFeature.isChangeable()) {
			throw new InvalidParameterException("Structural feature is unchangeable.");
		}
		
		return structuralFeature;
	}
	
	private static EList<?> getManyValueOrFail(EObject object, EStructuralFeature structuralFeature) {
		if (!structuralFeature.isMany()) {
			throw new InvalidParameterException("Structural feature is not many.");
		}
		
		Object currentValue = object.eGet(structuralFeature);
		
		if (!(currentValue instanceof EList<?>)) {
			throw new InvalidParameterException("Value of many structural feature is not a collection.");
		}
		
		return (EList<?>) currentValue;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> EList<T> getManyValueOrFail(EObject object, EStructuralFeature structuralFeature, T typeIdentifier) {
		return (EList<T>) getManyValueOrFail(object, structuralFeature);
	}
	
	
	public void setValue(EObject object, int structuralFeatureId, Object value) {
		EStructuralFeature structuralFeature = getChangeableStructuralFeatureOrFail(object, structuralFeatureId);
		setValue(object, structuralFeature, value);
	}
	
	public void setValue(EObject object, EStructuralFeature structuralFeature, Object value) {	
		DEModelLocker.activateWriter(this);
		
		if (structuralFeature.isMany()) {
			throw new InvalidParameterException("Called set value but structural feature refers to a collection.");
		}

		//References can only go to EObjects
		if (structuralFeature instanceof EReference) {
			if (value != null && !(value instanceof EObject)) {
				throw new InvalidParameterException("Referenced value is not an EObject (" + value + ").");
			}
		}
		
		//TODO: Assess full impact
		Object oldValue = object.eGet(structuralFeature);
		DEModelLocker.scheduleSetValueWriteAccess(object, structuralFeature, oldValue, value);
		object.eSet(structuralFeature, value);
		
		DEModelLocker.deactivateWriter(this);
	}
	
	public void unsetValue(EObject object, int structuralFeatureId) {
		EStructuralFeature structuralFeature = getChangeableStructuralFeatureOrFail(object, structuralFeatureId);
		unsetValue(object, structuralFeature);
	}
	
	public void unsetValue(EObject object, EStructuralFeature structuralFeature) {
		DEModelLocker.activateWriter(this);
		
		//TODO: Assess full impact
		DEModelLocker.scheduleUnsetValueWriteAccess(object, structuralFeature);
		object.eUnset(structuralFeature);
		
		DEModelLocker.deactivateWriter(this);
	}
	
	//Done
	public <T> void addValue(EObject container, int structuralFeatureId, T value) {
		EStructuralFeature structuralFeature = getChangeableStructuralFeatureOrFail(container, structuralFeatureId);
		addValue(container, structuralFeature, value);
	}
	
	//TODO: Partially untested and sequence is unclear.
	public <T> void addValue(EObject container, EStructuralFeature structuralFeature, T value) {
		//Insert at index null appends items.
		insertValue(container, structuralFeature, value, null);
	}
	
	
	//Done
	public <T> void insertValue(EObject container, int structuralFeatureId, T value, Integer index) {
		EStructuralFeature structuralFeature = getChangeableStructuralFeatureOrFail(container, structuralFeatureId);
		insertValue(container, structuralFeature, value, index);
	}
	
	//TODO: Partially untested and sequence is unclear.
	public <T> void insertValue(EObject container, EStructuralFeature structuralFeature, T value, Integer index) {
		DEModelLocker.activateWriter(this);
		
		try {
			EList<T> currentListValue = getManyValueOrFail(container, structuralFeature, value);
	
			//EObjects may be contained in a containment EReference, which they are removed from
			//upon adding them to a new containment reference.
			if (value instanceof EObject) {
				EObject valueObject = (EObject) value;
				scheduleRemoveFromContainment(valueObject);
			}
			
			
			//If the target structural feature is single and a previous value exists, it will be removed.
			//TODO: Untested
			if (!FeatureMapUtil.isMany(container, structuralFeature)) {
				Object previousValue = container.eGet(structuralFeature);
				
				scheduleRemove(container, container, structuralFeature, previousValue);
			}
			
			if (index == null) {
				//Append to list of nothing else specified.
				index = currentListValue.size();
			}
			
			DEModelLocker.scheduleAddValueWriteAccess(container, structuralFeature, value, index);
			currentListValue.add(value);
		} finally {
			DEModelLocker.deactivateWriter(this);
		}
	}
	
	
	
	
	//Done
	public void removeValue(EObject container, int structuralFeatureId, Object value) {
		EStructuralFeature structuralFeature = getChangeableStructuralFeatureOrFail(container, structuralFeatureId);
		removeValue(container, structuralFeature, value);
	}
	
	//TODO: Assess full impact
	public void removeValue(EObject container, EStructuralFeature structuralFeature, Object value) {
		DEModelLocker.activateWriter(this);
		
		try {
			EList<?> currentListValue = getManyValueOrFail(container, structuralFeature);
			
			//Schedule remove
			scheduleRemove(container, container, structuralFeature, value);
			
			//Perform remove
			currentListValue.remove(value);
			//Remove on opposite reference is performed automatically.
		} finally {
			DEModelLocker.deactivateWriter(this);
		}
	}
	
	//Done
	public void detach(EObject objectToRemove) {
		DEModelLocker.activateWriter(this);
		
		try {
			EObject container = objectToRemove.eContainer();
			
			if (container != null) {
				//Schedule remove
				scheduleRemoveFromContainment(objectToRemove);
				
				EReference containmentReference = objectToRemove.eContainmentFeature();
				
				//Perform remove
				if (FeatureMapUtil.isMany(container, containmentReference)) {
					EList<?> listContainer = (EList<?>) container.eGet(containmentReference);
					
					listContainer.remove(objectToRemove);
				} else {
					container.eUnset(containmentReference);
				}
				
				//Remove on opposite reference is performed automatically.
			}
	
			//In case it is a root level object, remove from resource.
			InternalEObject internalObjectToRemove = (InternalEObject) objectToRemove;
			Resource directResource = internalObjectToRemove.eDirectResource();
			if (directResource != null) {
				directResource.getContents().remove(internalObjectToRemove);
			}
		} finally {
			DEModelLocker.deactivateWriter(this);
		}
	}
	
	
	
	private void scheduleRemoveFromContainment(EObject valueToRemove) {
		EObject container = valueToRemove.eContainer();
		EReference containmentReference = valueToRemove.eContainmentFeature();
		
		scheduleRemove(container, container, containmentReference, valueToRemove);
	}
	
	/**
	 * Schedule a remove operation for the specified value and (if applicable) for the opposite reference of the specified reference.
	 * 
	 * @param notifier
	 * @param containerHoldingStructuralFeatureValues
	 * @param structuralFeature
	 * @param valueToRemove
	 */
	private void scheduleRemove(EObject notifier, EObject containerHoldingStructuralFeatureValues, EStructuralFeature structuralFeature, Object valueToRemove) {
		if (structuralFeature == null) {
			return;
		}
		
		//Schedule removes for containment and its opposite reference.
		//Opposite value seems to be removed first
		
		//Check for opposite reference
		//This depends on the EStructuralFeature being an EReference
		if (structuralFeature instanceof EReference) {
			EReference reference = (EReference) structuralFeature;
			//EReferences contain EObjects. Thus, value is really (at least) an EObject.
			EObject valueObjectToRemove = (EObject) valueToRemove;
			//Opposite reference
			EReference oppositeReference = reference.getEOpposite();

			if (oppositeReference != null) {
				//If it can contain a reference, the opposite value is at least an EObject.
				EObject oppositeValueObject = (EObject) valueObjectToRemove.eGet(oppositeReference);
				doScheduleRemove(valueObjectToRemove, containerHoldingStructuralFeatureValues, oppositeReference, oppositeValueObject);
			}
		}
		
		//This is agnostic of whether the structural feature is an EAttribute or an EReference.
		doScheduleRemove(notifier, containerHoldingStructuralFeatureValues, structuralFeature, valueToRemove);
	}
	
	private void doScheduleRemove(EObject notifier, EObject containerHoldingStructuralFeatureValues, EStructuralFeature structuralFeature, Object valueToRemove) {
		if (structuralFeature == null) {
			return;
		}
		
		//This is agnostic of whether the structural feature is an EAttribute or an EReference.
		if (FeatureMapUtil.isMany(containerHoldingStructuralFeatureValues, structuralFeature)) {
			EList<?> listContainer = (EList<?>) containerHoldingStructuralFeatureValues.eGet(structuralFeature);
			int index = listContainer.indexOf(valueToRemove);
			DEModelLocker.scheduleRemoveValueWriteAccess(notifier, structuralFeature, valueToRemove, index);
		} else {
			// For some reason, remove operations on single values seem to prompt a set operation (not an unset).
			// TODO: May be another default value.
			// structuralFeature.getDefaultValue()
			Object newValue = null;
			DEModelLocker.scheduleSetValueWriteAccess(notifier, structuralFeature, valueToRemove, newValue);
		}
	}
}
