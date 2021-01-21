package org.deltaecore.core.generation;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.generation.extension.DEExtensionGenerator;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.deltaecore.core.generation.general.DEProjectSetupGenerator;
import org.deltaecore.core.generation.interpreter.DEDeltaDialectInterpreterStubGenerator;
import org.eclipse.jdt.core.IJavaProject;

import de.christophseidl.util.eclipse.ResourceUtil;

public class DECodeGenerator {
	private DEDeltaDialect dialect;
	
	public DECodeGenerator(DEDeltaDialect dialect) {
		this.dialect = dialect;
	}
	
	public void generate() {
		//Create names
		DENameGenerator nameGenerator = new DENameGenerator(dialect);
		nameGenerator.generate();
		
		//Ensure plugin project setup (natures, manifest.mf, plugin.xml, source folders)
		DEProjectSetupGenerator projectSetupGenerator = new DEProjectSetupGenerator(nameGenerator);
		projectSetupGenerator.generate();
		
		//Generate interpreter stub using generation gap pattern
		DEDeltaDialectInterpreterStubGenerator interpreterStubGenerator = new DEDeltaDialectInterpreterStubGenerator(nameGenerator, projectSetupGenerator, dialect);
		interpreterStubGenerator.generate();
		
		//Generate extension using created elements (Make plugin a singleton)
		DEExtensionGenerator extensionGenerator = new DEExtensionGenerator(nameGenerator, projectSetupGenerator);
		extensionGenerator.generate();
		
		//TODO: Build project
		
		//Refresh the project
		IJavaProject dialectJavaProject = projectSetupGenerator.getDialectJavaProject();
		ResourceUtil.refreshProject(dialectJavaProject);
		
		//TEMP
//		generateNewNew();
	}
	
//	protected void generateNewNew() {
//		EPackage ePackage = dialect.getDomainPackage();
//		List<DEStandardDeltaOperationDefinition> operationDefinitions = DEDeltaDialectCreatorUtil.createStandardDeltaOperationDefinitions(ePackage);
//		
//		for (DEStandardDeltaOperationDefinition operationDefinition : operationDefinitions) {
//			String name = operationDefinition.getName();
//			DEDebug.println("\t//" + name);
//		}
//	}
	
//	//TODO: Tentative implementation of new derivation mechanisms
//	//NOTE: Signature generation works as expected but only on string level. need to adapt to model level and, before that,
//	//modify the meta model to respect the new structure and alter code generation.
//	protected void generateNew() {
//		EPackage ePackage = dialect.getDomainPackage();
//		
//		Iterator<EObject> iterator = ePackage.eAllContents();
//		
//		while (iterator.hasNext()) {
//			EObject eObject = iterator.next();
//			EObject eContainer = eObject.eContainer();
//			
//			if (eContainer instanceof EClassifier) {
//				EClassifier eContainingClassifier = (EClassifier) eObject.eContainer();
//				
//				String eContainingClassifierName = eContainingClassifier.getName();
//				String formattedContainingClassifierName = formatName(eContainingClassifierName);
//				
//				if (eObject instanceof EReference) {
//					EReference eReference = (EReference) eObject;
//					
//					String eReferenceName = eReference.getName();
//					String formattedReferenceName = formatName(eReferenceName);
//					
//					EClassifier eElementType = eReference.getEType();
//					
//					String eElementTypeName = eElementType.getName();
//					String formattedElementTypeName = formatName(eElementTypeName);
//					
//					
//					if (eReference.isChangeable()) {
//						String valueParameter = eElementTypeName + " value";
//						String elementParameter = eContainingClassifierName + "[" + eReferenceName + "] element";
//						String basicParameters = valueParameter + ", " + elementParameter;
//						
//						if (eReference.isMany()) {
//							//Add operation
//							String addSignature = "add" + formattedElementTypeName + "To" + formattedReferenceName + "Of" + formattedContainingClassifierName + "(";
//							addSignature += basicParameters + ")";
//							DEDebug.println(addSignature);
//							
//							//Insert Operation
//							String insertSignature = "insert" + formattedElementTypeName + "Into" + formattedReferenceName + "Of" + formattedContainingClassifierName + "(";
//							insertSignature += basicParameters + ", Integer index)";
//							DEDebug.println(insertSignature);
//							
//							//Remove Operation
//							String removeSignature = "remove" + formattedElementTypeName + "From" + formattedReferenceName + "Of" + formattedContainingClassifierName + "(";
//							removeSignature += basicParameters + ")";
//							DEDebug.println(removeSignature);
//						} else {
//							//Set operation
//							String setSignature = "set" + formattedReferenceName + "Of" + formattedContainingClassifierName + "(";
//							setSignature += basicParameters + ")";
//							DEDebug.println(setSignature);
//							
//							//Unset operation
//							String unsetSignature = "unset" + formattedReferenceName + "Of" + formattedContainingClassifierName + "(";
//							unsetSignature += elementParameter + ")";
//							DEDebug.println(unsetSignature);
//						}
//					}
//				}
//				
//				if (eObject instanceof EAttribute) {
//					EAttribute eAttribute = (EAttribute) eObject;
//					EClass eContainingClass = (EClass) eContainingClassifier;
//					
//					if (eContainingClass.isAbstract() && eAttribute.isChangeable() && !eAttribute.isID()) {
//						String eAttributeName = eAttribute.getName();
//						String formattedAttributeName = formatName(eAttributeName);
//								
//						//TODO: Attribute type is not accurate if it is a primitive type
//						//This should be fixed automatically when using a model based representation. 
//						EClassifier eAttributeType = eAttribute.getEType();
//						
//						String eAttributeTypeName = eAttributeType.getName();
//						
//						//Modify operation
//						String modifySignature = "modify" + formattedAttributeName + "Of" + formattedContainingClassifierName + "(";
//						modifySignature += eAttributeTypeName + " value, ";
//						modifySignature += eContainingClassifierName + "[" + eAttributeName + "] element" + ")";
//						DEDebug.println(modifySignature);
//					}
//				}
//			}
//			
//			if (eContainer instanceof EPackage) {
//				if (eObject instanceof EClass) {
//					EClass eClass = (EClass) eObject;
//					
//					String eClassName = eClass.getName();
//					String formattedClassName = formatName(eClassName);
//					
//					//Detach operations
//					//TODO: Only add detach operations for those classes that participate as target in a containment reference
//					String detachSignature = "detach" + formattedClassName + "(";
//					detachSignature += eClassName + " element";
//					detachSignature += ")";
//					DEDebug.println(detachSignature);
//				}
//			}
//		}
//	}
//	
//	protected static String formatName(String name) {
//		String formattedName = name;
//		
//		formattedName = StringUtil.removePrefix(formattedName);
//		formattedName = StringUtil.firstToUpper(formattedName);
//		
//		return formattedName;
//	}
}
