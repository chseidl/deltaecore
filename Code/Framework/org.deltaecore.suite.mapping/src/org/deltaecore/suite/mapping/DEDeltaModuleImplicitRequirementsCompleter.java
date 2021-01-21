package org.deltaecore.suite.mapping;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaBlock;
import org.deltaecore.core.decore.DEDeltaInvokation;
import org.deltaecore.core.decore.resource.decore.postprocessor.IDEDecorePostProcessor;
import org.deltaecore.core.decorebase.DERelativeFilePath;
import org.deltaecore.core.decorebase.DEcoreBaseFactory;
import org.deltaecore.debug.DEDebug;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.expression.DEAbstractFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEConditionalFeatureReferenceExpression;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.DERelativeVersionRestriction;
import org.deltaecore.feature.expression.DERelativeVersionRestrictionOperator;
import org.deltaecore.feature.expression.DEVersionRestriction;
import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.deltaecore.suite.mapping.DEMapping;
import org.deltaecore.suite.mapping.DEMappingModel;
import org.deltaecore.suite.mapping.util.DEMappingEvaluator;
import org.deltaecore.suite.mapping.util.DEMappingIOUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEDeltaModuleImplicitRequirementsCompleter implements IDEDecorePostProcessor {
	
	@Override
	public void process(DEDelta delta) {
//		System.out.println("Completing implicit requirements");
		
//		determineAndAddImplicitlyRequiredDeltaModules(delta);
	}

	//TODO: This is not finished, implement some time (2016_04_06)
	protected void determineAndAddImplicitlyRequiredDeltaModules(DEDelta delta) {
		//Find feature model
		DEFeatureModel featureModel = DEFeatureIOUtil.loadFeatureModelFromProjectRoot(delta);
		
		if (featureModel == null) {
			DEDebug.println("Couldn't find feature model in the root of the project of delta module \"" + delta.getName() + "\".");
			return;
		}
		
		DEDebug.println("Using feature model \"" + getResourceName(featureModel) + "\" to complete requirements of delta module \"" + delta.getName() + "\".");

		
		//Find mapping model
		DEMappingModel mappingModel = DEMappingIOUtil.loadAccompanyingMappingModel(featureModel);
		
		if (mappingModel == null) {
			DEDebug.println("Couldn't find mapping model \"" + getResourceName(mappingModel) + "\" for the feature model \"" + getResourceName(featureModel) + "\".");
			return;
		}
		
		DEDebug.println("Using mapping model \"" + getResourceName(mappingModel) + "\" to complete requirements of delta module \"" + delta.getName() + "\".");
		
		
		final List<Object> relevantElements = new ArrayList<Object>();
		List<DEMapping> mappings = mappingModel.getMappings();
		
		for (DEMapping mapping : mappings) {
			List<DEDeltaInvokation> deltaInvokations = mapping.getDeltaInvokations();
			
			for (DEDeltaInvokation deltaInvokation : deltaInvokations) {
				DEDelta invokedDelta = deltaInvokation.getDelta();
				
				if (invokedDelta == delta) {
					//TODO: Find the feature/version this delta module is associated with (exclusively?)
					DEExpression expression = mapping.getExpression();
					
					//TODO: How exactly to determine which feature/version the delta module is associated with?
					
					//Evaluate only single references to features.
					if (expression instanceof DEAbstractFeatureReferenceExpression) {
						//If it is a feature, there is not much that we can derive.
//						if (expression instanceof DEFeatureReferenceExpression) {
//							DEFeatureReferenceExpression featureReferenceExpression = (DEFeatureReferenceExpression) expression;
//							DEFeature feature = featureReferenceExpression.getFeature();
//						}
						
						
						if (expression instanceof DEConditionalFeatureReferenceExpression) {
							DEConditionalFeatureReferenceExpression conditionalFeatureReferenceExpression = (DEConditionalFeatureReferenceExpression) expression;
							
							DEVersionRestriction versionRestriction = conditionalFeatureReferenceExpression.getVersionRestriction();
							
							//If it is a version, ...
							if (versionRestriction instanceof DERelativeVersionRestriction) {
								DERelativeVersionRestriction relativeVersionRestriction = (DERelativeVersionRestriction) versionRestriction;

								DERelativeVersionRestrictionOperator operator = relativeVersionRestriction.getOperator();
								
								if (operator == DERelativeVersionRestrictionOperator.EQUAL) {
									DEVersion version = relativeVersionRestriction.getVersion();
									
									//... then get its predecessor ...
									DEVersion predecessorVersion = version.getSupersededVersion();
									
									if (predecessorVersion != null) {
										//... find all delta modules associated with the predecessor version and add requirements
										
										//TODO: What to add here?!
										relevantElements.add(predecessorVersion);
									} else {
										//... if it has no predecessor, then analyze the feature
										DEFeature feature = version.getFeature();
										relevantElements.add(feature);
									}
								}
							}
						}
					}
				}
			}
			
			
			
			DEMappingEvaluator mappingEvaluator = new DEMappingEvaluator() {
				@Override
				protected boolean isFeaturePresent(DEFeature feature) {
					return relevantElements.contains(feature);
				}
				
				@Override
				protected boolean isVersionPresent(DEVersion version) {
					return relevantElements.contains(version);
				}
			};
			
			
			
			
			List<DEDelta> implicitlyRequiredDeltas = mappingEvaluator.evaluateMapping(mapping);
			
			//In case the delta itself was added, remove it now.
			implicitlyRequiredDeltas.remove(delta);
			
			addImplicitlyRequiredDeltas(delta, implicitlyRequiredDeltas);
		}
	}
	
	private void addImplicitlyRequiredDeltas(DEDelta delta, List<DEDelta> implicitlyRequiredDeltas) {
		List<DEDeltaBlock> blocks = delta.getBlocks();
		
		//Make relative file paths!
		List<DERelativeFilePath> relativeFilePathsOfImplicityRequiredDeltas = createRelativeFilePaths(delta, implicitlyRequiredDeltas);
		
		for (DEDeltaBlock block : blocks) {
			for (DERelativeFilePath relativeFilePathOfImplicityRequiredDelta : relativeFilePathsOfImplicityRequiredDeltas) {
				List<DERelativeFilePath> relativeFilePaths = block.getRequiredElementRelativeFilePaths();
				addRequiredElementRelativeFilePathIfNotAlreadyPresent(relativeFilePathOfImplicityRequiredDelta, relativeFilePaths);
			}
		}
	}
	
	private List<DERelativeFilePath> createRelativeFilePaths(DEDelta delta, List<DEDelta> implicitlyRequiredDeltas) {
		List<DERelativeFilePath> relativeFilePaths = new ArrayList<DERelativeFilePath>();
		
		IFile deltaFile = EcoreIOUtil.getFile(delta);
		System.out.println("Delta file: " + deltaFile);
		
		IContainer deltaFileContainer = deltaFile.getParent();
		IPath deltaFileContainerPath = deltaFileContainer.getLocation();
		
		for (DEDelta implicitlyRequiredDelta : implicitlyRequiredDeltas) {
			IFile implicitlyRequiredDeltaFile = EcoreIOUtil.getFile(implicitlyRequiredDelta);
			
			//TODO: Not sure what to do here!
			if (implicitlyRequiredDeltaFile == null) {
				continue;
			}
			
			IPath implicitlyRequiredDeltaFilePath = implicitlyRequiredDeltaFile.getLocation();
			
			IPath implicitlyRequiredDeltaFileRelativePath = implicitlyRequiredDeltaFilePath.makeRelativeTo(deltaFileContainerPath);
			String rawImplicitlyRequiredDeltaFileRelativeFilePath = implicitlyRequiredDeltaFileRelativePath.toString();
			
			DERelativeFilePath implicitlyRequiredDeltaFileRelativeFilePath = DEcoreBaseFactory.eINSTANCE.createDERelativeFilePath();
			implicitlyRequiredDeltaFileRelativeFilePath.setRawRelativeFilePath(rawImplicitlyRequiredDeltaFileRelativeFilePath);
			
			System.out.println("Raw relative delta path: " + rawImplicitlyRequiredDeltaFileRelativeFilePath);
			
			relativeFilePaths.add(implicitlyRequiredDeltaFileRelativeFilePath);
		}
		
		return relativeFilePaths;
	}
	
	private void addRequiredElementRelativeFilePathIfNotAlreadyPresent(DERelativeFilePath newRelativeFilePath, List<DERelativeFilePath> relativeFilePaths) {
		String newRawRelativeFilePath = newRelativeFilePath.getRawRelativeFilePath();
		
		for (DERelativeFilePath relativeFilePath : relativeFilePaths) {
			String rawRelativeFilePath = relativeFilePath.getRawRelativeFilePath();
			
			//This is probably not an ideal comparison!
			if (rawRelativeFilePath.equals(newRawRelativeFilePath)) {
				return;
			}
		}
		
		relativeFilePaths.add(newRelativeFilePath);
	}
	
	
	private static String getResourceName(EObject element) {
		return element.eResource().getURI().lastSegment();
	}
}
