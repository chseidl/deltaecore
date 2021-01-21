package org.deltaecore.core.generation.dialect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decorebase.DEcoreBaseFactory;
import org.deltaecore.core.decoredialect.DEAddDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDetachDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEInsertDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEModelElementParameter;
import org.deltaecore.core.decoredialect.DEModelElementWithAttributeParameter;
import org.deltaecore.core.decoredialect.DEModelElementWithReferenceParameter;
import org.deltaecore.core.decoredialect.DEModifyDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEMultiValuedReferenceDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DENamedParameter;
import org.deltaecore.core.decoredialect.DERemoveDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DESetDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEStandardDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEUnsetDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEcoreDialectFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import de.christophseidl.util.ecore.EcoreResolverUtil;
import de.christophseidl.util.java.StringUtil;


//TODO: Migrate, complete restructuring to use new generation methods

//TODO: Maybe use delta operation name builder as well to avoid duplicate operation names

//TODO: Declaring class might not be the class we're looking after when abstract

public class DEDeltaDialectCreatorUtil {
	
	//NOTE: Used by delta dialect generation of David Wille.
	/**
	 * Create an empty delta dialect with a proper namespace uri set.
	 * 
	 * @return The empty delta dialect.
	 */
	public static DEDeltaDialect createDeltaDialect(String namespaceURI) {
		EPackage domainPackage = EcoreResolverUtil.resolveEPackageFromRegistry(namespaceURI);
		
		return createDeltaDialect(domainPackage);
	}
	
	//NOTE: Used by delta dialect generation of David Wille.
	/**
	 * Create an empty delta dialect with a proper namespace uri set.
	 * 
	 * @return The empty delta dialect.
	 */
	public static DEDeltaDialect createDeltaDialect(EPackage domainPackage) {
		DEDeltaDialect deltaDialect = DEcoreDialectFactory.eINSTANCE.createDEDeltaDialect();
		
		deltaDialect.setDomainPackage(domainPackage);
		
		return deltaDialect;
	}
	
	public static List<DEStandardDeltaOperationDefinition> createStandardDeltaOperationDefinitions(EPackage metaModel) {
		List<DEStandardDeltaOperationDefinition> standardDeltaOperationDefinitions = new ArrayList<DEStandardDeltaOperationDefinition>();

		if (metaModel != null) {
			//Create standard delta operation definitions
			standardDeltaOperationDefinitions.addAll(createSetDeltaOperationDefinitions(metaModel));
			standardDeltaOperationDefinitions.addAll(createUnsetDeltaOperationDefinitions(metaModel));
			
			standardDeltaOperationDefinitions.addAll(createAddDeltaOperationDefinitions(metaModel));
			standardDeltaOperationDefinitions.addAll(createInsertDeltaOperationDefinitions(metaModel));
			standardDeltaOperationDefinitions.addAll(createRemoveDeltaOperationDefinitions(metaModel));
			
			standardDeltaOperationDefinitions.addAll(createModifyDeltaOperationDefinitions(metaModel));
			
			standardDeltaOperationDefinitions.addAll(createDetachDeltaOperationDefinitions(metaModel));
		}
		
		return standardDeltaOperationDefinitions;
	}
	
	
	public static List<DESetDeltaOperationDefinition> createSetDeltaOperationDefinitions(EPackage metaModel) {
		List<DESetDeltaOperationDefinition> operationDefinitions = new ArrayList<DESetDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, false, null);
		
		for (EReference eReference : eReferences) {
			DESetDeltaOperationDefinition operationDefinition = createSetDeltaOperationDefinition(eReference);
			operationDefinitions.add(operationDefinition);
		}
		
