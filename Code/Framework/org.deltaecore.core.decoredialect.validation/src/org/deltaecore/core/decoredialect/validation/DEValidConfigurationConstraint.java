package org.deltaecore.core.decoredialect.validation;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EPackage;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEValidConfigurationConstraint extends AbstractConstraint<DEDeltaDialect> {

	@Override
	protected IStatus doValidate(DEDeltaDialect deltaDialect) {
		EPackage domainPackage = deltaDialect.getDomainPackage();
		
		//Provide a hint for unresolved domain EPackage
		if (domainPackage == null || domainPackage.eIsProxy()) {
			String message = "Failed to resolve domain meta model. Supply the namespace URI of the ecore model as identifier and make sure the respective EPackages are available during development time. For local domain models (in the same workspace as this file), it may be necessary to register the EPackage manually by selecting \"Register EPackages\" from the context menu of the respective .ecore file.";
			
			return createErrorStatus(message, domainPackage);
		}
		
		//TODO: Disabled for the time being because check of class fails. the logic here is sound though.
		//If a custom domain model element identifier resolver is declared, it needs to resolve to a valid class.
//		DEJavaClassReference domainModelElementIdentifierResolverClassReference = deltaDialect.getDomainModelElementIdentifierResolverClassReference();
//		
//		if (domainModelElementIdentifierResolverClassReference != null) {
//			Class<?> domainModelElementIdentifierResolverClass = domainModelElementIdentifierResolverClassReference.getReferencedJavaClass();
//			
//			if (domainModelElementIdentifierResolverClass == null) {
//				String message = "Domain model element identifier resolver could not be resolved to Java class.";
//				return createFailureStatus(message, domainModelElementIdentifierResolverClassReference);
//			}
//			
//			if (!DEDomainModelElementIdentifierResolver.class.isAssignableFrom(domainModelElementIdentifierResolverClass)) {
//				//Specified class is not a subclass of DEDomainModelElementIdentifierResolver.
//				String message = "The specified domain model element identifier resolver is not a sub-class of " + DEDomainModelElementIdentifierResolver.class.getSimpleName() + ".";
//				return createFailureStatus(message, domainModelElementIdentifierResolverClassReference);
//			}
//		}
		
		return createSuccessStatus();
	}
}
