package org.deltaecore.core.decorebase.impl.custom;

import org.deltaecore.core.decorebase.impl.DEMetaModelClassifierReferenceImpl;
import org.eclipse.emf.ecore.EClassifier;

public class DEMetaModelClassifierReferenceImplCustom extends DEMetaModelClassifierReferenceImpl {

	@Override
	public Class<?> getValueType() {
		EClassifier classifier = getClassifier();
		
		if (classifier == null) {
			return null;
		}
		
		return classifier.getInstanceClass();
	}
}