		return operationDefinitions;
	}
	
	public static List<DEUnsetDeltaOperationDefinition> createUnsetDeltaOperationDefinitions(EPackage metaModel) {
		List<DEUnsetDeltaOperationDefinition> operationDefinitions = new ArrayList<DEUnsetDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, false, null);
		
		for (EReference eReference : eReferences) {
			DEUnsetDeltaOperationDefinition operationDefinition = createUnsetDeltaOperationDefinition(eReference);
			operationDefinitions.add(operationDefinition);
		}
		
		return operationDefinitions;
	}
	
	
	public static List<DEAddDeltaOperationDefinition> createAddDeltaOperationDefinitions(EPackage metaModel) {
		List<DEAddDeltaOperationDefinition> operationDefinitions = new ArrayList<DEAddDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, true, null);
		
		for (EReference eReference : eReferences) {
			DEAddDeltaOperationDefinition operationDefinition = createAddDeltaOperationDefinition(eReference);
			operationDefinitions.add(operationDefinition);
		}
		
		return operationDefinitions;
	}
	
	public static List<DEInsertDeltaOperationDefinition> createInsertDeltaOperationDefinitions(EPackage metaModel) {
		List<DEInsertDeltaOperationDefinition> operationDefinitions = new ArrayList<DEInsertDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, true, null);
		
		for (EReference eReference : eReferences) {
			DEInsertDeltaOperationDefinition operationDefinition = createInsertDeltaOperationDefinition(eReference);
			operationDefinitions.add(operationDefinition);
		}
		
		return operationDefinitions;
	}
	
	public static List<DERemoveDeltaOperationDefinition> createRemoveDeltaOperationDefinitions(EPackage metaModel) {
		List<DERemoveDeltaOperationDefinition> operationDefinitions = new ArrayList<DERemoveDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, true, null);
		
		for (EReference eReference : eReferences) {
			DERemoveDeltaOperationDefinition operationDefinition = createRemoveDeltaOperationDefinition(eReference);
			operationDefinitions.add(operationDefinition);
		}
		
		return operationDefinitions;
	}
	
	
	public static List<DEModifyDeltaOperationDefinition> createModifyDeltaOperationDefinitions(EPackage metaModel) {
		List<DEModifyDeltaOperationDefinition> operationDefinitions = new ArrayList<DEModifyDeltaOperationDefinition>();
		
		Iterator<EObject> iterator = metaModel.eAllContents();
		
		while(iterator.hasNext()) {
			EObject eObject = iterator.next();
			
			if (eObject instanceof EClass) {
				EClass eClass = (EClass) eObject;
				
				if (!eClass.isAbstract() && !eClass.isInterface()) {
					
					List<EAttribute> eAttributes = eClass.getEAllAttributes();
					
					for (EAttribute eAttribute : eAttributes) {
						if (eAttribute.isChangeable() && !eAttribute.isID() && !eAttribute.isUnsettable()) {
							
							DEModifyDeltaOperationDefinition operationDefinition = createModifyDeltaOperationDefinition(eAttribute, eClass);
							operationDefinitions.add(operationDefinition);
						}
					}
				}
			}
		}
		
		return operationDefinitions;
	}
	
	
	public static List<DEDetachDeltaOperationDefinition> createDetachDeltaOperationDefinitions(EPackage metaModel) {
		List<DEDetachDeltaOperationDefinition> operationDefinitions = new ArrayList<DEDetachDeltaOperationDefinition>();
		List<EReference> eReferences = collectAllReferences(metaModel, null, true);
		Set<EClass> classesWithDetachOperations = new HashSet<EClass>();
		
		for (EReference eReference : eReferences) {
			EClass elementEClass = eReference.getEReferenceType();
			
			//Only create at most one detach oepration per EClass
			if (!classesWithDetachOperations.contains(elementEClass)) {
				DEDetachDeltaOperationDefinition operationDefinition = createDetachDeltaOperationDefinition(eReference);
				operationDefinitions.add(operationDefinition);
				
				classesWithDetachOperations.add(elementEClass);
			}
		}
		
		return operationDefinitions;
	}
	
	
	//Operation definitions
	//NOTE: Used by delta dialect generation of David Wille.
	public static DESetDeltaOperationDefinition createSetDeltaOperationDefinition(EReference eReference) {
		DESetDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDESetDeltaOperationDefinition();

		EClass elementEClass = eReference.getEContainingClass();
		String name = "set" + cleanUpName(eReference) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		addElementAndValueParameters(operationDefinition, eReference);
		
		return operationDefinition;
	}
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DEUnsetDeltaOperationDefinition createUnsetDeltaOperationDefinition(EReference eReference) {
		DEUnsetDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDEUnsetDeltaOperationDefinition();

		EClass elementEClass = eReference.getEContainingClass();
		String name = "unset" + cleanUpName(eReference) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		DEModelElementWithReferenceParameter elementParameter = createDEModelElementWithReferenceParameter("element", eReference);
		operationDefinition.setElement(elementParameter);
		
		return operationDefinition;
	}
	
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DEAddDeltaOperationDefinition createAddDeltaOperationDefinition(EReference eReference) {
		DEAddDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDEAddDeltaOperationDefinition();

		EClass valueEClass = eReference.getEReferenceType();
		EClass elementEClass = eReference.getEContainingClass();
		String name = "add" + cleanUpName(valueEClass) + "To" + cleanUpName(eReference) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		addElementAndValueParameters(operationDefinition, eReference);
		
		return operationDefinition;
	}
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DEInsertDeltaOperationDefinition createInsertDeltaOperationDefinition(EReference eReference) {
		DEInsertDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDEInsertDeltaOperationDefinition();

		EClass valueEClass = eReference.getEReferenceType();
		EClass elementEClass = eReference.getEContainingClass();
		String name = "insert" + cleanUpName(valueEClass) + "Into" + cleanUpName(eReference) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		addElementAndValueParameters(operationDefinition, eReference);
		
		//Create and add "index" parameter
		DENamedParameter indexParameter = DEcoreDialectFactory.eINSTANCE.createDENamedParameter();
		DEType type = DEcoreBaseFactory.eINSTANCE.createDEInteger();
		
		indexParameter.setName("index");
		indexParameter.setType(type);
		
		operationDefinition.setIndex(indexParameter);
		
		return operationDefinition;
	}
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DERemoveDeltaOperationDefinition createRemoveDeltaOperationDefinition(EReference eReference) {
		DERemoveDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDERemoveDeltaOperationDefinition();

		EClass valueEClass = eReference.getEReferenceType();
		EClass elementEClass = eReference.getEContainingClass();
		String name = "remove" + cleanUpName(valueEClass) + "From" + cleanUpName(eReference) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		addElementAndValueParameters(operationDefinition, eReference);
		
		return operationDefinition;
	}
	
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DEModifyDeltaOperationDefinition createModifyDeltaOperationDefinition(EAttribute eAttribute, EClass actualContainingEClass) {
		DEModifyDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDEModifyDeltaOperationDefinition();

		EClass elementEClass = actualContainingEClass;
		String name = "modify" + cleanUpName(eAttribute) + "Of" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		addElementAndValueParameters(operationDefinition, eAttribute, actualContainingEClass);
		
		return operationDefinition;
	}
	
	
	//NOTE: Used by delta dialect generation of David Wille.
	public static DEDetachDeltaOperationDefinition createDetachDeltaOperationDefinition(EReference eReference) {
		DEDetachDeltaOperationDefinition operationDefinition = DEcoreDialectFactory.eINSTANCE.createDEDetachDeltaOperationDefinition();

		EClass elementEClass = eReference.getEReferenceType();
		String name = "detach" + cleanUpName(elementEClass);
		operationDefinition.setName(name);
		
		
		//Create "element" parameter
		DEModelElementParameter elementParameter = DEcoreDialectFactory.eINSTANCE.createDEModelElementParameter();
		DEMetaModelClassifierReference type = createDEMetaModelClassifierReference(elementEClass);
		
		elementParameter.setName("element");
		elementParameter.setType(type);
		
		operationDefinition.setElement(elementParameter);
		
		return operationDefinition;
	}
	
	
	
	//Naming stuff
	private static String cleanUpName(String name) {
		String cleanedUpName = name;
		
		cleanedUpName = StringUtil.removePrefix(cleanedUpName);
		cleanedUpName = StringUtil.firstToUpper(cleanedUpName);
		
		return cleanedUpName;
	}
	
	private static String cleanUpName(EClassifier eClassifier) {
		String name = eClassifier.getName();
		return cleanUpName(name);
	}
	
	private static String cleanUpName(EReference eReference) {
		String name = eReference.getName();
		return cleanUpName(name);
	}
	
	private static String cleanUpName(EAttribute eAttribute) {
		String name = eAttribute.getName();
		return cleanUpName(name);
	}
	
	
	//Constructor stuff
	public static DEMetaModelClassifierReference createDEMetaModelClassifierReference(EClassifier eClassifier) {
		DEMetaModelClassifierReference metaModelClassifierReference = DEcoreBaseFactory.eINSTANCE.createDEMetaModelClassifierReference();
		metaModelClassifierReference.setClassifier(eClassifier);
		return metaModelClassifierReference;
	}
	
	public static DEModelElementWithReferenceParameter createDEModelElementWithReferenceParameter(String name, EReference eReference) {
		EClass eClass = eReference.getEContainingClass();
		
		DEModelElementWithReferenceParameter parameter = DEcoreDialectFactory.eINSTANCE.createDEModelElementWithReferenceParameter();
		DEMetaModelClassifierReference type = createDEMetaModelClassifierReference(eClass);
		
		parameter.setName(name);
		parameter.setType(type);
		parameter.setReference(eReference);
		
		return parameter;
	}
	
	public static DEModelElementWithAttributeParameter createDEModelElementWithAttributeParameter(String name, EAttribute eAttribute, EClass actualContainingEClass) {
		DEModelElementWithAttributeParameter parameter = DEcoreDialectFactory.eINSTANCE.createDEModelElementWithAttributeParameter();
		DEMetaModelClassifierReference type = createDEMetaModelClassifierReference(actualContainingEClass);
		
		parameter.setName(name);
		parameter.setType(type);
		parameter.setAttribute(eAttribute);
		
		return parameter;
	}
	
	public static DENamedParameter createDENamedParameterForReferenceValue(String name, EReference eReference) {
		EClass valueEClass = eReference.getEReferenceType();
		
		return createDENamedParameter(name, valueEClass);
	}
	
	public static DENamedParameter createDENamedParameterForAttributeValue(String name, EAttribute eAttribute) {
		DEType type = createTypeForAttributeValue(eAttribute);
		return createDENamedParameter(name, type);
	}
	
	public static DENamedParameter createDENamedParameter(String name, EClassifier eClassifier) {
		DEMetaModelClassifierReference type = createDEMetaModelClassifierReference(eClassifier);
		
		return createDENamedParameter(name, type);
	}
	
	public static DENamedParameter createDENamedParameter(String name, DEType type) {
		DENamedParameter namedParameter = DEcoreDialectFactory.eINSTANCE.createDENamedParameter();
		
		namedParameter.setName(name);
		namedParameter.setType(type);
		
		return namedParameter;
	}
	
	
	//Utils
	private static List<EReference> collectAllReferences(EPackage metaModel, Boolean isMany, Boolean containment) {
		List<EReference> eReferences = new ArrayList<EReference>();
		Iterator<EObject> iterator = metaModel.eAllContents();
		
		while(iterator.hasNext()) {
			EObject eObject = iterator.next();
			
			if (eObject instanceof EReference) {
				EReference eReference = (EReference) eObject;
				
				if (eReference.isChangeable() && (isMany == null || eReference.isMany() == isMany) && (containment == null || eReference.isContainment() == containment)) {
					eReferences.add(eReference);
				}
			}
		}
		
		return eReferences;
	}
	
	
	//TODO: unify
	private static void addElementAndValueParameters(DESetDeltaOperationDefinition operationDefinition, EReference eReference) {
		DENamedParameter valueParameter = createDENamedParameterForReferenceValue("value", eReference);
		operationDefinition.setValue(valueParameter);
		
		DEModelElementWithReferenceParameter elementParameter = createDEModelElementWithReferenceParameter("element", eReference);
		operationDefinition.setElement(elementParameter);
	}
	
	//TODO: unify
	private static void addElementAndValueParameters(DEMultiValuedReferenceDeltaOperationDefinition operationDefinition, EReference eReference) {
		DENamedParameter valueParameter = createDENamedParameterForReferenceValue("value", eReference);
		operationDefinition.setValue(valueParameter);
		
		DEModelElementWithReferenceParameter elementParameter = createDEModelElementWithReferenceParameter("element", eReference);
		operationDefinition.setElement(elementParameter);
	}
	
	private static void addElementAndValueParameters(DEModifyDeltaOperationDefinition operationDefinition, EAttribute eAttribute, EClass actualContainingEClass) {
		DENamedParameter valueParameter = createDENamedParameterForAttributeValue("value", eAttribute);
		operationDefinition.setValue(valueParameter);
		
		DEModelElementWithAttributeParameter elementParameter = createDEModelElementWithAttributeParameter("element", eAttribute, actualContainingEClass);
		operationDefinition.setElement(elementParameter);
	}
	
	//TODO: This is not clean - fix
	public static DEType createTypeForAttributeValue(EAttribute eAttribute) {
		EDataType eAttributeType = eAttribute.getEAttributeType();
		String attributeTypeName = eAttributeType.getName();
		
		if (attributeTypeName.equals("EBoolean") || attributeTypeName.equals("EBooleanObject")) {
			return DEcoreBaseFactory.eINSTANCE.createDEBoolean();
		}
		
		if (attributeTypeName.equals("EInt") || attributeTypeName.equals("EIntegerObject")) {
			return DEcoreBaseFactory.eINSTANCE.createDEInteger();
		}
		
		if (attributeTypeName.equals("EDouble") || attributeTypeName.equals("EDoubleObject")) {
			return DEcoreBaseFactory.eINSTANCE.createDEDouble();
		}
		
		if (attributeTypeName.equals("EString")) {
			return DEcoreBaseFactory.eINSTANCE.createDEString();
		}
		
		//Default is to create a reference to the type.
		DEMetaModelClassifierReference classifierReference = DEcoreBaseFactory.eINSTANCE.createDEMetaModelClassifierReference();
		classifierReference.setClassifier(eAttributeType);
		
		return classifierReference;
	}
	
	
	
	
	
	
	
