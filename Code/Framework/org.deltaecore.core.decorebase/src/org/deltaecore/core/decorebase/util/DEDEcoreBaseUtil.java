package org.deltaecore.core.decorebase.util;

import java.util.Collection;
import java.util.List;

import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.DEcoreBaseFactory;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEDEcoreBaseUtil {
	//Resolution
	
	//Equivalent EObjects may have different instance objects (memory locations) due to
	//the way loading works. Still have to find the equivalent but cannot depend on simple
	//equality (which only checks pointer equality for EObjects). ECoreUtil.equals(..) is
	//Too expensive for the general case as it compares ENTIRE models to each other.
	public static boolean isSimilar(EObject eObject1, EObject eObject2) {
		if (eObject1 == eObject2) {
			//Take pointer equality with a kiss!
			return true;
		}
		
		if (eObject1 == null || eObject2 == null) {
			return false;
		}
		
		Resource resource1 = eObject1.eResource();
		Resource resource2 = eObject2.eResource();
		
		if (resource1 != null && resource2 != null) {
			URI uri1 = resource1.getURI();
			URI uri2 = resource2.getURI();
			
			//TODO: Can URIs be in different format?! If so, bring to canonical form first.
			return uri1.equals(uri2);
		}
		
		//Fallback solution if at least one of the EObjects is not contained within a resource.
		return EcoreUtil.equals(eObject1, eObject2);
		
//		throw new UnsupportedOperationException("Comparing eObjects using their resource URI failed.");
	}
	
	public static boolean containsSimilar(EObject eObject, Collection<? extends EObject> otherEObjects) {
		return (findSimilar(eObject, otherEObjects) != null);
	}
	
	public static<T extends EObject> T findSimilar(EObject eObject, Collection<T> otherEObjects) {
		for (T otherEObject : otherEObjects) {
			if (isSimilar(eObject, otherEObject)) {
				return otherEObject;
			}
		}
		
		return null;
	}
	
	private static EClassifier doResolveEClassifierIdentifierInMetaModel(String eClassifierIdentifier, EPackage metaModel) {
		return metaModel.getEClassifier(eClassifierIdentifier);
	}
	
	public static EClassifier resolveEClassifierIdentifierInMetaModel(String eClassifierIdentifier, EPackage metaModel) {
		if (metaModel == null) {
			return null;
		}
		
		
		EClassifier eClassifier = doResolveEClassifierIdentifierInMetaModel(eClassifierIdentifier, metaModel);
		
		if (eClassifier != null) {
			return eClassifier;
		}

		
		//Traverse sub packages.
		List<EPackage> subPackages = metaModel.getESubpackages();
		
		for (EPackage subPackage : subPackages) {
			eClassifier = doResolveEClassifierIdentifierInMetaModel(eClassifierIdentifier, subPackage);
			
			if (eClassifier != null) {
				return eClassifier;
			}
		}
		
		
		//TODO: Should I do this recursively?! - probably
		//A metamodel may reference other metamodels and use their classes. Traverse the referenced metamodels as well.
		List<EPackage> referencedMetaModels = EcoreResolverUtil.resolveReferencedMetaModels(metaModel);
		
		for (EPackage referencedMetaModel : referencedMetaModels) {
			eClassifier = doResolveEClassifierIdentifierInMetaModel(eClassifierIdentifier, referencedMetaModel);
			
			if (eClassifier != null) {
				return eClassifier;
			}
		}
		
		return null;
	}
	
	
	
	


	
	
	public static Class<?> boxPrimitiveTypeIfNecessary(Class<?> type) {
		if (type == null) {
			return null;
		}
		
		if (type.isPrimitive()) {
			if (type.equals(Boolean.TYPE)) {
				return Boolean.class;
			}
			
			if (type.equals(Character.TYPE)) {
				return Character.class;
			}
			
			if (type.equals(Byte.TYPE)) {
				return Byte.class;
			}
			
			if (type.equals(Short.TYPE)) {
				return Short.class;
			}
			
			if (type.equals(Integer.TYPE)) {
				return Integer.class;
			}
			
			if (type.equals(Long.TYPE)) {
				return Long.class;
			}
			
			if (type.equals(Float.TYPE)) {
				return Float.class;
			}
			
			if (type.equals(Double.TYPE)) {
				return Double.class;
			}
			
			if (type.equals(Void.TYPE)) {
				return Void.class;
			}
		}
		
		return type;
	}
	
	public static EStructuralFeature resolveStructuralFeatureInMetaModelClassifierReference(String structuralFeatureName, DEMetaModelClassifierReference type) {
		EClassifier classifier = type.getClassifier();
		
		if (classifier == null) {
			return null;
		}
		
		if (classifier instanceof EClass) {
			EClass eClass = (EClass) classifier;
			return eClass.getEStructuralFeature(structuralFeatureName);
		}
		
		return null;
	}
	
	
	//Creation
	
	public static DEMetaModelClassifierReference createTypeFromEObject(EObject element) {
		DEMetaModelClassifierReference type = DEcoreBaseFactory.eINSTANCE.createDEMetaModelClassifierReference();
		EClass elementEClass = element.eClass();
		type.setClassifier(elementEClass);
		
		return type;
	}
	
	
	//Validation
	
	private static final String[] reservedKeywords = new String[] {
		//DEcoreBase
		"Boolean",
		"Integer",
		"Double",
		"String",
		
		//DEcoreDialect
		"deltaDialect",
		"configuration",
		"metaModel",
		"domainModelElementIdentifierResolver",
		"deltaOperations",
		"deltaOperation",
		
		//DEcore
		"abstract",
		"delta",
		"requires",
		"new",
		
		//Java (illegal because those would cause trouble after conversion)
		"abstract",
		"continue",
		"for",
		"new",
		"switch",
		"assert",
		"default",
		"goto",
		"package",
		"synchronized",
		"boolean",
		"do",
		"if",
		"private",
		"this",
		"break",
		"double",
		"implements",
		"protected",
		"throw",
		"byte",
		"else",
		"import",
		"public",
		"throws",
		"case",
		"enum",
		"instanceof",
		"return",
		"transient",
		"catch",
		"extends",
		"int",
		"short",
		"try",
		"char",
		"final",
		"interface",
		"static",
		"void",
		"class",
		"finally",
		"long",
		"strictfp",
		"volatile",
		"const",
		"float",
		"native",
		"super",
		"while",
	};
	
	
	public static boolean isReservedKeyword(String identifier) {
		//Check for reserved keywords
		for (String reservedKeyword : reservedKeywords) {
			if (reservedKeyword.equals(identifier)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isValidIdentifier(String identifier) {
		if (identifier == null) {
			return false;
		}
		
		if (!identifier.matches("[A-Za-z][A-Za-z0-9_]*")) {
			return false;
		}
		
		if (isReservedKeyword(identifier)) {
			return false;
		}
		
		return true;
	}
	
	public static String makeValidIdentifier(String potentiallyInvalidIdentifier) {
		if (potentiallyInvalidIdentifier == null || potentiallyInvalidIdentifier.isEmpty()) {
			potentiallyInvalidIdentifier = "missingIdentifier";
		}
		
		//Replace invalid characters in identifier.
		//Identifiers only consist of [A-Za-z0-9_] and have to start with [A-Za-z]
		potentiallyInvalidIdentifier = potentiallyInvalidIdentifier.replaceAll("([^A-Za-z0-9_])", "_");
		potentiallyInvalidIdentifier = potentiallyInvalidIdentifier.replaceFirst("^([^A-Za-z])*", "");
		
		return potentiallyInvalidIdentifier;
	}
	
	//TODO: This might be a more elegant implementation but not tested yet.
//	public static IFile getFileFromRelativeFilePath(DERelativeFilePath relativeFilePath) {
//		IFile modelFile = EcoreResolverUtil.resolveRelativeFileFromEObject(relativeFilePath);
//		IPath modelFilePath = modelFile.getFullPath();
//		
//		String rawRelativeFilePath = relativeFilePath.getRawRelativeFilePath();
//		IPath filePath = modelFilePath.append(rawRelativeFilePath);
//		
//		IProject project = modelFile.getProject();
//		return project.getFile(filePath);
//	}
	
	public static IFile getFileFromRelativeFilePath(DERelativeFilePath relativeFilePath) {
		Resource resource = relativeFilePath.eResource();
		IFile resourceFile = EcoreResolverUtil.resolveRelativeFileFromResource(resource);
		IContainer resourceContainer = resourceFile.getParent();
		
		String rawRelativeFilePath = relativeFilePath.getRawRelativeFilePath();
		IPath resourceContainerPath = resourceContainer.getFullPath();
		IPath filePath = resourceContainerPath.append(rawRelativeFilePath);
		
		IWorkspace workspace = resourceFile.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		IFile file = workspaceRoot.getFile(filePath);
		
		if (file == null) {
			throw new UnsupportedOperationException("Unresolvable relative file path \"" + relativeFilePath.getRawRelativeFilePath() + "\" during variant derivation.");
		}
		
		return file;
	}
}
