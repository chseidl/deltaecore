package org.deltaecore.core.generation.interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEParameter;
import org.deltaecore.core.decoredialect.DEStandardDeltaOperationDefinition;
import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.christophseidl.util.java.StringUtil;

public class DEDeltaDialectInterpreterCreatorUtil {
	public static List<String> createInterpreterMethodArgumentDeclarations(DEDeltaOperationDefinition deltaOperationDefinition) {
		List<String> interpreterMethodArguments = new ArrayList<String>();
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
		
		int i = 0;
		
		for (DEParameter parameter : parameters) {
			String parameterName = parameter.getName();
			Class<?> valueType = parameter.getType().getValueType();
			String simpleValueTypeName = valueType.getSimpleName();
			
			//E.g., "SFTBasicFault basicFault = (SFTBasicFault) arguments.get(0).getExpression().getValue();"
			String interpreterMethodArgument = "";
			
			interpreterMethodArgument += simpleValueTypeName + " " + parameterName + " = ";
			interpreterMethodArgument += "(" + simpleValueTypeName + ") arguments.get(" + i + ").getExpression().getValue();";
			
			interpreterMethodArguments.add(interpreterMethodArgument);
			i++;
		}
		
		return interpreterMethodArguments;
	}
	
	public static List<String> createImportStatements(List<DEDeltaOperationDefinition> deltaOperationDefinitions, boolean onlyManual) {
		List<String> importStatements = new ArrayList<String>();
		Set<Class<?>> alreadyImportedClasses = new HashSet<Class<?>>();
		
		boolean atLeastOneImplemented = false;
		
		for (DEDeltaOperationDefinition deltaOperationDefinition : deltaOperationDefinitions) {
			
			if (!onlyManual || (onlyManual && needsManualImplementation(deltaOperationDefinition))) {
				List<DEParameter> parameters = deltaOperationDefinition.getParameters();
				
				for (DEParameter parameter : parameters) {
					Class<?> valueType = parameter.getType().getValueType();
					
					if (!alreadyImportedClasses.contains(valueType) && !isPrimitiveWrapper(valueType)) {
						//E.g., "import eu.vicci.ecosystem.sft.SFTBasicFault;"
						String importStatement = "import " + valueType.getCanonicalName() + ";";
						importStatements.add(importStatement);
						
						alreadyImportedClasses.add(valueType);
					}
				}
				
				atLeastOneImplemented = true;
			}
		}
		
		if (atLeastOneImplemented) {
			importStatements.add(0, "import " + DEModelWriter.class.getCanonicalName() + ";");
		}
		
		return importStatements;
	}
	
	public static boolean needsManualImplementation(DEDeltaOperationDefinition deltaOperationDefinition) {
		if (deltaOperationDefinition instanceof DEStandardDeltaOperationDefinition) {
			return false;
		}
		
		return true;
	}
	
	private static boolean isPrimitiveWrapper(Class<?> c) {
		if (c.equals(Boolean.class) || c.equals(Integer.class) || c.equals(Double.class) || c.equals(String.class)) {
			return true;
		}
		
		return false;
	}
	
	public static String createInterpreterMethodCall(DEDeltaOperationDefinition deltaOperationDefinition) {
		//E.g., "interpretAddBasicFault(basicFault, parentGate);"
		String interpreterMethodCall = createDeltaOperationMethodName(deltaOperationDefinition) + "(";
		
		interpreterMethodCall += "modelWriter";
		
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
//		boolean isFirst = true;
		
		for (DEParameter parameter : parameters) {
//			if (!isFirst) {
				interpreterMethodCall += ", ";
//			}
			
			interpreterMethodCall += parameter.getName();
			
//			isFirst = false;
		}
		
		interpreterMethodCall += ")";
		
		return interpreterMethodCall;
	}
	
	public static String createInterpreterMethodAbstractDeclaration(DEDeltaOperationDefinition deltaOperationDefinition) {
		//E.g., "abstract protected boolean interpretAddBasicFault(SFTBasicFault basicFault, SFTGate parentGate);"
		String interpreterMethodAbstractDeclaration = "abstract " + createInterpreterMethodSignature(deltaOperationDefinition) + ";";
		
		return interpreterMethodAbstractDeclaration;	
	}
	
