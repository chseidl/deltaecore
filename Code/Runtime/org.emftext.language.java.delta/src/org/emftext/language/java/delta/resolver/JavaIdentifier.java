package org.emftext.language.java.delta.resolver;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.commons.Commentable;
import org.emftext.language.java.containers.Package;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;

//Ad hoc implementation, has room for improvement
//generalize protocol to type mapping
//package not working

public class JavaIdentifier {
	private static final String PROTOCOL_SEPARATOR = "::";
	
	private static final String MEMBER_SEPARATOR = "#";
	
	private String protocol; //package, class, interface, method, field, (???)
	private String identifier;
	

	
//	private static final Map<String, java.lang.Class<?>> protocolToElementTypeMapping = new HashMap<String, java.lang.Class<?>>();
//	
//	static {
//		protocolToElementTypeMapping.put("class", Class.class);
//	}
	
	//<protocol>::<identifier>
	public JavaIdentifier(String rawProtocolAndIdentifier) {
		parse(rawProtocolAndIdentifier);
	}
	
	private void parse(String rawProtocolAndIdentifier) {
		if (rawProtocolAndIdentifier == null) {
			return;
		}
		
		int separatorIndex = rawProtocolAndIdentifier.indexOf(PROTOCOL_SEPARATOR);
		
		if (separatorIndex == -1 || separatorIndex == 0 || separatorIndex == rawProtocolAndIdentifier.length() - PROTOCOL_SEPARATOR.length()) {
			return;
		}
		
		protocol = rawProtocolAndIdentifier.substring(0, separatorIndex).toLowerCase();
		identifier = rawProtocolAndIdentifier.substring(separatorIndex + PROTOCOL_SEPARATOR.length());
	}
	
	public EObject resolve(EObject model) {
		if (protocol == null) {
			return null;
		}
		
		Iterator<EObject> iterator = model.eAllContents();
		
		while(iterator.hasNext()) {
			EObject element = iterator.next();
		
			if ((protocol.equals("package") && element instanceof Package) ||
				(protocol.equals("class") && element instanceof Class) ||
				(protocol.equals("interface") && element instanceof Interface) ||
				(protocol.equals("method") && element instanceof Method) ||
				(protocol.equals("field") && element instanceof Field)) {
				
				String otherIdentifier = createIdentifier(element);
				
				if (identifier.equals(otherIdentifier)) {
					return element;
				}
			}
		}
		
		return null;
	}
	
	public static String deresolve(EObject element) {
		String protocol = createProtocol(element);
		String identifier = createIdentifier(element);
		
		if (protocol != null && identifier == null) {
			return protocol + PROTOCOL_SEPARATOR + identifier;
		}
		
		throw new UnsupportedOperationException();
	}
	
	private static String createProtocol(EObject element) {
		if (element instanceof Package) {
			return "pacakge";
		}
		
		if (element instanceof Class) {
			return "class";
		}
		
		if (element instanceof Interface) {
			return "interface";
		}
		
		if (element instanceof Method) {
			return "method";
		}
		
		if (element instanceof Field) {
			return "field";
		}
		
		return null;	
	}
	
	private static String createIdentifier(EObject element) {
		if (element instanceof Package) {
			Package p = (Package) element;
			return createPackageIdentifier(p);
		}
		
		if (element instanceof Class) {
			Class c = (Class) element;
			return createClassIdentifier(c);
		}
		
		if (element instanceof Interface) {
			Interface i = (Interface) element;
			return createInterfaceIdentifier(i);
		}
		
		if (element instanceof Method) {
			Method m = (Method) element;
			return createMethodIdentifier(m);
		}
		
		if (element instanceof Field) {
			Field f = (Field) element;
			return createFieldIdentifier(f);
		}
		
		return null;
	}
	
	private static String createPackageIdentifier(Commentable p) {
		String identifier = "";
		boolean isFirst = true;
		
		for (String containingPackageName : p.getContainingPackageName()) {
			if (!isFirst) {
				identifier += ".";			
			}
			
			identifier += containingPackageName;
			isFirst = false;
		}
		
		return identifier;
	}
		
	private static String createClassIdentifier(Class c) {
		return createConcreteClassifierIdentifier(c);
	}
	
	private static String createInterfaceIdentifier(Interface i) {
		return createConcreteClassifierIdentifier(i);
	}
	
	private static String createConcreteClassifierIdentifier(ConcreteClassifier c) {
		String identifier = createPackageIdentifier(c) + ".";
//		String identifier = "";
//		
//		for (String containingPackageName : c.getContainingPackageName()) {
//			identifier += containingPackageName;
//			identifier += ".";			
//		}
		
		identifier += c.getName();
		
		return identifier;
	}
	
	private static String createMethodIdentifier(Method m) {
		String identifier = createMemberIdentifier(m);
		
		identifier += "(";
		
		//TODO: Parameter types
//		for (Parameter parameter : m.getParameters()) {
//			TypeReference typeReference = parameter.getTypeReference();
//			Type type = typeReference.getTarget();
//			
//			//type.get
//		}
		
		identifier += ")";
		
		return identifier;
	}
	
	private static String createFieldIdentifier(Field f) {
		return createMemberIdentifier(f);
	}
	
	private static String createMemberIdentifier(Member member) {
		ConcreteClassifier classifier = member.getContainingConcreteClassifier();
		
		if (classifier instanceof Class) {
			Class c = (Class) classifier;
			String identifier = createClassIdentifier(c);
			
			identifier += MEMBER_SEPARATOR + member.getName();
			
			return identifier;
		} else {
			System.err.println("Found field with concreate classifier of type " + classifier.getClass());
		}

		return null;
	}
}
