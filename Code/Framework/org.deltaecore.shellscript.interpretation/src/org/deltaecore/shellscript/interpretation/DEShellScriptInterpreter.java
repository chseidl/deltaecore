package org.deltaecore.shellscript.interpretation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.variant.DEVariantCreator;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.deltaecore.shellscript.DECommand;
import org.deltaecore.shellscript.DEShellScript;
import org.deltaecore.shellscript.resource.deshellscript.util.DeshellscriptResourceUtil;
import org.deltaecore.shellscript.util.DEShellScriptUtil;
import org.deltaecore.suite.variant.util.DEConfigurationEvaluator;
import org.deltaecore.util.DEIOUtil;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.christophseidl.util.ecore.EcoreIOUtilJavaFile;
import de.christophseidl.util.java.StringUtil;

//TODO: Check configuration validity

public abstract class DEShellScriptInterpreter implements DEShellScriptConstants {
	private boolean quiet;
	private boolean verbose;
	
	private ResourceSet resourceSet;
	
	private DEFeatureModel featureModel;
//	private DEConstraintModel constraintModel;
//	private DEMappingModel mappingModel;
//	private DEApplicationOrderConstraintModel applicationOrderConstraintModel;
	//TODO: configuration
	
	private DEVariantCreator variantCreator;
	
	public DEShellScriptInterpreter() {
		variantCreator = new DEVariantCreator();
	}
	
	public boolean interpretShellScript(DEShellScript shellScript) {
		List<DECommand> commands = shellScript.getCommands();
		int numberOfInterpretedCommands = 0;
		
		for (DECommand command : commands) {
			String name = DEShellScriptUtil.getCanonicalName(command.getName());
			
			if (name.equals("exit")) {
				return false;
			} else {
				interpretCommand(command);
				numberOfInterpretedCommands++;
			}
		}
		
		int numberOfUninterpretedCommands = commands.size() - numberOfInterpretedCommands;
		
		if (numberOfUninterpretedCommands > 0) {
			printError("Exiting shell with " + numberOfInterpretedCommands + " uninterpreted command(s).");
		}
		
		return true;
	}
	
	//Return value signals whether to keep receiving commands in shell mode.
	protected boolean interpretRawShellScript(String rawShellScript) {
		DEShellScript shellScript = parseShellScript(rawShellScript);

		if (shellScript == null) {
			printError("Unable to parse input. " + COMMAND_FORMAT + ".");
			return true;
		}

		return interpretShellScript(shellScript);
	}

	protected DEShellScript parseShellScript(String rawShellScript) {
		Resource resource = DeshellscriptResourceUtil.getResource(rawShellScript.getBytes());

		if (resource == null) {
			return null;
		}

		List<EObject> contents = resource.getContents();

		if (contents != null && !contents.isEmpty()) {
			EObject content = contents.get(0);

			if (content instanceof DEShellScript) {
				return (DEShellScript) content;
			}
		}

		return null;
	}
	
	private void reset() {
		quiet = false;
		verbose = false;
		
		resourceSet = null;
		
		featureModel = null;
//		constraintModel = null;
//		mappingModel = null;
//		applicationOrderConstraintModel = null;
	}
	
	private void interpretCommand(DECommand command) {
		reset();
		
		if (DEShellScriptUtil.isCommand(command, COMMAND_HELP)) {
			interpretHelp(command);
			return;
		}
		
		if (DEShellScriptUtil.isCommand(command, COMMAND_DERIVE_VARIANT)) {
			interpretDeriveVariant(command);
			return;
		}
	}
	
