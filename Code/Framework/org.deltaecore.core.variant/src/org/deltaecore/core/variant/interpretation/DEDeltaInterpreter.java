package org.deltaecore.core.variant.interpretation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decore.DEExpression2;
import org.deltaecore.core.decore.DEExpressionContainer;
import org.deltaecore.core.decore.DELiteral;
import org.deltaecore.core.decore.DEModelElementIdentifier;
import org.deltaecore.core.decore.DEStatement;
import org.deltaecore.core.decore.impl.custom.DEModelElementIdentifierImplCustom;
import org.deltaecore.core.decore.resource.decore.IDecoreLocationMap;
import org.deltaecore.core.decore.resource.decore.IDecoreTextResource;
import org.deltaecore.core.decore.util.DEDEcoreIOUtil;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.deltaecore.core.variant.call.DEDeltaOperationCallExecutor;
import org.deltaecore.core.variant.exception.DEDeltaInterpretationException;
import org.deltaecore.debug.DEDebug;
import org.deltaecore.extension.resolution.DEAmbiguousDomainModelElementIdentifierException;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.deltaecore.extension.resolution.DEUnresolvableDomainModelElementIdentifierException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.emftext.language.ecoreid.exception.UnresolvableIDException;
import org.emftext.language.ecoreid.resource.ecoreid.util.EcoreIDParserUtil;