	public static String createInterpreterMethodSignature(DEDeltaOperationDefinition deltaOperationDefinition) {
		//E.g., "protected boolean interpretAddBasicFault(SFTBasicFault basicFault, SFTGate parentGate)"
		String interpreterMethodSignature = "protected boolean " + createDeltaOperationMethodName(deltaOperationDefinition) + "(";
		
		interpreterMethodSignature += "DEModelWriter modelWriter";
		
		List<DEParameter> parameters = deltaOperationDefinition.getParameters();
//		boolean isFirst = true;
		
		for (DEParameter parameter : parameters) {
//			if (!isFirst) {
				interpreterMethodSignature += ", ";
//			}
			
			String parameterName = parameter.getName();
			Class<?> valueType = parameter.getType().getValueType();
			
			interpreterMethodSignature += valueType.getSimpleName() + " " + parameterName;
			
//			isFirst = false;
		}
		
		interpreterMethodSignature += ")";
		
		return interpreterMethodSignature;
	}
	
	public static String createDeltaOperationMethodName(DEDeltaOperationDefinition deltaOperationDefinition) {
		String deltaOperationName = deltaOperationDefinition.getName();
		
		return "interpret" + StringUtil.firstToUpper(deltaOperationName);
	}
	
	public static String createPackageConstantForStructuralFeature(EStructuralFeature structuralFeature) {
//		//TODO: Use the name of the constant referring to this feature in the generated package instead of its raw id
//		//(which is hard to decipher to a feature and might be volatile if the meta model changes), see below.
		int featureID = structuralFeature.getFeatureID();
		return Integer.toString(featureID);
	}
	
//	public static String createPackageConstantForStructuralFeature(EStructuralFeature structuralFeature) {
//		Resource resource = structuralFeature.eResource();
//		URI namespaceURI = resource.getURI();
//		String rawNamespaceURI = namespaceURI.toString();
//		
//		EPackage ePackage = Registry.INSTANCE.getEPackage(rawNamespaceURI);
//		String packageName = ePackage.getClass().getCanonicalName();
//
////		System.out.println("EPackage: " + packageName);
//
////		//TODO: Need genmodel to get the package name as it uses the prefix specified only in the genmodel.
//
////		//Use the same mechanism as during code generation.
////		GenFeature genFeature = GenModelFactory.eINSTANCE.createGenFeature();
////		genFeature.setEcoreFeature(structuralFeature);
////		
////		EClass eContainingClass = structuralFeature.getEContainingClass();
////		
////		GenClass genClass = GenModelFactory.eINSTANCE.createGenClass();
////		genClass.setEcoreClass(eContainingClass);
////		String featureConstantName = genClass.getFeatureID(genFeature);
////		
////		
////		return packageName + "." + featureConstantName;
//		
//		
//		
//		
//		
//		
//		
//		
//		
////		IConfigurationElement metaModelConfigurationElement = EcoreResolverUtil.findMetaModelConfigurationElement(namespaceURI);
////		GenModel genModel = EcoreResolverUtil.findGenModelInRegistry(metaModelConfigurationElement);
////		String fullyQualifiedEPackageName = metaModelConfigurationElement.getAttribute("class");
////		System.out.println(fullyQualifiedEPackageName);
//		
//		//TODO: Have to be from the same resource set!!!
//		
//		GenModel genModel = EcoreResolverUtil.resolveGenModelFromModelElement(structuralFeature);
//		
//		EClass eContainingClass = structuralFeature.getEContainingClass();
//		GenClass genClass = EcoreResolverUtil.resolveGenClass(eContainingClass, genModel);
//		
//		GenFeature genFeature = EcoreResolverUtil.resolveGenFeature(structuralFeature, genModel);
//		
//		
//		String featureConstantName = genClass.getFeatureID(genFeature);
//		
//		System.out.println(featureConstantName);
//		
//		
//		//Dummy
//		int featureID = structuralFeature.getFeatureID();
//		return Integer.toString(featureID);
//	}
}