	private void interpretHelp(DECommand command) {
		String value = DEShellScriptUtil.getArgumentValue(command, PARAMETER_COMMAND);
		
		if (value == null) {
			//General help - list of commands
			print("DeltaEcore shell.");
			print(COMMAND_FORMAT);
			print();
			print("Commands:");
			print(COMMAND_DERIVE_VARIANT + "\t[" + COMMAND_DERIVE_VARIANT_LONG + "]\t" + COMMAND_DERIVE_VARIANT_DESCRIPTION);
			print(COMMAND_HELP + "\t[" + COMMAND_HELP_LONG + "]\t\t" + COMMAND_HELP_DESCRIPTION);
			return;
		}
		
		if (value.equals(COMMAND_HELP)) {
			print(COMMAND_HELP_DESCRIPTION);
			print();
			print("Options:");
			print("-" + PARAMETER_COMMAND + " \"<Command>\"" + "\t" + "Specifies file paths for delta modules as input.");
			return;
		}
		
		if (value.equals(COMMAND_DERIVE_VARIANT)) {
			print(COMMAND_DERIVE_VARIANT_DESCRIPTION);
			print();
			print("Options:");
//			print("-" + PARAMETER_APPLICATION_ORDER_CONSTRAINT_MODEL + " \"<File>\"" + "\t" + "Path to the application-order constraint model. Can be omitted when using -" + PARAMETER_FEATURE_MODEL  + " where the application-order constraint model is assumed to have the same name as the feature model with extension '.aoc'.");
			print("-" + PARAMETER_DELTA_MODULES + " \"<File1>\" [, \"<FileN>\"]" + "\t\t\t\t" + "Specifies file paths for delta modules as input.");
//			print("-" + PARAMETER_CONSTRAINT_MODEL + " \"<File>\"" + "\t" + "Path to the constraint model. Can be omitted when using -" + PARAMETER_FEATURE_MODEL + " where the constraint model is assumed to have the same name as the feature model with extension '.constraints'.");
			print("-" + PARAMETER_FEATURES + " \"<Feature1>\" [, \"<FeatureN>\"]"  + "\t\t\t" + "Specifies names of features as input (requires -" + PARAMETER_FEATURE_MODEL + ").");
			print("-" + PARAMETER_FEATURE_MODEL + " \"<File>\"" + "\t\t\t\t\t\t" + "Path to the feature model (used with -" + PARAMETER_FEATURES + " or -" + PARAMETER_FEATURES_WITH_VERSIONS + ").");
			print("-" + PARAMETER_FEATURES_WITH_VERSIONS + " \"<Feature1@Version>\" [, \"<FeatureN@Version>\"]" + "\t" + "Specifies names of features with versions connected by '@' as input, e.g., \"FeatureA@1.0\" (requires -" + PARAMETER_FEATURE_MODEL + ").");
//			print("-" + PARAMETER_MAPPING_MODEL + " \"<File>\"" + "\t" + "Path to the mapping model. Can be omitted when using -" + PARAMETER_FEATURE_MODEL + " where the mapping model is assumed to have the same name as the feature model with extension '.mapping'.");
			print("-" + SWITCH_QUIET + "\t\t\t\t\t\t\t" + "Quiet mode to suppress all information output.");
			print("-" + PARAMETER_VARIANT_FOLDER + " \"<Folder>\"" + "\t\t\t\t\t\t" + "Path for the variant folder used for output.");
			print("-" + SWITCH_VERBOSE + "\t\t\t\t\t\t\t" + "Verbose mode to display internal details.");
			return;
		}
	}
	
	private void interpretDeriveVariant(DECommand command) {
		//Sanity check for input
		if (!ensureNoMoreThanOneArgument(command, new String[] {SWITCH_QUIET, SWITCH_VERBOSE})) {
			return;
		}
		
		if (!ensureExactlyOneArgument(command, new String[] {PARAMETER_DELTA_MODULES, PARAMETER_FEATURES, PARAMETER_FEATURES_WITH_VERSIONS})) {
			return;
		}
		
		if ((DEShellScriptUtil.hasArgument(command, PARAMETER_FEATURES) || DEShellScriptUtil.hasArgument(command, PARAMETER_FEATURES_WITH_VERSIONS)) && !DEShellScriptUtil.hasArgument(command, PARAMETER_FEATURE_MODEL)) {
			System.err.println("When using features (-" + PARAMETER_FEATURES + ") or features with versions (-" + PARAMETER_FEATURES_WITH_VERSIONS + ") as input, a feature model has to be specified (-" + PARAMETER_FEATURE_MODEL + ").");
			return;
		}

		
		quiet = DEShellScriptUtil.hasArgument(command, SWITCH_QUIET);
		verbose = DEShellScriptUtil.hasArgument(command, SWITCH_VERBOSE);
		
		featureModel = tryToLoadModel(command, PARAMETER_FEATURE_MODEL, "feature", DEFeatureIOUtil.FILE_EXTENSIONS, false);
//		constraintModel = tryToLoadModel(command, PARAMETER_CONSTRAINT_MODEL, "constraint", DEConstraintIOUtil.FILE_EXTENSIONS, true);
//		mappingModel = tryToLoadModel(command, PARAMETER_MAPPING_MODEL, "mapping", DEMappingIOUtil.FILE_EXTENSIONS, true);
//		applicationOrderConstraintModel = tryToLoadModel(command, PARAMETER_APPLICATION_ORDER_CONSTRAINT_MODEL, "application-order constraint", DEApplicationOrderConstraintIOUtil.FILE_EXTENSIONS, true);
		
		//Input
		List<DEDelta> deltaModules = getDeltaModulesFromInput(command);

		if (deltaModules == null || deltaModules.isEmpty()) {
			printError("No delta modules could be loaded.");
			return;
		}
		
		//Output
		File variantFolder = getVariantFolder(command);		
		
		
		//Perform variant derivation
		print("Creating variant in \"" + variantFolder + "\".", quiet);
		
		if (!variantFolder.exists()) {
			variantFolder.mkdirs();
			print("Creating variant folder \"" + variantFolder + "\".", quiet);
		}

		try {
			List<Resource> affectedResources = variantCreator.createVariantFromDeltas(deltaModules);
			
			for (Resource affectedResource : affectedResources) {
				File relocatedModelFile = EcoreIOUtilJavaFile.getResourceFileForDifferentFolder(affectedResource, variantFolder);
				print("Saving variant artifact to \"" + relocatedModelFile + "\".", quiet);
				EcoreIOUtilJavaFile.saveResourceAs(affectedResource, relocatedModelFile);
			}
			
			print("Variant derivation completed successfully!", quiet);
		} catch(DEDeltaRequirementsCycleException e) {
			printError(e.getMessage());
			print("Variant derivation failed!", quiet);
			return;
		}
	}


