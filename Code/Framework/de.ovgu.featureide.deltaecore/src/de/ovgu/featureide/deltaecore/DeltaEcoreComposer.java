package de.ovgu.featureide.deltaecore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationArtifact;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.deltaecore.feature.variant.DEConfigurationVariantGenerator;
import org.deltaecore.integration.ie.feature.data.DEFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.featureide.DEFeatureIDEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.featureide.data.DEFeatureIDEFeatureModelImportExportData;
import org.deltaecore.suite.variant.DEConfigurationDeltaModuleVariantGenerator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;
import de.christophseidl.util.java.StringUtil;
import de.ovgu.featureide.core.IFeatureProject;
import de.ovgu.featureide.core.builder.ComposerExtensionClass;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

//TODO: Some delta modules appear multiple times in requirements - investigate!

public class DeltaEcoreComposer extends ComposerExtensionClass {
	private static String relativeVariantFolderPath = "variant";
	private static String relativeDeltaModuleFolderPath = "deltas";
	
	private static String generatedFilesBaseName = "DeltaEcore";
	
	private static String featureModelFileExtension = "feature";
	private static String mappingFileExtension = "mapping";
	private static String deltaModuleFileExtension = "decore";
	
	
	private DEFeatureModel deFeatureModel;
	private DEConfigurationVariantGenerator variantGenerator;
	
	public DeltaEcoreComposer() {
		variantGenerator = new DEConfigurationDeltaModuleVariantGenerator();
	}
	
	@Override
	public Mechanism getGenerationMechanism() {
		return Mechanism.DELTA_ORIENTED_PROGRAMMING;
	}
	
	@Override
	public boolean hasSourceFolder() {
		return false;
	}

	
	@Override
	public boolean initialize(IFeatureProject featureProject) {
		boolean wasSuccessful = super.initialize(featureProject);
		
		if (wasSuccessful) {
			IFeatureModel featureModel = featureProject.getFeatureModel();
			
			//Store in memory for use during variant derivation and save to disk for creation of mapping.
			try {
				DEFeatureIDEFeatureModelImporter importer = new DEFeatureIDEFeatureModelImporter();
				DEFeatureIDEFeatureModelImportExportData featureModelImportData = new DEFeatureIDEFeatureModelImportExportData(featureModel);
				DEFeatureModelImportExportData deFeatureModelImportedData = importer.importFeatureModel(featureModelImportData);
				deFeatureModel = deFeatureModelImportedData.getFeatureModel();
			} catch(Exception e) {
				String message = "Problems converting feature model for DeltaEcore: " + e.getMessage();
				generateError(message);
				
				return false;
			}
			
			IProject project = featureProject.getProject();
			
			IFile deFeatureModelFile = project.getFile(new Path(generatedFilesBaseName + "." + featureModelFileExtension));

			boolean modelSavedSuccessfully = EcoreIOUtil.saveModelAs(deFeatureModel, deFeatureModelFile);
			
			if (!modelSavedSuccessfully) {
				return false;
			}
			
			return createInitialFilesIfNecessary(project);
		}
		
		return false;
	}
	
	private static String createPathToDeltaModuleFile(DEFeature feature) {
		String featureName = StringUtil.toCamelCase(feature.getName());
		return relativeDeltaModuleFolderPath + "/" + featureName + "." + deltaModuleFileExtension;
	}
	
	private boolean createInitialFilesIfNecessary(IProject project) {
		//Also create a basic mapping if the file does not exist.
		boolean mappingFileOk = true;
		IFile mappingFile = project.getFile(new Path(generatedFilesBaseName + "." + mappingFileExtension));
		DEFeature feature = findFirstLeafFeature();
		
		if (!mappingFile.exists()) {
			mappingFileOk = writeMappingFile(mappingFile, feature);
			
			IFile deltaModuleFile = project.getFile(new Path(createPathToDeltaModuleFile(feature)));
			boolean deltaModuleFileOk = true;
			
			if (!deltaModuleFile.exists()) {
				deltaModuleFileOk = writeDeltaModuleFile(deltaModuleFile, feature);
			}
			
			return mappingFileOk && deltaModuleFileOk;
		}
		
		return mappingFileOk;
	}
	
