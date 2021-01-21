package org.deltaecore.feature.validation;

import org.deltaecore.feature.DEFeaturePackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.ui.IStartup;

import de.christophseidl.util.ecore.validation.adapter.ValidatorAdapter;

/**
 * This class registers the validations for the feature model so that they can be used within the graphical editor.
 */
public class DEFeatureValidatorRegistrationAtStartup implements IStartup {
	@Override
	public void earlyStartup() {
		EPackage ePackage = DEFeaturePackage.eINSTANCE;
		EValidator.Registry.INSTANCE.put(ePackage, new ValidatorAdapter());
	}
}