//	public static DEAddDeltaOperationDefinition createAddDeltaOperationDefinition(EClass target) {
//		DEAddDeltaOperationDefinition deltaOperationDefinition = DEcoreDialectFactory.eINSTANCE.createDEAddDeltaOperationDefinition();
//		DEParameterBuilder parameterBuilder = new DEParameterBuilder();
//		
//		initializeStandardDeltaOperationDefinition(deltaOperationDefinition, target, parameterBuilder);
//		
//		//Parent
//		EReference containingReference = EcoreReflectionUtil.findContainingReferenceForEClass(target);
//		DEContainingReferenceDeltaOperationParameter parentParameter = parameterBuilder.createContainingReferenceDeltaOperationParameterFromEReference(containingReference);
//		deltaOperationDefinition.setParent(parentParameter);
//		
//		return deltaOperationDefinition;
//	}
//	
//	public static DEModifyDeltaOperationDefinition createModifyDeltaOperationDefinition(EClass target) {
//		DEModifyDeltaOperationDefinition deltaOperationDefinition = DEcoreDialectFactory.eINSTANCE.createDEModifyDeltaOperationDefinition();
//		DEParameterBuilder parameterBuilder = new DEParameterBuilder();
//		
//		initializeStandardDeltaOperationDefinition(deltaOperationDefinition, target, parameterBuilder);
//		
//		List<EStructuralFeature> structuralFeatures = getStructuralFeaturesForModifyDeltaOperation(target);
//		List<DEModifyDeltaOperationParameter> declaredParameters = deltaOperationDefinition.getDeclaredParameters();
//		
//		for (EStructuralFeature structuralFeature : structuralFeatures) {
//			DEModifyDeltaOperationParameter parameter = parameterBuilder.createModifyDeltaOperationParameterFromEStructuralFeature(structuralFeature);
//			declaredParameters.add(parameter);
//		}
//		
//		return deltaOperationDefinition;
//	}
//	
//	public static DEReplaceDeltaOperationDefinition createReplaceDeltaOperationDefinition(EClass target) {
//		DEReplaceDeltaOperationDefinition deltaOperationDefinition = DEcoreDialectFactory.eINSTANCE.createDEReplaceDeltaOperationDefinition();
//		DEParameterBuilder parameterBuilder = new DEParameterBuilder();
//		
//		initializeStandardDeltaOperationDefinition(deltaOperationDefinition, target, parameterBuilder);
//		
//		DENamedParameter replacementParameter = parameterBuilder.createReplacementParameterFromEClass(target);
//		deltaOperationDefinition.setReplacement(replacementParameter);
//		
//		return deltaOperationDefinition;
//	}
//	
//	public static DERemoveDeltaOperationDefinition createRemoveDeltaOperationDefinition(EClass target) {
//		DERemoveDeltaOperationDefinition deltaOperationDefinition = DEcoreDialectFactory.eINSTANCE.createDERemoveDeltaOperationDefinition();
//		DEParameterBuilder parameterBuilder = new DEParameterBuilder();
//		
//		initializeStandardDeltaOperationDefinition(deltaOperationDefinition, target, parameterBuilder);
//		
//		return deltaOperationDefinition;
//	}
//	
//
//
//	
//	private static void initializeStandardDeltaOperationDefinition(DEStandardDeltaOperationDefinition deltaOperationDefinition, EClass target, DEParameterBuilder parameterBuilder) {
//		initializeDeltaOperationDefinition(deltaOperationDefinition, target);
//		
//		DENamedParameter objectParameter = parameterBuilder.createObjectParameter(target);
//		deltaOperationDefinition.setObject(objectParameter);
//	}
//	
//	private static void initializeDeltaOperationDefinition(DEDeltaOperationDefinition deltaOperationDefinition, EClass target) {
//		String deltaOperationName = createDeltaOperationName(target, deltaOperationDefinition);
//		deltaOperationDefinition.setName(deltaOperationName);
//	}
//	
//	private static String createDeltaOperationName(EClass target, DEDeltaOperationDefinition deltaOperationDefinition) {
//		String operationTypePrefix = getOperationTypePrefix(deltaOperationDefinition);
//		String targetName = StringUtil.removePrefix(target.getName());
//		
//		return operationTypePrefix + targetName;
//	}
//	
//	private static String getOperationTypePrefix(DEDeltaOperationDefinition deltaOperationDefinition) {
//		if (deltaOperationDefinition instanceof DEAddDeltaOperationDefinition) {
//			return "add";
//		}
//		
//		if (deltaOperationDefinition instanceof DEModifyDeltaOperationDefinition) {
//			return "modify";
//		}
//		
//		if (deltaOperationDefinition instanceof DEReplaceDeltaOperationDefinition) {
//			return "replace";
//		}
//		
//		if (deltaOperationDefinition instanceof DERemoveDeltaOperationDefinition) {
//			return "remove";
//		}
//		
//		if (deltaOperationDefinition instanceof DECustomDeltaOperationDefinition) {
//			return "custom";
//		}
//		
//		throw new UnsupportedOperationException("Could not derive operation type prefix from unknown delta oepration definition type.");
//	}
//	
//	
//	//TODO: Seriously think about only allowing attributes!
//	//TODO: Externalize this to make it reuseble (e.g., for constraints on modify delta operations)
//	private static List<EStructuralFeature> getStructuralFeaturesForModifyDeltaOperation(EClass eClass) {
//		List<EStructuralFeature> matchingStructuralFeatures = new ArrayList<EStructuralFeature>();
//		List<EStructuralFeature> structuralFeatures = eClass.getEAllStructuralFeatures();
//		
//		for (EStructuralFeature structuralFeature : structuralFeatures) {
//			//Skip non changeable features.
//			if (!structuralFeature.isChangeable()) {
//				continue;
//			}
//			
//			//TODO: Cannot handle many valued references and attributes right now so exclude them for the time being.
//			if (structuralFeature.isMany()) {
//				continue;
//			}
//			
//			//Disallow modification of IDs.
//			if (structuralFeature instanceof EAttribute) {
//				EAttribute eAttribute = (EAttribute) structuralFeature;
//				
//				if (eAttribute.isID()) {
//					continue;
//				}
//			}
//			
//			matchingStructuralFeatures.add(structuralFeature);
//		}
//		
//		return matchingStructuralFeatures;
//	}
//	
//	public static List<EClass> getEClassesForReplaceDeltaOperation(EClass objectClass) {
//		List<EClass> replacementClasses = new ArrayList<EClass>();
//		
//		//Add the class itself.
//		replacementClasses.add(objectClass);
//		
//		//Add all super classes/interfaces.
//		replacementClasses.addAll(objectClass.getEAllSuperTypes());
//		
//		//TODO: Add all specializing classes
//		
//		EcoreUtil2.sortByName(replacementClasses);
//		return replacementClasses;
//	}
//	
//	//Implement this and put in a more prominent spot for constraints.
//	public static boolean isPossibleDeltaOperationDefinition(Class<? extends DEDeltaOperationDefinition> deltaOperationDefinitionType, EClass target) {
//		boolean isAdd = DEAddDeltaOperationDefinition.class.isAssignableFrom(deltaOperationDefinitionType);
//		boolean isModify = DEModifyDeltaOperationDefinition.class.isAssignableFrom(deltaOperationDefinitionType);
//		boolean isReplace = DEReplaceDeltaOperationDefinition.class.isAssignableFrom(deltaOperationDefinitionType);
//		boolean isRemove = DERemoveDeltaOperationDefinition.class.isAssignableFrom(deltaOperationDefinitionType);
//		
//		//No add, remove or replace operations for root elements of meta model
//		if (isAdd || isReplace || isRemove) {
//			EReference containingReference = EcoreReflectionUtil.findContainingReferenceForEClass(target);
//			
//			if (containingReference == null) {
//				return false;
//			}
//		}
//		
//		//Disallow modify operations when there are no modifiable features.
//		if (isModify) {
//			List<EStructuralFeature> structuralFeatures = getStructuralFeaturesForModifyDeltaOperation(target);
//			
//			if (structuralFeatures.isEmpty()) {
//				return false;
//			}
//		}
//		
//		return true;
//	}
}
