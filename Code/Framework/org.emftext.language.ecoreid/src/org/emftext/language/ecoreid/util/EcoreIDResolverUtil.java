package org.emftext.language.ecoreid.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.emftext.language.ecoreid.EcoreID;
import org.emftext.language.ecoreid.exception.AmbiguousIDException;
import org.emftext.language.ecoreid.exception.UnresolvableIDException;

public class EcoreIDResolverUtil {
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, ENamedElement resolutionContext) {
		return resolveEcoreID(ecoreID, resolutionContext, (List<EClass>) null);
	}
	
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, List<? extends ENamedElement> resolutionContexts) {
		return resolveEcoreID(ecoreID, resolutionContexts, (List<EClass>) null);
	}
	
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, ENamedElement resolutionContext, EClass acceptedType) {
		return resolveEcoreID(ecoreID, resolutionContext, Collections.singletonList(acceptedType));
	}
	
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, ENamedElement resolutionContext, List<EClass> acceptedTypes) {
		return resolveEcoreID(ecoreID, Collections.singletonList(resolutionContext), acceptedTypes);
	}
	
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, List<? extends ENamedElement> resolutionContexts, EClass acceptedType) {
		return resolveEcoreID(ecoreID, resolutionContexts, Collections.singletonList(acceptedType));
	}
	
	public static ENamedElement resolveEcoreID(EcoreID ecoreID, List<? extends ENamedElement> resolutionContexts, List<EClass> acceptedTypes) {
		List<ENamedElement> results = rawResolveEcoreID(ecoreID, resolutionContexts, acceptedTypes);
		
		//Evaluate
		if (results == null || results.isEmpty()) {
			throw new UnresolvableIDException(ecoreID);
		}
		
		if (results.size() > 1) {
			throw new AmbiguousIDException(ecoreID, results);
		}
		
		return results.get(0);
		
	}
	
	public static List<ENamedElement> rawResolveEcoreID(EcoreID ecoreID, List<? extends ENamedElement> resolutionContexts, List<EClass> acceptedTypes) {
		if (ecoreID == null) {
			return null;
		}
		
		List<String> qualifiedName = ecoreID.getNames();
		List<ENamedElement> results = new ArrayList<ENamedElement>();
		
		
		//Resolve
		for (ENamedElement resolutionContext : resolutionContexts) {
			//Regular resolution.
			results.addAll(resolveQualifiedName(qualifiedName, resolutionContext));
			
			
			//Special resolution when first package name is explicit.
			if (resolutionContext instanceof EPackage) {
				EPackage ePackage = (EPackage) resolutionContext;
				String ePackageName = ePackage.getName();
				
				String firstName = qualifiedName.get(0);
				
				if (firstName.equals(ePackageName)) {
					List<String> effectiveQualifiedName = qualifiedName.subList(1, qualifiedName.size());
					results.addAll(resolveQualifiedName(effectiveQualifiedName, resolutionContext));
				}
			}
		}
		
		
		//Purge
		Iterator<ENamedElement> iterator = results.iterator();
		
		while(iterator.hasNext()) {
			ENamedElement eNamedElement = iterator.next();
			
			//If its an operation, check that the parameters match (if required).
			if (eNamedElement instanceof EOperation) {
				EOperation eOperation = (EOperation) eNamedElement;
				
				if (!parameterListMatches(ecoreID, eOperation)) {
					//Leave this one intact (skip the removal).
					iterator.remove();
					continue;
				}
			}
			
			
			//Check explicitly accepted types.
			EClass eClass = eNamedElement.eClass();
			
			if (acceptedTypes != null && !isAcceptedType(eClass, acceptedTypes)) {
				iterator.remove();
				continue;
			}
		}
		
		return results;
	}
	
	private static boolean isAcceptedType(EClass eClass, List<EClass> acceptedTypes) {
		for (EClass acceptedType : acceptedTypes) {
			if (acceptedType == eClass) {
				return true;
			}
			
			if (acceptedType.isSuperTypeOf(eClass)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean parameterListMatches(EcoreID ecoreID, EOperation eOperation) {
		List<String> parameterTypeNames = ecoreID.getParameterTypeNames();
		List<EParameter> eParameters = eOperation.getEParameters();
		boolean explicitlyNoParametersSpecified = ecoreID.isExplicitlyNoParametersSpecified();
		
		if (parameterTypeNames.isEmpty()) {
			if (explicitlyNoParametersSpecified) {
				//Require that there are no parameters
				return eParameters.isEmpty();
			} else {
				//No requirements on parameters
				return true;
			}
		} else {
			//Require that there are as many parameters of the given type.
			if (parameterTypeNames.size() != eParameters.size()) {
				return false;
			}
			
			for (int i = 0; i < eParameters.size(); i++) {
				EParameter eParameter = eParameters.get(i);
				EClassifier type = eParameter.getEType();
				String actualParameterTypeName = type.getName();
				
				String expectedParameterTypeName = parameterTypeNames.get(i);
				
				if (!actualParameterTypeName.equals(expectedParameterTypeName)) {
					return false;
				}
			}
			
			return true;
		}
	}
	
	private static List<ENamedElement> resolveQualifiedName(List<String> names, ENamedElement resolutionContext) {
		List<ENamedElement> results = new ArrayList<ENamedElement>();
		
		if (names.isEmpty()) {
			//Package may be implicit (i.e., without giving any name)
			if (resolutionContext instanceof EPackage) {
				results.add(resolutionContext);
			}
			
			return results;
		}
		
		String currentName = names.get(0);
		
		if (currentName == null) {
			throw new InvalidParameterException();
		}
		
		List<String> remainingNames = names.subList(1, names.size());
		boolean isLastName = remainingNames.isEmpty();
		
		
		if (resolutionContext instanceof EPackage) {
			EPackage ePackage = (EPackage) resolutionContext;
			
			List<EPackage> eSubpackages = ePackage.getESubpackages();
			
			for (EPackage eSubpackage : eSubpackages) {
				String name = eSubpackage.getName();
				
				if (currentName.equals(name)) {
					results.add(eSubpackage);
				}
			}
			
			
			List<EClassifier> eClassifiers = ePackage.getEClassifiers();
			
			for (EClassifier eClassifier : eClassifiers) {
				String name = eClassifier.getName();
				
				if (currentName.equals(name)) {
					if (remainingNames.size() <= 1) {
						results.add(eClassifier);
					}
				}
			}
		}
		
		if (resolutionContext instanceof EClass) {
			EClass eClass = (EClass) resolutionContext;
			
			if (!isLastName) {
				throw new InvalidParameterException();
			}
			
			
			List<EStructuralFeature> eAllStructuralFeatures = eClass.getEAllStructuralFeatures();
			
			for (EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
				String name = eStructuralFeature.getName();
				
				if (currentName.equals(name)) {
					if (isLastName) {
						results.add(eStructuralFeature);
					}
				}
			}
			
			
			List<EOperation> eAllOperations = eClass.getEAllOperations();
			
			for (EOperation eOperation : eAllOperations) {
				String name = eOperation.getName();
				
				if (currentName.equals(name)) {
					if (isLastName) {
						results.add(eOperation);
					}
				}
			}
		}
		
		if (resolutionContext instanceof EEnum) {
			EEnum eEnum = (EEnum) resolutionContext;
			
			if (isLastName) {
				EEnumLiteral eEnumLiteral = eEnum.getEEnumLiteral(currentName);
				
				if (eEnumLiteral == null) {
					throw new InvalidParameterException();
				}
				
				results.add(eEnumLiteral);
			}
		}
		
		if (isLastName) {
			return results;
		} else {
			List<ENamedElement> completeResults = new ArrayList<ENamedElement>();
			
			for (ENamedElement namedElement : results) {
				List<ENamedElement> subResults = resolveQualifiedName(remainingNames, namedElement);
				completeResults.addAll(subResults);
			}
			
			return completeResults;
		}
	}
}