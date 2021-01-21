package org.deltaecore.core.decoredialect.util;

import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter;
import org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DEDEcoreDialectResolverUtil {
	public static EAttribute resolveAttributeInModelElementWithAttributeParameter(String attributeName, DEModelElementWithAttributeParameter modelElementWithAttributeParameter) {
		DEMetaModelClassifierReference type = modelElementWithAttributeParameter.getType();
		EStructuralFeature structuralFeature = DEDEcoreBaseUtil.resolveStructuralFeatureInMetaModelClassifierReference(attributeName, type);
		
		if (structuralFeature instanceof EAttribute) {
			return (EAttribute) structuralFeature;
		}
		
		return null;
	}
	
	public static EReference resolveReferenceInModelElementWithReferenceParameter(String referenceName, DEModelElementWithReferenceParameter modelElementWithReferenceParameter) {
		DEMetaModelClassifierReference type = modelElementWithReferenceParameter.getType();
		EClassifier eClassifier = type.getClassifier();
		
		if (eClassifier instanceof EClass) {
			EClass eClass = (EClass) eClassifier;
			EStructuralFeature structuralFeature = eClass.getEStructuralFeature(referenceName);
			
			if (structuralFeature instanceof EReference) {
				return (EReference) structuralFeature;
			}
		}
			
		return null;
	}
	
	//TODO: Migrate, probably obsolete as there is no more guessing as to which reference could be targeted
//	public static List<EReference> resolveContainingReferenceCandidates(DEContainingReferenceDeltaOperationParameter containingReferenceDeltaOperationParameter) {
//		DEAddingDeltaOperationDefinition addDeltaOperationDefinition = containingReferenceDeltaOperationParameter.getDeltaOperationDefinition();
//		DENamedParameter objectParameter = addDeltaOperationDefinition.getObject();
//		DEType rawTargetType = objectParameter.getType();
//		
//		if (rawTargetType instanceof DEMetaModelClassifierReference) {
//			DEMetaModelClassifierReference sourceType = containingReferenceDeltaOperationParameter.getType();
//			DEMetaModelClassifierReference targetType = (DEMetaModelClassifierReference) rawTargetType;
//			
//			EClassifier sourceEClassifier = sourceType.getClassifier();
//			EClassifier targetEClassifier = targetType.getClassifier();
//	
//			if (sourceEClassifier instanceof EClass && targetEClassifier instanceof EClass) {
//				EClass sourceEClass = (EClass) sourceEClassifier;
//				EClass targetEClass = (EClass) targetEClassifier;
//				
//				return EcoreReflectionUtil.findContainmentReferencesFromEClassToEClass(sourceEClass, targetEClass);
//			}
//		}
//		
//		return new ArrayList<EReference>();
//	}
	
}
