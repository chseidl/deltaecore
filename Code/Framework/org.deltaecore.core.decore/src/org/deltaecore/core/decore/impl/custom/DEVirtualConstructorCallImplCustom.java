package org.deltaecore.core.decore.impl.custom;

import java.util.List;

import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEStructuralFeatureReference;
import org.deltaecore.core.decore.DEcorePackage;
import org.deltaecore.core.decore.impl.DEVirtualConstructorCallImpl;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.debug.DEDebug;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

//IMPORTANT: ensure that the value is NOT created every time getValue() is called as it changes the
//referencable object so that following statements have another object than previous ones.
//Only create a new object when the type of the constructor changes, which means that the value
//has to be instance of a different class. Otherwise, update the existing value.
public class DEVirtualConstructorCallImplCustom extends DEVirtualConstructorCallImpl {
	private EObject eValue;
	private boolean eValueTypeDirty;
	private boolean eValueContentDirty;
	
	public DEVirtualConstructorCallImplCustom() {
		eValueTypeDirty = true;
		eValueContentDirty = true;
		
		registerListeners();
	}
	
	private void registerListeners() {
		//Register listener for changes on "type" and "namedArguments" references to track changes relevant for eValue creation.
		//eAdapters().add(new EContentAdapter() {
		eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);

				Object feature = notification.getFeature();
				
				if (feature instanceof EStructuralFeature) {
					EStructuralFeature structuralFeature = (EStructuralFeature) feature;
					int featureId = structuralFeature.getFeatureID();
					
					if (featureId == DEcorePackage.DE_VIRTUAL_CONSTRUCTOR_CALL__TYPE) {
						DEMetaModelClassifierReference oldType = (DEMetaModelClassifierReference) notification.getOldValue();
						DEMetaModelClassifierReference newType = (DEMetaModelClassifierReference) notification.getNewValue();
						
						if (classifierChanged(oldType, newType)) {
							//New classifier means that the eValue has to be an instance of another type.
							//Hence, the value has to be recreated altogether (at least upon next access).
							eValueTypeDirty = true;
						}
					}
					
					//Some of the named values were changed.
					if (featureId == DEcorePackage.DE_VIRTUAL_CONSTRUCTOR_CALL__NAMED_ARGUMENTS) {
						//Some of the parameters were changed. Recreate contents of eValue upon next access.
						eValueContentDirty = false;
					}
				}
			}
		});
	}
	
	private static boolean classifierChanged(DEMetaModelClassifierReference oldType, DEMetaModelClassifierReference newType) {
		if (oldType == null && newType != null) {
			return true;
		}
		
		//Think this should never happen in practice.
		if (oldType != null && newType == null) {
			return true;
		}
		
		//Think this should never happen in practice.
		if (oldType == null && newType == null) {
			return false;
		}
		
		//Both types exist. Check if classifier changed.
		EClassifier oldClassifier = oldType.getClassifier();
		EClassifier newClassifier = newType.getClassifier();
		
		//Pointer equality intended.
		return (newClassifier != oldClassifier);
	}
	
	protected void refreshValueIfNecessary() {
		if (eValueTypeDirty) {
			createValue();
		}
		
		if (eValueContentDirty) {
			createValueContent();
		}
	}
	
	protected void createValue() {
		//Make sure that the reference to the old value is discarded in any case.
		eValue = null;
		
		//Build up the value from type and arguments
		DEMetaModelClassifierReference type = getType();
		EClassifier classifier = type.getClassifier();
		
		if (classifier == null || classifier.eIsProxy()) {
			DEDebug.println("Failed");
			return;
		}
		
		if (classifier instanceof EClass) {
			EClass eClass = (EClass) classifier;
			
			//Create instance of class
			DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(this);
//			DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(this);
			DEDeltaDialect dialect = block.getDeltaDialect();
			
			EFactory factory = dialect.getDomainFactory();
			eValue = factory.create(eClass);
		} else {
			System.err.println(getClass().getSimpleName() + " not implemented yet (Classifier: " + classifier + ").");
		}
		
		eValueTypeDirty = false;
		eValueContentDirty = true;
	}
	
	protected void createValueContent() {
		if (eValue == null) {
			return;
		}
		
		//Set attributes of instance according to values of arguments
		List<DEStructuralFeatureReference> structuralFeatureReferences = getNamedArguments();
		
		for (DEStructuralFeatureReference structuralFeatureReference : structuralFeatureReferences) {
			EStructuralFeature eStructuralFeature = structuralFeatureReference.getStructuralFeature();
			
			if (eStructuralFeature != null && !eStructuralFeature.eIsProxy()) {
				DEExpression2 argument = structuralFeatureReference.getExpression();
				Object value = argument.getValue();

				if (eStructuralFeature instanceof EAttribute) {
					eValue.eSet(eStructuralFeature, value);
				}
				
				if (eStructuralFeature instanceof EReference) {
					//TODO: No idea how to handle this in particular.
					//Special as it may have side effects (opposite references, change of contained elements of another object if it is a containment relation)
					eValue.eSet(eStructuralFeature, value);
				}
			}
		}
		
		eValueContentDirty = false;
	}
	
	@Override
	public Object getValue() {
		refreshValueIfNecessary();
		return eValue;
	}

	@Override
	public Class<?> getValueJavaClass() {
		DEType type = getType();
		
		if (type != null) {
			return type.getValueType();
		}
		
		return null;
	}

}

