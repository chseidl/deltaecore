package org.emftext.sdk.concretesyntax.delta.resolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deltaecore.core.extension.resolution.DEAbstractCompilingDomainModelElementIdentifierResolver;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Rule;

public class ConcreteSyntaxIDResolver extends DEAbstractCompilingDomainModelElementIdentifierResolver<CompiledConcreteSyntaxID> {
	private static List<Class<?>> supportedClasses = new ArrayList<Class<?>>();
	
	static {
		supportedClasses.add(ConcreteSyntax.class);
		supportedClasses.add(Rule.class);
	}
	
	@Override
	public String deresolveDomainModelElement(EObject domainModelElement) {
		String elementType = getElementTypeForClass(domainModelElement.getClass());
		String elementIdentifier = getElementIdentifier(domainModelElement);
		
		if (elementType != null && elementIdentifier != null) {
			return elementType + ":" + elementIdentifier;
		}
		
		return null;
	}

	@Override
	protected CompiledConcreteSyntaxID compileIdentifier(String rawIdentifier) {
		String[] rawIdentifierParts = rawIdentifier.split(":");
		
		if (rawIdentifierParts != null && rawIdentifierParts.length == 2) {
			String elementType = rawIdentifierParts[0];
			String elementIdentifier = rawIdentifierParts[1];
			
			return new CompiledConcreteSyntaxID(elementType, elementIdentifier);
		}
		
		return null;
	}

	@Override
	protected void resolveIdentifier(CompiledConcreteSyntaxID identifier, List<EObject> referencableModels, List<EObject> resolvedElements) {
		String elementType = identifier.getElementType();
		String elementIdentifier = identifier.getElementIdentifier();
		
		if (elementType == null || elementIdentifier == null) {
			return;
		}
		
		for (EObject referencableModel : referencableModels) {
			if (referencableModel instanceof ConcreteSyntax) {
				//Also include the root element itself.
				Resource resource = referencableModel.eResource();
				Iterator<EObject> iterator = resource.getAllContents();
				
				while(iterator.hasNext()) {
					EObject element = iterator.next();
					String currentElementIdentifier = getElementIdentifier(element);
					
					if (isRightType(elementType, element) && elementIdentifier.equals(currentElementIdentifier)) {
						resolvedElements.add(element);
					}
				}
			}
		}
	}

	private static String getElementTypeForClass(Class<?> c) {
		if (c == null) {
			return null;
		}
		
		for (Class<?> supportedClass : supportedClasses) {
			if (supportedClass.isAssignableFrom(c)) {
				return supportedClass.getSimpleName();
			}
		}
		
		return null;
	}
	
	private static Class<?> getClassForElementType(String elementType) {
		if (elementType == null) {
			return null;
		}
		
		for (Class<?> supportedClass : supportedClasses) {
			String supportedElementType = supportedClass.getSimpleName();
			
			if (supportedElementType.equals(elementType)) {
				return supportedClass;
			}
		}
		
		return null;
	}
	
	private static boolean isRightType(String elementType, EObject element) {
		Class<?> c = getClassForElementType(elementType);
		Class<?> ec = element.getClass();
		
		if (c != null && c.isAssignableFrom(ec)) {
			return true;
		}
		
		return false;
	}
	
	private static String getElementIdentifier(EObject element) {
		if (element instanceof ConcreteSyntax) {
			ConcreteSyntax concreteSyntax = (ConcreteSyntax) element;
			return concreteSyntax.getName();
		}
		
		if (element instanceof Rule) {
			Rule rule = (Rule) element;
			
			GenClass genClass = rule.getMetaclass();
			
			if (genClass == null) {
				return null;
			}
			
			EClass ecoreClass = genClass.getEcoreClass();
			
			if (ecoreClass == null) {
				return null;
			}
			
			return ecoreClass.getName();
		}
		
		return null;
	}
}
