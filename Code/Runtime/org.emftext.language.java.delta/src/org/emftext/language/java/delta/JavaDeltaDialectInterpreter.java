package org.emftext.language.java.delta;

import java.util.Iterator;
import java.util.List;

import org.deltaecore.interpretation.locking.DEModelWriter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationsPackage;
import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ClassifiersPackage;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.containers.Package;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportingElement;
import org.emftext.language.java.imports.ImportsPackage;
import org.emftext.language.java.members.ClassMethod;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.members.MembersFactory;
import org.emftext.language.java.members.MembersPackage;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.modifiers.Abstract;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.modifiers.Modifier;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modifiers.ModifiersPackage;
import org.emftext.language.java.modifiers.Private;
import org.emftext.language.java.modifiers.Protected;
import org.emftext.language.java.modifiers.Public;
import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersPackage;
import org.emftext.language.java.parameters.Parametrizable;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.statements.StatementListContainer;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.Type;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

//TODO: Respect model writer some time
//TODO: NO error handling whatsoever right now - fix!

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class JavaDeltaDialectInterpreter extends JavaAbstractDeltaDialectInterpreter {

	private static <T extends EObject> T parseText(String text, EClass eClassForParsingRule) {
		//TODO: Implement
		//TODO: For abstract classes find the concrete subclasses with rules
		return null;
	}
	
	private static boolean createConcreteClassifierInPackage(ConcreteClassifier cc, Package p) {
		//TODO: Create file etc.
		return true;
	}
	
	private static boolean createConcreteClassifierInClass(ConcreteClassifier cc, Class c) {
		List<Member> members = c.getMembers();
		members.add(cc);
		return true;
	}
	
	private static boolean removeConcreteClassifier(ConcreteClassifier cc) {
		//TODO: Delete file etc.
		return true;
	}
	
	private static boolean refineImplementation(String text, StatementListContainer slc) {
		//TODO: parse to statements or block?!
		//TODO: respect "original();"
		
		return true;
	}
	
	private static ClassifierReference createClassifierReference(Classifier c) {
		ClassifierReference cr = TypesFactory.eINSTANCE.createClassifierReference();
		cr.setTarget(c);
		return cr;
	}

	private static boolean removeTypeFromTypeReferences(Type t, List<TypeReference> trs) {
		Iterator<TypeReference> iterator = trs.iterator();
		
		while (iterator.hasNext()) {
			TypeReference tr  = iterator.next();
			Type target = tr.getTarget();
			
			if (target == t) {
				iterator.remove();
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	@Override
	protected boolean interpretCreateAnnotation(DEModelWriter modelWriter, String aText, Annotable an) {
		AnnotationInstance ai = parseText(aText, AnnotationsPackage.eINSTANCE.getAnnotationInstance());
		return interpretAddAnnotation(modelWriter, ai, an);
	}

	@Override
	protected boolean interpretCreateImport(DEModelWriter modelWriter, String iText, ImportingElement ie) {
		Import i = parseText(iText, ImportsPackage.eINSTANCE.getImport());
		return interpretAddImport(modelWriter, i, ie);
	}

	@Override
	protected boolean interpretCreateClass(DEModelWriter modelWriter, String cText, Package p) {
		Class c = parseText(cText, ClassifiersPackage.eINSTANCE.getClass_());
		return createConcreteClassifierInPackage(c, p);
	}

	@Override
	protected boolean interpretRemoveClass(DEModelWriter modelWriter, Class c) {
		return removeConcreteClassifier(c);
	}

	@Override
	protected boolean interpretSetSuperClassForClass(DEModelWriter modelWriter, Class sc, Class c) {
		ClassifierReference cr = createClassifierReference(sc);
		return interpretSetSuperClassReferenceForClass(modelWriter, cr, c);
	}
	
	@Override
	protected boolean interpretAddSuperInterfaceForClass(DEModelWriter modelWriter, Interface i, Class c) {
		ClassifierReference cr = createClassifierReference(i);
		return interpretAddSuperInterfaceReferenceForClass(modelWriter, cr, c);
	}

	@Override
	protected boolean interpretRemoveSuperInterfaceForClass(DEModelWriter modelWriter, Interface i, Class c) {
		List<TypeReference> trs = c.getImplements();
		return removeTypeFromTypeReferences(i, trs);
	}
	
	@Override
	protected boolean interpretCreateInternalClass(DEModelWriter modelWriter, String cText, Class cc) {
		Class c = parseText(cText, ClassifiersPackage.eINSTANCE.getClass_());
		return createConcreteClassifierInClass(c, cc);
	}

	@Override
	protected boolean interpretCreateInternalInterface(DEModelWriter modelWriter, String iText, Class cc) {
		Interface i = parseText(iText, ClassifiersPackage.eINSTANCE.getInterface());
		return createConcreteClassifierInClass(i, cc);
	}
	
	@Override
	protected boolean interpretCreateInternalEnumeration(DEModelWriter modelWriter, String eText, Class cc) {
		Enumeration e = parseText(eText, ClassifiersPackage.eINSTANCE.getEnumeration());
		return createConcreteClassifierInClass(e, cc);
	}
	
	@Override
	protected boolean interpretCreateInterface(DEModelWriter modelWriter, String iText, Package p) {
		Interface i = parseText(iText, ClassifiersPackage.eINSTANCE.getInterface());
		return createConcreteClassifierInPackage(i, p);
	}

	@Override
	protected boolean interpretRemoveInterface(DEModelWriter modelWriter, Interface i) {
		return removeConcreteClassifier(i);
	}

	@Override
	protected boolean interpretAddSuperInterfaceForInterface(DEModelWriter modelWriter, Interface si, Interface i) {
		ClassifierReference cr = createClassifierReference(i);
		return interpretAddSuperInterfaceReferenceForInterface(modelWriter, cr, i);
	}

	@Override
	protected boolean interpretRemoveSuperInterfaceForInterface(DEModelWriter modelWriter, Interface si, Interface i) {
		List<TypeReference> trs = i.getExtends();
		return removeTypeFromTypeReferences(si, trs);
	}
	
	@Override
	protected boolean interpretCreateEnumeration(DEModelWriter modelWriter, String eText, Package p) {
		Enumeration e = parseText(eText, ClassifiersPackage.eINSTANCE.getEnumeration());
		return createConcreteClassifierInPackage(e, p);
	}

	@Override
	protected boolean interpretRemoveEnumeration(DEModelWriter modelWriter, Enumeration e) {
		return removeConcreteClassifier(e);
	}
	
	@Override
	protected boolean interpretAddSuperInterfaceForEnumeration(DEModelWriter modelWriter, Interface i, Enumeration e) {
		ClassifierReference cr = createClassifierReference(i);
		return interpretAddSuperInterfaceReferenceForEnumeration(modelWriter, cr, e);
	}

	@Override
	protected boolean interpretRemoveSuperInterfaceForEnumeration(DEModelWriter modelWriter, Interface i, Enumeration e) {
		List<TypeReference> trs = e.getImplements();
		return removeTypeFromTypeReferences(i, trs);
	}
	
	@Override
	protected boolean interpretCreateEnumConstant(DEModelWriter modelWriter, String ecText, Enumeration e) {
		EnumConstant ec = parseText(ecText, MembersPackage.eINSTANCE.getEnumConstant());
		return interpretAddEnumConstant(modelWriter, ec, e);
	}

	@Override
	protected boolean interpretCreateModifier(DEModelWriter modelWriter, String mText, AnnotableAndModifiable aam) {
		Modifier m = parseText(mText, ModifiersPackage.eINSTANCE.getModifier());
		return interpretAddModifier(modelWriter, m, aam);
	}

	@Override
	protected boolean interpretSetAbstractModifier(DEModelWriter modelWriter, Boolean abstractValue, AnnotableAndModifiable aam) {
		aam.removeModifier(Abstract.class);
		
		if (abstractValue) {
			aam.addModifier(ModifiersFactory.eINSTANCE.createAbstract());
		}
		
		return true;
	}

	@Override
	protected boolean interpretSetStaticModifier(DEModelWriter modelWriter, Boolean staticValue, AnnotableAndModifiable aam) {
		aam.removeModifier(Static.class);
		
		if (staticValue) {
			aam.addModifier(ModifiersFactory.eINSTANCE.createStatic());
		}
		
		return true;
	}

	@Override
	protected boolean interpretSetPublicVisbility(DEModelWriter modelWriter, AnnotableAndModifiable aam) {
		aam.makePublic();
		return true;
	}

	@Override
	protected boolean interpretSetProtectedVisibility(DEModelWriter modelWriter, AnnotableAndModifiable aam) {
		aam.makeProtected();
		return true;
	}

	@Override
	protected boolean interpretSetPrivateVisibility(DEModelWriter modelWriter, AnnotableAndModifiable aam) {
		aam.makePrivate();
		return true;
	}

	@Override
	protected boolean interpretSetPackageVisibility(DEModelWriter modelWriter, AnnotableAndModifiable aam) {
		//Package visibility is implicit if no explicit visibility is specified.
		aam.removeModifier(Public.class);
		aam.removeModifier(Protected.class);
		aam.removeModifier(Private.class);
		return false;
	}

	@Override
	protected boolean interpretCreateField(DEModelWriter modelWriter, String fText, MemberContainer mc) {
		Field f = parseText(fText, MembersPackage.eINSTANCE.getField());
		return interpretAddField(modelWriter, f, mc);
	}

	@Override
	protected boolean interpretCreateConstructor(DEModelWriter modelWriter, String cText, MemberContainer mc) {
		Constructor c = parseText(cText, MembersPackage.eINSTANCE.getField());
		return interpretAddConstructor(modelWriter, c, mc);
	}

	@Override
	protected boolean interpretImplementConstructor(DEModelWriter modelWriter, Constructor c, String implementationText) {
		return refineImplementation(implementationText, c);
	}

	@Override
	protected boolean interpretCreateMethod(DEModelWriter modelWriter, String mText, MemberContainer mc) {
		Method m = parseText(mText, MembersPackage.eINSTANCE.getMethod());
		return interpretAddMethod(modelWriter, m, mc);
	}

	@Override
	protected boolean interpretImplementMethod(DEModelWriter modelWriter, ClassMethod cm, String implementationText) {
		return refineImplementation(implementationText, cm);
	}

	@Override
	protected boolean interpretCreateParameter(DEModelWriter modelWriter, String pText, Parametrizable pt, Integer index) {
		Parameter p = parseText(pText, ParametersPackage.eINSTANCE.getParameter());
		return interpretInsertParameter(modelWriter, p, pt, index);
	}
	
	
	//Refactorings
	
	@Override
	protected boolean interpretExtractMethod(DEModelWriter modelWriter, String newMethodName, ClassMethod containingMethod, Integer startStatementIndex, Integer endStatementIndex) {
		Class containingClass = (Class) containingMethod.getContainingConcreteClassifier();
		
		//Create the new method in the same class as the containing method
		ClassMethod newMethod = MembersFactory.eINSTANCE.createClassMethod();
		newMethod.setName(newMethodName);
		containingClass.getMembers().add(newMethod);
		
		//TODO: determine parameters some time
		
		
		//Move statements to new method
		List<Statement> statements = containingMethod.getStatements();
		List<Statement> affectedStatements = statements.subList(startStatementIndex, endStatementIndex);
		newMethod.getStatements().addAll(affectedStatements);

		//Add call at original site
		MethodCall newMethodCall = ReferencesFactory.eINSTANCE.createMethodCall();
		newMethodCall.setTarget(newMethod);
		//TODO: determine arguments some time.
		
		ExpressionStatement newMethodCallExpressionStatement = StatementsFactory.eINSTANCE.createExpressionStatement();
		newMethodCallExpressionStatement.setExpression(newMethodCall);
		statements.add(startStatementIndex, newMethodCallExpressionStatement);

		return true;
	}

	@Override
	protected boolean interpretExtractSuperInterface(DEModelWriter modelWriter, String newInterfaceName, ConcreteClassifier containingClassOrInterface, Member fieldsAndMethods) {
		// TODO Auto-generated method stub
		
		//Requires creation of a file
		return true;
	}

	@Override
	protected boolean interpretExtractSuperClass(DEModelWriter modelWriter, String newClassName, ConcreteClassifier containingClassOrInterface, Member fieldsAndMethods) {
		// TODO Auto-generated method stub
		
		//Requires creation of a file
		
		return true;
	}
}