	private List<DEDelta> getDeltaModulesFromInput(DECommand command) {
		if (DEShellScriptUtil.hasArgument(command, PARAMETER_DELTA_MODULES)) {
			return getDeltaModulesFromFiles(command, PARAMETER_DELTA_MODULES);
		}
		
		if (DEShellScriptUtil.hasArgument(command, PARAMETER_FEATURES)) {
			return getDeltaModulesFromFeaturesAndFeatureVersions(command, PARAMETER_FEATURES, false);
		}
		
		if (DEShellScriptUtil.hasArgument(command, PARAMETER_FEATURES_WITH_VERSIONS)) {
			return getDeltaModulesFromFeaturesAndFeatureVersions(command, PARAMETER_FEATURES_WITH_VERSIONS, true);
		}

		return null;
	}
	
	private List<DEDelta> getDeltaModulesFromFiles(DECommand command, String parameterName) {
		List<File> deltaModuleFiles = new ArrayList<File>();
		List<String> argumentValues = DEShellScriptUtil.getArgumentValues(command, parameterName);
		
		for (String input : argumentValues) {
			File deltaModuleFile = getAbsoluteFile(input);
			
			if (!deltaModuleFile.exists()) {
				printError("Delta module \"" + deltaModuleFile + "\" does not exist.");
				return null;
			}
			
			if (!deltaModuleFile.isFile()) {
				printError("Delta module \"" + deltaModuleFile + "\" is not a file.");
				return null;
			}
			
			printVerbose("Using delta module file \"" + deltaModuleFile + "\" as input.");
			
			deltaModuleFiles.add(deltaModuleFile);
		}
		
		return loadModelsFromFiles(deltaModuleFiles);	
	}
	
	private List<DEDelta> getDeltaModulesFromFeaturesAndFeatureVersions(DECommand command, String parameterName, boolean containsVersions) {
		if (featureModel == null) {
			printError("Cannot resolve feature names because feature model was not loaded.");
			return null;
		}
		
		List<String> inputValues = DEShellScriptUtil.getArgumentValues(command, parameterName);
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();

		for (String inputValue : inputValues) {
			String featureName = inputValue;
			String versionName = null;
			
			if (containsVersions) {
				int index = inputValue.lastIndexOf("@");
				
				if (index != -1) {
					featureName = inputValue.substring(0, index);
					versionName = inputValue.substring(index + 1);
				}
			}
			
			DEFeature feature = DEFeatureResolverUtil.resolveFeature(featureName, featureModel);
			
			if (feature == null) {
				printError("Failed to resolve feature \"" + featureName + "\".");
				return null;
			} else {
				printVerbose("Resolved feature \"" + featureName + "\".");
			}
			
			DEConfigurationUtil.addFeatureToConfiguration(feature, configuration, false);
			
			if (versionName != null) {
				DEVersion version = DEFeatureResolverUtil.resolveVersion(versionName, feature);
				
				if (version == null) {
					printError("Failed to resolve feature version \"" + versionName + "\" of feature \"" + featureName + "\".");
					return null;
				} else {
					printVerbose("Resolved feature version \"" + versionName + "\" of feature \"" + featureName + "\".");
				}
				
				DEConfigurationUtil.addVersionToConfiguration(version, configuration, false);
			}
		}
		
		return DEConfigurationEvaluator.getDeltasListForConfiguration(configuration);
	}
	
