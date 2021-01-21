package org.eclipse.uml2.uml.delta;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

import de.tu_bs.cs.isf.spldps.eclipse.design.composers.uml.UMLComposerUtil;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class UmlDeltaDialectInterpreter extends UmlAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretCreatePackage(DEModelWriter modelWriter, String qualifiedPackageName, Model umlModel) {
		if (!UMLComposerUtil.packageExists(qualifiedPackageName, umlModel)) {
			UMLComposerUtil.createPackage(qualifiedPackageName, umlModel);
		}
		
		return true;
	}

	@Override
	protected boolean interpretCreateInterface(DEModelWriter modelWriter, String simpleInterfaceName, Package umlPackage) {
		UMLComposerUtil.createInterface(simpleInterfaceName, umlPackage);
		
		return true;
	}
	
	@Override
	protected boolean interpretCreateClass(DEModelWriter modelWriter, String simpleClassName, Package package_, Boolean abstract_) {
		UMLComposerUtil.createClass(simpleClassName, package_, abstract_);
		return true;
	}

	
	@Override
	protected boolean interpretCreateProperty(DEModelWriter modelWriter, String name, Type type, Integer lowerBound, Integer upperBound, Class containingClass) {
		UMLComposerUtil.createProperty(name, type, lowerBound, upperBound, containingClass);
		return true;
	}
	
	@Override
	protected boolean interpretCreateOperationInInterface(DEModelWriter modelWriter, String name, Interface containingInterface) {
		UMLComposerUtil.createOperationInInterface(name, containingInterface);
		return true;
	}
	
	@Override
	protected boolean interpretCreateOperationInClass(DEModelWriter modelWriter, String name, Class containingClass) {
		UMLComposerUtil.createOperationInClass(name, containingClass);
		return true;
	}
	
	@Override
	protected boolean interpretCreateParameter(DEModelWriter modelWriter, String name, Type type, Integer lowerBound, Integer upperBound, Operation containingOperation) {
		UMLComposerUtil.createParameter(name, type, lowerBound, upperBound, containingOperation);
		return true;
	}
	
	
	@Override
	protected boolean interpretCreateGeneralization(DEModelWriter modelWriter, Class class_, Class superClass) {
		UMLComposerUtil.createGeneralization(class_, superClass);
		return true;
	}
	
	@Override
	protected boolean interpretCreateInterfaceRealization(DEModelWriter modelWriter, Class realizingClass, Interface realizedInterface) {
		UMLComposerUtil.createInterfaceRealization(realizingClass, realizedInterface);
		return true;
	}

	@Override
	protected boolean interpretCreateCommentInOperation(DEModelWriter modelWriter, String text, Operation operation) {
		UMLComposerUtil.createCommentInOperation(text, operation);
		return true;
	}
}