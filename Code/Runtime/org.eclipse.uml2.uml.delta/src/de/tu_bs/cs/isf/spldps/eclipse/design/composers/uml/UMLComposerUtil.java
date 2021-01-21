package de.tu_bs.cs.isf.spldps.eclipse.design.composers.uml;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class UMLComposerUtil {
	
	public static Model getOrCreateModel(IFile umlModelFile) {
		if (umlModelFile.exists()) {
			return EcoreIOUtil.loadModel(umlModelFile);
		} else {
			ResourceSet resourceSet = new ResourceSetImpl();
			URI uri = EcoreIOUtil.createURIFromFile(umlModelFile);
			Resource resource = resourceSet.createResource(uri);

			Model umlModel =  UMLFactory.eINSTANCE.createModel();
			resource.getContents().add(umlModel);
			return umlModel;
		}
	}

	public static Package getOrCreatePackage(String qualifiedPackageName, Model umlModel) {
		//TODO: Implement so that nested packages are supported
		Package umlPackage = umlModel.getNestedPackage(qualifiedPackageName);

		if (umlPackage != null) {
			return umlPackage;
		}

		return createPackage(qualifiedPackageName, umlModel);
	}
	
	public static Interface getOrCreateInterface(String simpleInterfaceName, Package umlPackage, Model umlModel) {
		PackageableElement element = umlPackage.getPackagedElement(simpleInterfaceName);
		
		if (element instanceof Interface) {
			return (Interface) element;
		}
		
		return createInterface(simpleInterfaceName, umlPackage);
	}

	public static Class getOrCreateClass(String simpleClassName, Package umlPackage, boolean abstract_, Model umlModel) {
		PackageableElement element = umlPackage.getPackagedElement(simpleClassName);

		if (element instanceof Class) {
			return (Class) element;
		}
		
		return createClass(simpleClassName, umlPackage, abstract_);
	}
	
	
	
	public static boolean packageExists(String qualifiedPackageName, Model umlModel) {
		Package umlPackage = umlModel.getNestedPackage(qualifiedPackageName);

		return (umlPackage != null);
	}
	
	public static Package createPackage(String qualifiedPackageName, Model umlModel) {
		Package package_ = UMLFactory.eINSTANCE.createPackage();

		package_.setName(qualifiedPackageName);
		umlModel.getPackagedElements().add(package_);
		return package_;
	}
	
	public static Interface createInterface(String simpleInterfaceName, Package package_) {
		Interface interface_ = UMLFactory.eINSTANCE.createInterface();
		interface_.setName(simpleInterfaceName);
		package_.getPackagedElements().add(interface_);
		return interface_;
	}
	
	public static Class createClass(String simpleClassName, Package umlPackage, boolean abstract_) {
		Class class_ = UMLFactory.eINSTANCE.createClass();
		class_.setName(simpleClassName);
		class_.setIsAbstract(abstract_);
		umlPackage.getPackagedElements().add(class_);
		return class_;
	}
	

	public static Property createProperty(String name, Type type, int lowerBound, int upperBound, Class containingClass) {
		Property property = UMLFactory.eINSTANCE.createProperty();
		property.setName(name);
		property.setType(type);
		property.setLower(lowerBound);
		property.setUpper(upperBound);
		containingClass.getOwnedAttributes().add(property);
		return property;
	}
	
	public static Operation createOperationInInterface(String name, Interface containingInterface) {
		Operation operation = UMLFactory.eINSTANCE.createOperation();
		operation.setName(name);
		containingInterface.getOwnedOperations().add(operation);
		return operation;
	}
	
	public static Operation createOperationInClass(String name, Class containingClass) {
		Operation operation = UMLFactory.eINSTANCE.createOperation();
		operation.setName(name);
		containingClass.getOwnedOperations().add(operation);
		return operation;
	}
	
	public static Parameter createParameter(String name, Type type, int lowerBound, int upperBound, Operation containingOperation) {
		Parameter parameter = UMLFactory.eINSTANCE.createParameter();
		parameter.setName(name);
		parameter.setType(type);
		parameter.setLower(lowerBound);
		parameter.setUpper(upperBound);
		containingOperation.getOwnedParameters().add(parameter);
		return parameter;
	}
	
	
	
	public static Generalization createGeneralization(Class class_, Class superClass) {
		return class_.createGeneralization(superClass);
	}

	public static InterfaceRealization createInterfaceRealization(Class realizingClass, Interface realizedInterface) {
		InterfaceRealization umlInterfaceRealization = UMLFactory.eINSTANCE.createInterfaceRealization();
		umlInterfaceRealization.setContract(realizedInterface);
		realizingClass.getInterfaceRealizations().add(umlInterfaceRealization);
		return umlInterfaceRealization;
	}
	
	public static Comment createCommentInOperation(String text, Operation operation) {
		Comment comment = UMLFactory.eINSTANCE.createComment();
		comment.setBody(text);
		operation.getOwnedComments().add(comment);
		return comment;
	}
}