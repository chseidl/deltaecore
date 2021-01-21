package org.deltaecore.core.decore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEStatement;
import org.deltaecore.core.decore.DEStructuralFeatureReference;
import org.deltaecore.core.decore.DEVariableDeclaration;
import org.deltaecore.core.decore.DEVariableReference;
import org.deltaecore.core.decore.DEVirtualConstructorCall;
import org.deltaecore.core.decorebase.DEMetaModelClassifierReference;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEDEcoreResolverUtil extends DEDEcoreBaseUtil {
	
	public static List<DEDelta> getRequiredDeltas(DEDelta delta) {
		List<DEDelta> allRequiredDeltas = new ArrayList<DEDelta>();
		
		Resource resource = delta.eResource();
		ResourceSet resourceSet = resource.getResourceSet();
		
		List<DEDeltaBlock> blocks = delta.getBlocks();
		
		for (DEDeltaBlock block : blocks) {
			List<DEDelta> requiredDeltasOfBlock = doGetRequiredDeltas(block, resourceSet);
			allRequiredDeltas.addAll(requiredDeltasOfBlock);
		}
		
		return allRequiredDeltas;
	}
	
	public static List<DEDelta> getRequiredDeltas(DEDeltaBlock block) {
		Resource resource = block.eResource();
		ResourceSet resourceSet = resource.getResourceSet();
		
		return doGetRequiredDeltas(block, resourceSet);
	}
	
	private static List<DEDelta> doGetRequiredDeltas(DEDeltaBlock block, ResourceSet resourceSet) {
		List<DEDelta> allRequiredDeltas = new ArrayList<DEDelta>();
		List<DERelativeFilePath> allRequiredElementPaths = block.getRequiredElementRelativeFilePaths();
		
		for (DERelativeFilePath requiredElementPath : allRequiredElementPaths) {
			DEDelta requiredDelta = DEDEcoreIOUtil.loadDeltaFromRelativeFilePath(requiredElementPath, resourceSet);
			allRequiredDeltas.add(requiredDelta);
		}
		
		return allRequiredDeltas;
	}

	public static DEDeltaBlock getContainingBlock(EObject element) {
		while(element != null) {
			if (element instanceof DEDeltaBlock) {
				return (DEDeltaBlock) element;
			}
			
			element = element.eContainer();
		}
		
		return null;
	}
	
	public static List<DEDelta> getAllTransitivelyRequiredDeltas(DEDelta delta) throws DEDeltaRequirementsCycleException {
		List<DEDelta> requiredDeltas = new ArrayList<DEDelta>();
		List<DEDelta> processedDeltas = new ArrayList<DEDelta>();
		
		doGetAllTransitivelyRequiredDeltas(delta, delta, true, processedDeltas, requiredDeltas);
		
		return requiredDeltas;
	}
	
	private static void doGetAllTransitivelyRequiredDeltas(DEDelta delta, DEDelta originalDelta, boolean isOriginalCall, List<DEDelta> processedDeltas, List<DEDelta> transitivelyRequiredDeltas) throws DEDeltaRequirementsCycleException {
		if (!isOriginalCall && delta == originalDelta) {
			//Already processes this delta but we are here again.
			//This means, a cycle exists. Throw and notify.
			int index = processedDeltas.indexOf(delta);
			//Create a copy as subList() only creates a view of the original list.
			List<DEDelta> requirementsCycle = new ArrayList<DEDelta>(processedDeltas.subList(index, processedDeltas.size()));
			throw new DEDeltaRequirementsCycleException(originalDelta, requirementsCycle);
		}

		//Add self
		transitivelyRequiredDeltas.add(delta);
		processedDeltas.add(delta);
		
		//Add further required deltas
		List<DEDelta> directlyRequiredElements = getRequiredDeltas(delta);
		
		for (DEDelta directlyRequiredDelta : directlyRequiredElements) {
			doGetAllTransitivelyRequiredDeltas(directlyRequiredDelta, originalDelta, false, processedDeltas, transitivelyRequiredDeltas);
		}
	}

	public static List<Resource> collectModifiedResources(DEDelta delta) {
		return collectModifiedResources(Collections.singletonList(delta));
	}
	
//	public static List<Resource> collectModifiedResources(List<DEDelta> deltas) {
//		List<Resource> modifiedResources = new LinkedList<Resource>();
//		
//		for (DEDelta delta : deltas) {
//			List<DEDeltaBlock> blocks = delta.getBlocks();
//			
//			for (DEDeltaBlock block : blocks) {
//				List<DERelativeFilePath> modifiedElementRelativeFilePaths = block.getModifiedElementRelativeFilePaths();
//				
//				for (DERelativeFilePath relativeFilePath : modifiedElementRelativeFilePaths) {
//					ResourceSet resourceSet = relativeFilePath.eResource().getResourceSet();
//					
//					try {
//						Resource modifiedResource = DEDEcoreIOUtil.getResourceFromRelativeFilePath(relativeFilePath, resourceSet, true);
//						
//						if (!modifiedResources.contains(modifiedResource)) {
//							modifiedResources.add(modifiedResource);
//						}
//					} catch(Exception e) {
////						DEDebug.println("Couldn't load \"" + relativeFilePath.getRawRelativeFilePath() + "\".");
////						e.printStackTrace();
//						//This may happen for resources that are created as part of variant generation and referenced in the modifies clause of a subsequent delta module.
//						//This behavior is ok and intended.
//					}
//				}
//			}
//		}
//		
//		return modifiedResources;
//	}
	
	public static List<Resource> collectModifiedResources(List<DEDelta> deltas) {
		List<Resource> modifiedResources = new LinkedList<Resource>();
		List<DERelativeFilePath> modifiedElementRelativeFilePaths = collectModifiedElementRelativeFilePaths(deltas);
				
		for (DERelativeFilePath relativeFilePath : modifiedElementRelativeFilePaths) {
			ResourceSet resourceSet = relativeFilePath.eResource().getResourceSet();
			
			try {
				Resource modifiedResource = DEDEcoreIOUtil.getResourceFromRelativeFilePath(relativeFilePath, resourceSet, true);
				
				if (modifiedResource != null && !modifiedResources.contains(modifiedResource)) {
					modifiedResources.add(modifiedResource);
				}
			} catch(Exception e) {
//				e.printStackTrace();
				//NOTE: If the intended resources fails to load, there might still be a memory instance of the resource in the resource set.
				//This may cause problems later on.
				//Intended use of this method is to avoid this problem altogether but a fix in this exception handling code might be in order, too.
				return null;
			}
		}
		
		return modifiedResources;
	}
	
	public static List<DERelativeFilePath> collectModifiedElementRelativeFilePaths(List<DEDelta> deltas) {
		return doCollectRelativeFilePaths(deltas, true);
	}
	
	public static List<DERelativeFilePath> collectCreatedElementRelativeFilePaths(List<DEDelta> deltas) {
		return doCollectRelativeFilePaths(deltas, false);
	}
	
	//Either modified or created resources
	private static List<DERelativeFilePath> doCollectRelativeFilePaths(List<DEDelta> deltas, boolean modified) {
		List<DERelativeFilePath> allRelativeFilePaths = new LinkedList<DERelativeFilePath>();
		
		for (DEDelta delta : deltas) {
			List<DEDeltaBlock> blocks = delta.getBlocks();
			
			for (DEDeltaBlock block : blocks) {
				List<DERelativeFilePath> relativeFilePaths = modified ? block.getModifiedElementRelativeFilePaths() : block.getCreatedElementRelativeFilePaths();
				
				allRelativeFilePaths.addAll(relativeFilePaths);
			}
		}
		
		return allRelativeFilePaths;
	}
	
	
	
	
	
	
	private static Set<EObject> getAllReferencableModels(DEDelta delta) throws DEDeltaRequirementsCycleException {
		List<Resource> modifiedResources = collectModifiedResources(delta);
		Set<EObject> referencableModels = new HashSet<EObject>();
		
		for (Resource modifiedResource : modifiedResources) {
			List<EObject> contents = modifiedResource.getContents();
			referencableModels.addAll(contents);
		}
		
		//Also look in the domain meta model specified in the notation definition.
		List<DEDeltaBlock> blocks = delta.getBlocks();
		
		for (DEDeltaBlock block : blocks) {
			DEDeltaDialect deltaDialect = block.getDeltaDialect();
			EPackage metamodel = deltaDialect.getDomainPackage();
			referencableModels.add(metamodel);
			
			//Also add all (indirectly) referenced metamodels as candidates for resolution.
			//TODO: This probably needs some kind of caching mechanism as it is really taxing to process each metamodel element on each resolution of an identifier.
			Set<EPackage> recursivelyReferencedMetamodels = EcoreResolverUtil.resolveAllRecursivelyReferencedMetamodels(metamodel);
			referencableModels.addAll(recursivelyReferencedMetamodels);
		}
		
		return referencableModels;
	}
	
	public static DEVariableDeclaration resolveVariableReference(String identifier, DEVariableReference incompleteVariableReference) {
		//No scoping whatsoever
		DEDeltaBlock block = getContainingBlock(incompleteVariableReference);
//		DEDelta delta = (DEDelta) EcoreUtil.getRootContainer(incompleteVariableReference);
		List<DEStatement> statements = block.getStatements();
		
		DEStatement variableReferenceStatement = findStatementForExpression(incompleteVariableReference);
		
		if (variableReferenceStatement == null) {
			System.err.println("Variable reference statement is null.");
			return null;
		}
		
		if (!statements.contains(variableReferenceStatement)) {
			System.err.println("Unable to find variable reference statement in statements of delta.");
			return null;
		}
		
		int index = statements.indexOf(variableReferenceStatement);
		int i = 0;
		
		for (DEStatement statement : statements) {
			//Variables can only be reference if they have been declared _before_ use.
			//Hence, only look up the declaration before the reference.
			if (i > index) {
				break;
			}
			
			if (statement instanceof DEVariableDeclaration) {
				DEVariableDeclaration variableDeclaration = (DEVariableDeclaration) statement;
				String variableName = variableDeclaration.getName();
				
				if (variableName != null && variableName.equals(identifier)) {
					return variableDeclaration;
				}
			}
			
			i++;
		}
		
		return null;
	}
	
	private static DEStatement findStatementForExpression(DEExpression2 expression) {
		EObject container = expression;
		
		while (container != null) {
			if (container instanceof DEStatement) {
				return (DEStatement) container;
			}
			
			container = container.eContainer();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static<T extends EClassifier> List<T> resolveEClassifiers(String identifier, DEDelta delta) {
		List<T> eClassifiers = new ArrayList<T>();
		
		if (identifier == null) {
			return eClassifiers;
		}
		
		try {
			Set<EObject> referenceableModels = getAllReferencableModels(delta);
			
			for (EObject referenceableModel : referenceableModels) {
				Iterator<EObject> iterator = referenceableModel.eAllContents();
				
				while(iterator.hasNext()) {
					EObject eObject = iterator.next();
					
					if (eObject instanceof EClassifier) {
						EClassifier eClassifier = (EClassifier) eObject;
						String name = eClassifier.getName();
						
						if (identifier.equals(name)) {
							try {
								T typedEClassifier = (T) eObject;
								eClassifiers.add(typedEClassifier);
							} catch(ClassCastException e) {
								//Swallow that.
							}
						}
					}
				}
			}
			
		} catch(DEDeltaRequirementsCycleException e) {
			e.printStackTrace();
		}
		
		return eClassifiers; 
	}
	
	public static EDataType resolveEDataType(String identifier, DEDelta delta) {
		List<EDataType> eDataTypes = resolveEClassifiers(identifier, delta);
		
		//EEnums are a subclass of EDataType but are handled differently.
		removeEEnums(eDataTypes);
		
		if (eDataTypes.isEmpty()) {
			return null;
		}
		
		if (eDataTypes.size() > 1) {
			//TODO: Ambiguous reference
		}
		
		return eDataTypes.get(0);
	}
	
	private static void removeEEnums(List<EDataType> eDataTypes) {
		Iterator<EDataType> iterator = eDataTypes.iterator();
		
		while(iterator.hasNext()) {
			EDataType eDataType = iterator.next();
			
			if (eDataType instanceof EEnum) {
				iterator.remove();
			}
		}
	}
	
	public static EEnum resolveEEnum(String identifier, DEDelta delta) {
		List<EEnum> eEnums = resolveEClassifiers(identifier, delta);
		
		if (eEnums.isEmpty()) {
			return null;
		}
		
		if (eEnums.size() > 1) {
			//TODO: Ambiguous reference
		}
		
		return eEnums.get(0);
	}
	
//	public static EEnum resolveEEnum(String identifier, DEDelta delta) {
//		if (identifier == null) {
//			return null;
//		}
//		
//		try {
//			List<EObject> referenceableModels = getAllReferencableModels(delta);
//			
//			for (EObject referenceableModel : referenceableModels) {
//				Iterator<EObject> iterator = referenceableModel.eAllContents();
//				
//				while(iterator.hasNext()) {
//					EObject eObject = iterator.next();
//					
//					//TODO: Prepare for ambiguous reference
//					
//					if (eObject instanceof EEnum) {
//						EEnum eEnum = (EEnum) eObject;
//						String enumName = eEnum.getName();
//						
//						if (identifier.equals(enumName)) {
//							return eEnum;
//						}
//					}
//				}
//			}
//			
//		} catch(DEDeltaRequirementsCycleException e) {
//			e.printStackTrace();
//		}
//		
//		return null; 
//	}
	
	public static EEnumLiteral resolveEnumLiteral(String identifier, EEnum eEnum) {
		if (identifier == null || eEnum == null) {
			return null;
		}
		
		List<EEnumLiteral> literals = eEnum.getELiterals();
		
		for (EEnumLiteral literal : literals) {
			String name = literal.getName();
			
			if (identifier.equals(name)) {
				return literal;
			}
		}
		
		return null;
	}
	
	public static EStructuralFeature resolveStructuralFeatureInNamedArgument(String structuralFeatureName, DEStructuralFeatureReference structuralFeatureReference) {
		DEVirtualConstructorCall virtualConstructorCall = structuralFeatureReference.getVirtualConstructorCall();
		DEMetaModelClassifierReference type = virtualConstructorCall.getType();
		
		return DEDEcoreBaseUtil.resolveStructuralFeatureInMetaModelClassifierReference(structuralFeatureName, type);
	}
}
