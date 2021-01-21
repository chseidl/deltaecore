package org.deltaecore.core.decore.validation;

import org.deltaecore.core.decore.DEVirtualConstructorCall;
import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEVirtualConstructorCallTypeConstraint extends AbstractConstraint<DEVirtualConstructorCall> {

	@Override
	protected IStatus doValidate(DEVirtualConstructorCall virtualConstructorCall) {
		DEMetaModelClassifierReference type = virtualConstructorCall.getType();
		EClassifier classifier = type.getClassifier();
		
		if (classifier instanceof EClass) {
			return createSuccessStatus();
		}
		
		return createErrorStatus("Constructors are only supported for subclasses of EClass.", type); 
	}
}
