package org.eclipse.emf.ecore.delta;

import java.util.List;
import java.util.Map.Entry;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class EcoreDeltaDialectInterpreter extends EcoreAbstractDeltaDialectInterpreter {
//	private static final EcoreFactory factory = EcoreFactory.eINSTANCE;

//	@Override
//	protected boolean interpretSetUniqueMetaModelURI(DEModelWriter modelWriter, EPackage p) {
//		String baseURI = p.getNsURI();
//		Random random = new Random();
//		int salt = random.nextInt();
//		
//		String uri = baseURI + "/" + salt;
//		p.setNsURI(uri);
//		
//		return true;
//	}
	
	protected static class EAnnotationDetailsEntry extends EStringToStringMapEntryImpl {
		public EAnnotationDetailsEntry(String key, String value) {
			super.key = key;
			setValue(value);
		}
	}
	
//	@Override
//	protected boolean interpretRemoveESuperType(DEModelWriter modelWriter, EClass eClass, EClass eSuperClass) {
//		List<EClass> eSuperTypes = eClass.getESuperTypes();
//		eSuperTypes.remove(eSuperClass);
//		
//		return true;
//	}

//	@Override
//	protected boolean interpretSetEReferenceMultiplicity(DEModelWriter modelWriter, EReference eReference, Integer lowerBound, Integer upperBound) {
//		eReference.setLowerBound(lowerBound);
//		eReference.setUpperBound(upperBound);
//		
//		return true;
//	}
//
//	@Override
//	protected boolean interpretAddEAttributeOld(DEModelWriter modelWriter, String name, EClassifier type, EClass containingEClass) {
//		EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
//		
//		eAttribute.setName(name);
//		eAttribute.setEType(type);
//		eAttribute.setLowerBound(1);
//		eAttribute.setUpperBound(1);
//		
//		List<EStructuralFeature> eStructuralFeatures = containingEClass.getEStructuralFeatures();
//		eStructuralFeatures.add(eAttribute);
//		
//		
//		return true;
//	}

	@Override
	protected boolean interpretSetEOperationImplementation(DEModelWriter modelWriter, EOperation eOperation, String implementation) {
		//EAnnotation
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource("http://www.eclipse.org/emf/2002/GenModel");
		
		//DetailsEntry
		Entry<String, String> detailsEntry = new EAnnotationDetailsEntry("body", implementation);
		
		EMap<String, String> details = annotation.getDetails();
		details.add(detailsEntry);
		
		replaceEAnnotation(annotation, eOperation);
		
		return true;
	}

	private static void replaceEAnnotation(EAnnotation eAnnotation, EModelElement eModelElement) {
		String source = eAnnotation.getSource();
		List<EAnnotation> annotations = eModelElement.getEAnnotations();
		
		//See if there is already an annotation and remove it if so.
		EAnnotation existingAnnotation = eModelElement.getEAnnotation(source);
		
		if (existingAnnotation != null) {
			annotations.remove(existingAnnotation);
		}
		
		annotations.add(eAnnotation);
	}
	
//	@Override
//	protected boolean interpretAddEEnumOld(DEModelWriter modelWriter, String name, EPackage ePackage) {
//		EEnum eEnum = factory.createEEnum();
//		eEnum.setName(name);
//		
//		List<EClassifier> eClassifiers = ePackage.getEClassifiers();
//		eClassifiers.add(eEnum);
//		
//		return true;
//	}
//
//	@Override
//	protected boolean interpretAddEEnumLiteralOld(DEModelWriter modelWriter, String literalName, EEnum eEnum) {
//		return interpretAddEEnumLiteralOld2(modelWriter, literalName, literalName, eEnum);
//	}
//
//	@Override
//	protected boolean interpretAddEEnumLiteralOld2(DEModelWriter modelWriter, String literalName, String literal, EEnum eEnum) {
//		EEnumLiteral eEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
//		
//		eEnumLiteral.setName(literalName);
//		eEnumLiteral.setLiteral(literal);
//		
//		int value = createNextEEnumLiteralValue(eEnum);
//		eEnumLiteral.setValue(value);
//
//		List<EEnumLiteral> eLiterals = eEnum.getELiterals();
//		
//		eLiterals.add(eEnumLiteral);
//		
//		return true;
//	}

//	private static int createNextEEnumLiteralValue(EEnum eEnum) {
//		Integer highestValue = null;
//		List<EEnumLiteral> eLiterals = eEnum.getELiterals();
//		
//		for (EEnumLiteral eExistingEnumLiteral : eLiterals) {
//			int currentValue = eExistingEnumLiteral.getValue();
//			
//			if (highestValue == null || currentValue > highestValue) {
//				highestValue = currentValue;
//			}
//		}
//		
//		if (highestValue == null) {
//			return 0;
//		}
//		
//		return highestValue + 1;
//	}

//	@Override
//	protected boolean interpretCreateEClass(DEModelWriter modelWriter, String name, EPackage ePackage) {
//		EClass eClass = factory.createEClass();
//		eClass.setName(name);
//		List<EClassifier> eClassifiers = ePackage.getEClassifiers();
//		eClassifiers.add(eClass);
//		
//		return true;
//	}
//
//	@Override
//	protected boolean interpretCreateEReference(DEModelWriter modelWriter, EClass eFromClass, EClass eToClass, String name, Integer lowerBound, Integer upperBound, Boolean containment) {
//		EReference eReference = factory.createEReference();
//		
//		eReference.setEType(eToClass);
//		eReference.setName(name);
//		eReference.setLowerBound(lowerBound);
//		eReference.setUpperBound(upperBound);
//		eReference.setContainment(containment);
//		
//		List<EStructuralFeature> eStructuralFeatures = eFromClass.getEStructuralFeatures();
//		eStructuralFeatures.add(eReference);
//		
//		return true;
//	}

	@Override
	protected boolean interpretMakeEReferencesOpposite(DEModelWriter modelWriter, EReference reference1, EReference reference2) {
		//TODO: Really both ways needed?
		reference1.setEOpposite(reference2);
		reference2.setEOpposite(reference1);
		
		return true;
	}

	@Override
	protected boolean interpretSetEClassifierPackage(DEModelWriter modelWriter, EPackage ePackage, EClassifier eClassifier) {
		//NOTE: Has to be a custom function if specified in this direction as
		//EClassifier.ePackage is an unchangeable structural feature.
		List<EClassifier> eClassifiers = ePackage.getEClassifiers();
		eClassifiers.add(eClassifier);
		
		return true;
	}
}