import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEDeltaInterpreter {
	private Map<EObject, EObject> originalToCopyMap;

	
	protected DEDeltaOperationCallExecutor createDeltaOperationCallExecutor(String dialectNamespaceURI) {
		return new DEDeltaOperationCallExecutor(dialectNamespaceURI);
	}
	
	//TODO: Throw exception if fails
	public List<Resource> interpret(DEDelta delta, List<Resource> inputResources, Map<EObject, EObject> originalToCopyMap) {
		List<Resource> affectedResources = new LinkedList<Resource>(inputResources);
		
		this.originalToCopyMap = originalToCopyMap;
		
		clearErrorMarkers(delta);
		
		List<DEDeltaBlock> blocks = delta.getBlocks();
		List<Resource> resourcesInScope = new LinkedList<Resource>();

		for (DEDeltaBlock block : blocks) {
			List<Resource> createdResources = createResources(block);

			resourcesInScope.clear();
			resourcesInScope.addAll(inputResources);
			resourcesInScope.addAll(createdResources);
			
			interpretDeltaBlock(block, resourcesInScope);
			
			affectedResources.addAll(createdResources);
		}
		
		return affectedResources;
	}
	
	private List<Resource> createResources(DEDeltaBlock block) {
		List<Resource> createdResources = new LinkedList<Resource>();

		List<DERelativeFilePath> createElementRelativeFilePaths = block.getCreatedElementRelativeFilePaths();
		ResourceSet resourceSet = block.eResource().getResourceSet();

		for (DERelativeFilePath relativeFilePath : createElementRelativeFilePaths) {
			Resource createdResource = DEDEcoreIOUtil.createResourceFromRelativeFilePath(relativeFilePath, resourceSet, true);

			DEDebug.println("Created resource for \"" + relativeFilePath.getRawRelativeFilePath() + "\".");
			
			createdResources.add(createdResource);
		}

		return createdResources;
	}
	
	private boolean interpretDeltaBlock(DEDeltaBlock block, List<Resource> resourcesInScope) {
		DEDeltaDialect dialect = block.getDeltaDialect();

		if (dialect == null) {
			return false;
		}
		
		EPackage domainPackage = dialect.getDomainPackage();
		
		if (domainPackage == null) {
			return false;
		}
		
		String dialectNamespaceURI = domainPackage.getNsURI();

		DEDeltaOperationCallExecutor deltaOperationCallExecutor = createDeltaOperationCallExecutor(dialectNamespaceURI);
		// DEModelWriter modelWriter = deltaOperationCallExecutor.getModelWriter();

		// TODO: Re-enable locking once it works for all (generated) delta operations.
		// Have to lock all referenced elements that are not deltas
		// DEModelLocker.lockModels(affectedModels, modelWriter);


		//TODO: Create resources here!
		
		List<DEStatement> statements = block.getStatements();

		for (DEStatement statement : statements) {
			try {
				interpretStatement(deltaOperationCallExecutor, statement, resourcesInScope);
			} catch (Exception e) {
				DEDelta delta = block.getDelta();
				String message = "Problem occurred while interpreting delta \"" + delta.getName() + "\"";
	
				if (statement instanceof DEDeltaOperationCall) {
					DEDeltaOperationCall deltaOperationCall = (DEDeltaOperationCall) statement;
					
					message += " (" + deltaOperationCall.getOperationDefinition().getName() + ")";
				}
	
				message += ": " + e.getMessage();
	
				createErrorMarker(delta, message, false);
	
				return false;
			} finally {
				// DEModelLocker.unlockModels(affectedModels, modelWriter);
			}
		}

		return true;
	}
	
	public void interpretStatement(DEDeltaOperationCallExecutor deltaOperationCallExecutor, DEStatement statement, List<Resource> resourcesInScope) throws DEDeltaInterpretationException {
		DEDeltaBlock block = (DEDeltaBlock) statement.eContainer();
		
		if (!resolveAllModelElementIdentifiers(statement, resourcesInScope)) {
			stopInterpretation(block, "Unresolved domain model element identifier(s)");
		}

		if (!checkCompatibleValueTypeOfExpressionsInStatement(statement)) {
			stopInterpretation(block, "Incompatible type of expression");
		}
		
		if (statement instanceof DEDeltaOperationCall) {
			DEDeltaOperationCall deltaOperationCall = (DEDeltaOperationCall) statement;
			deltaOperationCallExecutor.interpretDeltaOperationCall(deltaOperationCall);
		}
	}
	
	
	private boolean resolveAllModelElementIdentifiers(DEStatement statement, List<Resource> resourcesInScope) {
		boolean result = true;
		Iterator<EObject> iterator = statement.eAllContents();

		while (iterator.hasNext()) {
			EObject eObject = iterator.next();

			if (eObject instanceof DEModelElementIdentifier) {
				DEModelElementIdentifierImplCustom modelElementIdentifier = (DEModelElementIdentifierImplCustom) eObject;
				EObject domainModelElement = null;

				try {
					domainModelElement = resolveDomainModelElementReference(modelElementIdentifier, resourcesInScope);
				} catch (DEUnresolvableDomainModelElementIdentifierException e) {
					
					String rawIdentifier = modelElementIdentifier.getRawIdentifier();
					DEDebug.println("Raw identifier: " + rawIdentifier);
					
					String message = "The domain model element identifier \"" + rawIdentifier + "\" could not be resolved.";
					createErrorMarker(modelElementIdentifier, message);

					result = false;
				} catch (DEAmbiguousDomainModelElementIdentifierException e) {
					
					String rawIdentifier = modelElementIdentifier.getRawIdentifier();
					String message = "The domain model element identifier \"" + rawIdentifier + "\" is ambiguous.";
					createErrorMarker(modelElementIdentifier, message);
					
					result = false;
				} finally {
					modelElementIdentifier.setCachedDomainModelElement(domainModelElement);
				}
			}
		}

		return result;
	}
	
	private static EObject resolveDomainModelElementReference(DEModelElementIdentifier identifier, List<Resource> resourcesInScope) throws DEUnresolvableDomainModelElementIdentifierException, DEAmbiguousDomainModelElementIdentifierException {
		String rawIdentifier = identifier.getRawIdentifier();

		if (resourcesInScope != null) {
			try {
				DEDeltaBlock block = DEDEcoreResolverUtil.getContainingBlock(identifier);

				// Resolve from registry in case a custom domain model element identifier reference resolver was registered.
				EPackage domainPackage = block.getDomainPackage();
				String namespaceURI = domainPackage.getNsURI();

				DEDomainModelElementIdentifierResolver domainModelElementIdentifierResolver = DEDeltaDialectExtensionRegistry.getDomainModelElementIdentifierResolver(namespaceURI);

				EObject resolvedEObject = domainModelElementIdentifierResolver.resolveDomainModelElementIdentifierToSingleEObjectFromResources(rawIdentifier, resourcesInScope);
				
				if (resolvedEObject != null) {
					return resolvedEObject;
				}
			} catch (UnresolvableIDException e) {
				// Just swallow this and make the next attempt.
			}
		}

		try {
			// Ecore is the common meta meta model. Hence, if resolution in the meta model fails,
			// try resolving in the meta meta model using EcoreID syntax. This may be required, e.g.,
			// when setting the type of attributes to a datatype defined in the source language's meta model
			// EcoreID is the right id type even though it is meant for meta models as Ecore is itself defined in Ecore.
			EPackage ecoreMetaModel = EcoreResolverUtil.resolveEPackageFromRegistry("http://www.eclipse.org/emf/2002/Ecore");
			return EcoreIDParserUtil.resolveEcoreIDString(rawIdentifier, ecoreMetaModel);
		} catch (UnresolvableIDException e) {
			// Just swallow this and handle error later on.
		}

		throw new DEUnresolvableDomainModelElementIdentifierException(rawIdentifier, resourcesInScope);
	}
	
	
	private void stopInterpretation(DEDeltaBlock block, String reason) {
		DEDelta delta = block.getDelta();
		throw new UnsupportedOperationException("Interpretation of delta \"" + delta.getName() + "\" canceled: " + reason);
	}
	
	private boolean checkCompatibleValueTypeOfExpressionsInStatement(DEStatement statement) {
		//The statement itself may be an expression container.
		if (statement instanceof DEExpressionContainer) {
			DEExpressionContainer expressionContainer = (DEExpressionContainer) statement;
			
			if (!checkCompatibleValueTypeOfExpressionInExpressionContainer(expressionContainer)) {
				return false;
			}
		}
		
		//The statement may contain further expression containers.
		Iterator<EObject> iterator = statement.eAllContents();

		while (iterator.hasNext()) {
			EObject eObject = iterator.next();

			if (eObject instanceof DEExpressionContainer) {
				DEExpressionContainer expressionContainer = (DEExpressionContainer) eObject;
				if (!checkCompatibleValueTypeOfExpressionInExpressionContainer(expressionContainer)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean checkCompatibleValueTypeOfExpressionInExpressionContainer(DEExpressionContainer expressionContainer) {
		DEExpression2 expression = expressionContainer.getExpression();
		
		Object value = expression.getValue();
		Class<?> valueJavaClass = expression.getValueJavaClass();
		Class<?> expectedJavaClass = expressionContainer.getExpectedJavaClass();
		
		if (!isCompatibleValueType(expectedJavaClass, valueJavaClass, value)) {
			String message = "The";
			
			if (expression instanceof DELiteral) {
				//Give explicit values for literals in error message.
				message += " value \"" + expression.getValue() + "\"";
			} else {
				//Stick with generic term "expression" for all non-literals.
				message += " expression";
			}
			
			if (valueJavaClass != null) {
				message += " of type \"" + valueJavaClass.getSimpleName() + "\"";
			}
			
			message += " does not match the expected type";
			
			if (expectedJavaClass != null) {
				message += " " + expectedJavaClass.getSimpleName();
			}
			
			message += ".";
			
			createErrorMarker(expression, message);
			return false;
		}
		
		return true;
	}
	
	private static boolean isCompatibleValueType(Class<?> expectedJavaClass, Class<?> valueJavaClass, Object value) {
		//No class provided. If this is because the value is null, it is OK
		//(as null can be any type of value).
		if (valueJavaClass == null) {
			return (value == null);
		}

		//Nothing expected, everything valid.
		if (expectedJavaClass == null) {
			return true;
		}
		
		//Return whether the value type is a subclass of the expected Java class or not.
		return expectedJavaClass.isAssignableFrom(valueJavaClass);
	}
	


	
	
	//Error marker handling
	//TODO: Externalize this some time (don't forget extension in plugin.xml)
	
	private void createErrorMarker(EObject eObject, String message) {
		createErrorMarker(eObject, message, true);	
	}
	
	private void createErrorMarker(EObject eObject, String message, boolean includePositionInformation) {
		//NOTE: Need the original object here as its resource holds the location map with information on location in text.
		EObject originalEObject = getOriginal(eObject);
		internalCreateErrorMarker(originalEObject, message, includePositionInformation);
	}
	
	public static final String PROBLEM_MARKER_ID = "org.deltaecore.problemmarker";
	
	private EObject getOriginal(EObject searchedCopy) {
		Set<Entry<EObject, EObject>> entrySet = originalToCopyMap.entrySet();
		
		for (Entry<EObject, EObject> entry : entrySet) {
			EObject copy = entry.getValue();
			
			if (copy == searchedCopy) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	private static void internalCreateErrorMarker(final EObject eObject, final String message, final boolean includePositionInformation) {
		final IFile file = EcoreResolverUtil.resolveRelativeFileFromEObject(eObject);
		
		//Need to do this delayed in case the resource tree is locked for modification.
		WorkspaceJob job = new WorkspaceJob("Add error marker to " + file.getName()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
				doCreateErrorMarker(eObject, file, message, includePositionInformation);
				return Status.OK_STATUS;
			}
		};
		
		job.setRule(file);
		job.schedule();
	}

	private static void doCreateErrorMarker(EObject eObject, IFile file, String message, boolean includePositionInformation) throws CoreException {
		
		IMarker marker = file.createMarker(PROBLEM_MARKER_ID);
		
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.MESSAGE, message);
		
		if (includePositionInformation) {
			Resource resource = eObject.eResource();
			
			if (resource instanceof IDecoreTextResource) {
				IDecoreTextResource textResource = (IDecoreTextResource) resource;
				IDecoreLocationMap locationMap = textResource.getLocationMap();
				
				int line = locationMap.getLine(eObject);
				int charStart = locationMap.getCharStart(eObject);
				int charEnd = locationMap.getCharEnd(eObject);
				
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.CHAR_START, charStart);
				marker.setAttribute(IMarker.CHAR_END, charEnd);
			}
		}
	}
	
	private static void clearErrorMarkers(EObject eObject) {
		final IFile file = EcoreResolverUtil.resolveRelativeFileFromEObject(eObject);
		
		//Need to do this delayed in case the resource tree is locked for modification.
		WorkspaceJob job = new WorkspaceJob("Add error marker to " + file.getName()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
				IMarker[] markers = file.findMarkers(PROBLEM_MARKER_ID, false, 0);
				
				if (markers != null) {
					for (IMarker marker : markers) {
						marker.delete();
					}
				}
				
				return Status.OK_STATUS;
			}
		};
		
		job.setRule(file);
		job.schedule();
	}
}