	private File getVariantFolder(DECommand command) {
		File variantFolder = null;
		
		if (DEShellScriptUtil.hasArgument(command, PARAMETER_VARIANT_FOLDER)) {
			String variantFolderPath = DEShellScriptUtil.getArgumentValue(command, PARAMETER_VARIANT_FOLDER);
			
			if (variantFolderPath == null) {
				variantFolder = getCurrentFolder();
				printVerbose("Using current folder \"" + variantFolder.getAbsolutePath() + "\" as variant folder.");
			} else {
				variantFolder = getAbsoluteFile(variantFolderPath);
				
				if (variantFolder.exists()) {
					if (!variantFolder.isDirectory()) {
						printError("Variant folder \"" + variantFolderPath + "\" is not a folder.");
					}
				} else {
					printVerbose("Variant folder \"" + variantFolderPath + "\" does not exist and will be created.");
				}
			}
		}
		
		return variantFolder;
	}
	
	
	
	
	private File getCurrentFolder() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IPath workspaceLocation = workspaceRoot.getLocation();
		File workspaceDirectory = workspaceLocation.toFile();
				
		return workspaceDirectory;
	}
	
	private File getAbsoluteFile(String path) {
		File file = new File(path);
		
		if (file.isAbsolute()) {
			return file;
		}
		
		return new File(getCurrentFolder(), path);
	}
	
	

	
	private boolean ensureNoMoreThanOneArgument(DECommand command, String[] arguments) {
		if (!DEShellScriptUtil.ensureMinMax(command, arguments, 0, 1)) {
			printError("May only specify one of " + StringUtil.implode(arguments, ", ") + ".");
			return false;
		}
		
		return true;
	}
	
	private boolean ensureExactlyOneArgument(DECommand command, String[] arguments) {
		if (!DEShellScriptUtil.ensureMinMax(command, arguments, 1, 1)) {
			printError("Have to specify one of " + StringUtil.implode(arguments, ", ") + ".");
			return false;
		}
		
		return true;
	}
	
	protected void print() {
		print("");
	}
	
	protected abstract void print(String message);
	
	protected void print(String message, boolean quiet) {
		if (!quiet) {
			print(message);
		}
	}
	
	protected void printVerbose(String message) {
		if (verbose) {
			print(message);
		}
	}
	
	protected void printError(String message) {
		print(message);
	}
	
	
	
	private <T extends EObject> T tryToLoadModel(DECommand command, String parameterName, String modelName, String[] fileExtensions, boolean tryAccompanying) {
		if (DEShellScriptUtil.hasArgument(command, parameterName)) {
			String path = DEShellScriptUtil.getArgumentValue(command, parameterName);
			File file = getAbsoluteFile(path);
			
			printVerbose("Loading " + modelName + " model from \"" + path + "\".");
			
			T model = loadModelFromFile(file);
			
			if (model == null) {
				printError("Failed to load " + modelName + " model from \"" + path + "\".");
			}
			
			return model;
		} else {
			if (tryAccompanying && featureModel != null) {
				T model = DEIOUtil.doLoadAccompanyingModel(featureModel, fileExtensions);
				
				if (model == null) {
					print("Failed to load accompanying " + modelName + " model.", quiet);
				}
				
				return model;
			}
		}
		
		return null;
	}
	
	protected <T extends EObject> List<T> loadModelsFromFiles(List<File> files) {
		List<T> models = new ArrayList<T>();
	
		for (File file : files) {
			T model = loadModelFromFile(file);
			models.add(model);
		}
		
		return models;
	}
	
	protected <T extends EObject> T loadModelFromFile(File file) {
		if (resourceSet != null) {
			return EcoreIOUtilJavaFile.loadModel(file, resourceSet);
		} else {
			T model = EcoreIOUtilJavaFile.loadModel(file);
			Resource resource = model.eResource();
			resourceSet = resource.getResourceSet();
			return model;
		}
	}
}
