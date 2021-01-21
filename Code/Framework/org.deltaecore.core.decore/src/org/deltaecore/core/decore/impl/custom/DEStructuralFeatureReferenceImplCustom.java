package org.deltaecore.core.decore.impl.custom;

import org.deltaecore.core.decore.impl.DEStructuralFeatureReferenceImpl;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DEStructuralFeatureReferenceImplCustom extends DEStructuralFeatureReferenceImpl {
	@Override
	public Class<?> getExpectedJavaClass() {
		EStructuralFeature structuralFeature = getStructuralFeature();
		EClassifier expectedType = structuralFeature.getEType();
		
		if (expectedType == null || expectedType.eIsProxy()) {
			//This may happen when an invalid classifier is specified and EMFText
			//creates a proxy for it instead of just not passing it.
			return null;
		}
		
		Class<?> expectedJavaType = expectedType.getInstanceClass();
		
		if (expectedJavaType == null) {
			//This may happen if the EPackage of the expectedType EClassifier is not loaded
			//with the runtime instance that knows about generated classes.
			return null;
		}
		
		//Internally, no primitive types are used.
		//If the EDataType of an attribute maps to a primitive
		//type, the check for subclass would fail. Hence, get the respective complex
		//wrapper type instead of the primitive type and use it for sub type checking.
		return DEDEcoreBaseUtil.boxPrimitiveTypeIfNecessary(expectedJavaType);
	}
}