	private DEFeature findFirstLeafFeature() {
		Iterator<EObject> iterator = deFeatureModel.eAllContents();
		
		while(iterator.hasNext()) {
			EObject element = iterator.next();
			
			if (element instanceof DEFeature) {
				DEFeature feature = (DEFeature) element;
				
				List<DEGroup> groups = feature.getGroups();
				
				if (groups.isEmpty()) {
					return feature;
				}
			}
		}
		
		return null;
	}
	
	private boolean writeMappingFile(IFile file, DEFeature feature) {
		String content = "";
		
		content += "//TODO: Create mappings from expressions of features to lists of delta modules\n";
		content += "\n";
		content += "//NOTE: It is possible to use complex expressions of features, e.g.,:\n";
		content += "//(FeatureA || FeatureB) && !\"Feature with Space in Name\"\n";
		content += "\n";
		content += DEFeatureResolverUtil.deresolveFeature(feature) + ":" + "\n";
		content += "\t<" + createPathToDeltaModuleFile(feature) + ">\n";
		
		return writeToFile(content, file, "initial mapping file");
	}
	
	private boolean writeDeltaModuleFile(IFile file, DEFeature feature) {
		String featureName = feature.getName();
		String content = "";
		
		content += "delta \""+ featureName + "\"\n";
		content += "\n";
		content += "//TODO: If you want to modify something else than Ecore meta models, use the meta model URI of the the target model here (respective delta language has to exist)\n";
		content += "dialect <http://www.eclipse.org/emf/2002/Ecore>\n";
		content += "//TODO: Add required models to modify or other delta modules here, e.g., \"requires <../model/MainModel.ecore>\"\n";
		content += "{\n";
		content += "\t//TODO: Add calls to delta operations here to perform changes on the required models associated with this delta module.\n";
		content += "}\n";
		
		return writeToFile(content, file, "delta module file");
	}
	
	private boolean writeToFile(String content, IFile file, String fileDescription) {
		try {
			ResourceUtil.writeToFile(content, file);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			
			String message = "Problems writing " + fileDescription + ": " + e.getMessage();
			generateError(message);
			
			return false;
		}
	}
	
	
	
	@Override
	public void performFullBuild(IFile config) {
//		System.out.println("Performing full build on " + config.getLocation());
	
		DEConfiguration configuration = createConfigurationFromConfigFile(config);

		IProject project = config.getProject();
		IFolder variantFolder = project.getFolder(new Path(relativeVariantFolderPath));
		
		try {
			variantGenerator.createAndSaveVariantFromConfiguration(configuration, variantFolder);
		} catch (Exception e) {
			e.printStackTrace();
			generateError(e.getMessage());
		}
	}

	private DEConfiguration createConfigurationFromConfigFile(IFile config) {
		IPath configLocation = config.getLocation();
		File configFile = new File(configLocation.toString());
		
		List<String> featureNames = readTextfile(configFile);
		
		//Create a DeltaEcore configuration
		DEConfiguration deConfiguration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		List<DEConfigurationArtifact> deConfigurationArtifacts = deConfiguration.getConfigurationArtifacts();
		
		for (String featureName : featureNames) {
			DEFeature deFeature = DEFeatureResolverUtil.resolveFeature(featureName, deFeatureModel);
			DEFeatureSelection featureSelection = DEConfigurationFactory.eINSTANCE.createDEFeatureSelection();
			featureSelection.setFeature(deFeature);
			deConfigurationArtifacts.add(featureSelection);
		}
		
		return deConfiguration;
	}
	
	private static List<String> readTextfile(File file) {
		List<String> lines = new ArrayList<String>();
		
		BufferedReader bufferedReader = null;
		 
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
 
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return lines;
	}
	
	protected void generateError(String message) {
		IProject project = featureProject.getProject();
		featureProject.createBuilderMarker(project, message, 0, IMarker.SEVERITY_ERROR);
	}
}
