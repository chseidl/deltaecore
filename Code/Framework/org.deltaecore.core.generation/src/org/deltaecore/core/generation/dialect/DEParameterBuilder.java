package org.deltaecore.core.generation.dialect;

import java.util.List;

import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DEType;
import org.deltaecore.core.decorebase.DEcoreBaseFactory;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DENamedParameter;
import org.deltaecore.core.decoredialect.DEParameter;
import org.deltaecore.core.decoredialect.DEcoreDialectFactory;
import org.deltaecore.core.generation.DEIdentifierNameBuilder;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.christophseidl.util.java.StringUtil;

//TODO: Migrate, possibly obsolete
public class DEParameterBuilder extends DEIdentifierNameBuilder {
	
	public DEParameterBuilder() {
		super();
	}
	
	public DEParameterBuilder(DEDeltaOperationDefinition deltaOperationDefinition) {
		this();
		
		//Initialize already declared names from existing operation definition.
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
		
		for (DEParameter parameter : parameters) {
			registerParameterName(parameter);
		}
	}
	
	public DENamedParameter createObjectParameter(EClass target) {
		return createNamedParameterFromEClass(target, "");
	}
	
	public DENamedParameter createReplacementParameterFromEClass(EClass target) {
		String name = createReplacementParameterName(target);
		
		return createNamedParameter(target, name);
	}
	
	public void updateReplacementParameterName(DENamedParameter parameter) {
		unregisterParameterName(parameter);
		
		DEType type = parameter.getType();
		DEMetaModelClassifierReference metaModelClassifierReference = (DEMetaModelClassifierReference) type;
		EClass replacementEClass = (EClass) metaModelClassifierReference.getClassifier();
		String name = createReplacementParameterName(replacementEClass);
		parameter.setName(name);
	}
	
	private String createReplacementParameterName(EClass target) {
		return createParameterNameFromEClass(target, "replacement");
	}
	
	private DENamedParameter createNamedParameterFromEClass(EClass target, String prefix) {
		String name = createParameterNameFromEClass(target, prefix);
		DEMetaModelClassifierReference type = createMetaModelClassifierReference(target);
		
		return createNamedParameter(type, name);
	}
	
	public DENamedParameter createNamedParameterFromEStructuralFeature(EStructuralFeature structuralFeature) {
		String name = StringUtil.firstToLower(structuralFeature.getName());
		DEType type = getParameterType(structuralFeature);
		
		return createNamedParameter(type, name);
	}
	
//	public DEModifyDeltaOperationParameter createModifyDeltaOperationParameterFromEStructuralFeature(EStructuralFeature structuralFeature) {
//		DEType type = getParameterType(structuralFeature);
//		
//		DEModifyDeltaOperationParameter parameter = DEcoreDialectFactory.eINSTANCE.createDEModifyDeltaOperationParameter();
//		parameter.setStructuralFeature(structuralFeature);
//		parameter.setType(type);
//		
//		return parameter;
//	}
//	
//	public DEContainingReferenceDeltaOperationParameter createContainingReferenceDeltaOperationParameterFromEReference(EReference reference) {
//		EClass containingClass = reference.getEContainingClass();
//		DEMetaModelClassifierReference type = createMetaModelClassifierReference(containingClass);
//		String name = createAddDeltaOperationParameterNameFromEReference(reference);
//		
//		DEContainingReferenceDeltaOperationParameter parameter = DEcoreDialectFactory.eINSTANCE.createDEContainingReferenceDeltaOperationParameter();
//		parameter.setContainingReference(reference);
//		parameter.setType(type);
//		parameter.setName(name);
//		
//		return parameter;
//	}
	
//	private String createAddDeltaOperationParameterNameFromEReference(EReference reference) {
//		EClass containingClass = reference.getEContainingClass();
//		String name = createParameterNameFromEClass(containingClass, "parent");
//		name = makeIdentifierNameValidAndUniqueIfNecessary(name);
//		
//		registerIdentifierName(name);
//		
//		return name;
//	}
	
//	public void updateContainingReferenceDeltaOperationParameterName(DEContainingReferenceDeltaOperationParameter parameter) {
//		unregisterParameterName(parameter);
//		
//		EReference reference = parameter.getContainingReference();
//		String name = createAddDeltaOperationParameterNameFromEReference(reference);
//		parameter.setName(name);
//	}
	
	private DENamedParameter createNamedParameter(EClass target, String name) {
		DEMetaModelClassifierReference type = createMetaModelClassifierReference(target);
		
		return createNamedParameter(type, name);
	}
	
	private DENamedParameter createNamedParameter(DEType type, String name) {
		DENamedParameter namedParameter = DEcoreDialectFactory.eINSTANCE.createDENamedParameter();
		
		name = makeIdentifierNameValidAndUniqueIfNecessary(name);
		
		namedParameter.setType(type);
		namedParameter.setName(name);

		registerIdentifierName(name);
		
		return namedParameter;
	}
	
	private static String createParameterNameFromEClass(EClass eClass, String namePrefix) {
		if (eClass == null) {
			return null;
		}
		
		String parameterName = eClass.getName();
		parameterName = StringUtil.removePrefix(parameterName);
		
		//Allow prefixes to create variance in names
		if (namePrefix != null && !namePrefix.isEmpty()) {
			parameterName = StringUtil.firstToLower(namePrefix) + parameterName;
		} else {
			parameterName = StringUtil.firstToLower(parameterName);
		}
		
		return parameterName;
	}
	

	
	private static DEMetaModelClassifierReference createMetaModelClassifierReference(EClassifier classifier) {
		DEMetaModelClassifierReference metaModelClassifierReference = DEcoreBaseFactory.eINSTANCE.createDEMetaModelClassifierReference();
		metaModelClassifierReference.setClassifier(classifier);
		return metaModelClassifierReference;
	}
	
	private static DEType getParameterType(EStructuralFeature structuralFeature) {
		EClassifier parameterType = structuralFeature.getEType();
		
		//An EEnum is a special case of an EDataType that is handle equivalently to an EClass
		if (parameterType instanceof EClass || parameterType instanceof EEnum) {
			return createMetaModelClassifierReference(parameterType);
		}
		
		if (parameterType instanceof EDataType) {
			EDataType eDataType = (EDataType) parameterType;
			String eDataTypeName = eDataType.getName();
			
			//Handle Boolean, Integer, Double and String as special cases.
			if (eDataTypeName.equals("EBoolean") || eDataTypeName.equals("EBooleanObject")) {
				return DEcoreBaseFactory.eINSTANCE.createDEBoolean();
			}
			
			if (eDataTypeName.equals("EInt") || eDataTypeName.equals("EIntegerObject")) {
				return DEcoreBaseFactory.eINSTANCE.createDEInteger();
			}
	
			if (eDataTypeName.equals("EDouble") || eDataTypeName.equals("EDoubleJavaObject")) {
				return DEcoreBaseFactory.eINSTANCE.createDEDouble();
			}
			
			if (eDataTypeName.equals("EString")) {
				return DEcoreBaseFactory.eINSTANCE.createDEString();
			}
			
			//Can't handle these in the general case right now
			return createMetaModelClassifierReference(parameterType);
		}
		
		System.err.println("Encountered unknown parameter type \"" + parameterType.getClass() + "\".");
		return null;
	}
	
	private void registerParameterName(DEParameter parameter) {
		String name = parameter.getName();
		registerIdentifierName(name);
	}
	
	private void unregisterParameterName(DEParameter parameter) {
		String name = parameter.getName();
		unregisterIdentifierName(name);
	}
}
