package org.deltaecore.variant.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.variant.DEVariantCreator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public abstract class AbstractVariantGenerationTest {
	private IProject inputProject;
	private IProject expectedOutputProject;
	private IProject actualOutputProject;
	
	public AbstractVariantGenerationTest() {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		inputProject = createInputProject(workspaceRoot);
		expectedOutputProject = createExpectedOutputProject(workspaceRoot);
		actualOutputProject = createActualOutputProject(workspaceRoot);
	}
	
	
	protected IProject getInputProject() {
		return inputProject;
	}

	protected IProject getExpectedOutputProject() {
		return expectedOutputProject;
	}

	protected IProject getActualOutputProject() {
		return actualOutputProject;
	}
	
	
	protected IProject createInputProject(IWorkspaceRoot workspaceRoot) {
		return workspaceRoot.getProject("Input");
	}
	
	protected IProject createExpectedOutputProject(IWorkspaceRoot workspaceRoot) {
		return workspaceRoot.getProject("ExpectedOutput");
	}
	
	protected IProject createActualOutputProject(IWorkspaceRoot workspaceRoot) {
		return workspaceRoot.getProject("ActualOutput");
	}
	
	
	protected IFolder createDeltasFolder(String testCaseName, String testSetName) {
		return inputProject.getFolder(testSetName + "/" + "deltas");
	}
	
	protected IFolder createExpectedVariantFolder(String testCaseName, String testSetName) {
		return expectedOutputProject.getFolder(testCaseName);
	}
	
	protected IFolder createActualVariantFolder(String testCaseName, String testSetName) {
		//Make this independent from concrete test case name as files are deleted anyways
		return actualOutputProject.getFolder("resources");
	}
	
	
	protected void testSuccessfulVariantGeneration(String testCaseName, String testSetName, String[] deltaNames) {
		try {
			testVariantGeneration(testCaseName, testSetName, deltaNames);
		} catch(Exception e) {
			assertEquals("Variant creation threw exception: \"" + e.getMessage() + "\".", false, true);
		}
	}
	
	protected void testVariantGeneration(String testCaseName, String testSetName, String[] deltaNames) throws DEDeltaRequirementsCycleException, CoreException {
		System.out.println("===== Started Test Case \"" + testCaseName + "\" =====");
		//Setup folder structure
		IFolder deltasFolder = createDeltasFolder(testCaseName, testSetName);
		IFolder expectedVariantFolder = createExpectedVariantFolder(testCaseName, testSetName);
		IFolder actualVariantFolder = createActualVariantFolder(testCaseName, testSetName);
		
		//Create folders for actual variant if needed
		ResourceUtil.ensureFolderStructure(actualVariantFolder);
		
		//Delete resources in the folder where the variant should be created
		ResourceUtil.deleteResourcesInFolder(actualVariantFolder);
		
		
		//Create delta files
		List<IFile> deltaFiles = new LinkedList<IFile>();
		
		for (String deltaFileName : deltaNames) {
			IFile deltaFile = deltasFolder.getFile(deltaFileName + ".decore");
			deltaFiles.add(deltaFile);
		}
		
		//Perform variant derivation
		DEVariantCreator variantCreator = new DEVariantCreator();
		variantCreator.createAndSaveVariantFromDeltaFiles(deltaFiles, actualVariantFolder);
		
		//Compare resources of created variant
		compareVariantFolders(expectedVariantFolder, actualVariantFolder);
		
		System.out.println("===== Finished Test Case \"" + testCaseName + "\" =====");
	}

	
	private void compareVariantFolders(IFolder expectedVariantFolder, IFolder actualVariantFolder) throws CoreException {
		compareVariantFolders(actualVariantFolder, expectedVariantFolder, actualVariantFolder);
	}
	
	private void compareVariantFolders(IFolder currentActualFolder, IFolder expectedVariantFolder, IFolder actualVariantFolder) throws CoreException {
		IPath relativeCurrentFolderPath = ResourceUtil.makeResourcePathRelativeToContainer(currentActualFolder, actualVariantFolder);
		IFolder currentExpectedFolder = expectedVariantFolder.getFolder(relativeCurrentFolderPath);
		
		IResource[] actualMembers = currentActualFolder.members();
		IResource[] expectedMembers = currentExpectedFolder.members();
		
		//Ensures that there are no files missing in the actual variant
		assertEquals("Number of resources in folder \"" + currentActualFolder.getFullPath() + "\" does not match, ", expectedMembers.length, actualMembers.length);
		
		for (IResource actualMember : actualMembers) {
			if (actualMember instanceof IFile) {
				IFile actualFile = (IFile) actualMember;
				
				IPath relativeCurrentFilePath = ResourceUtil.makeResourcePathRelativeToContainer(actualFile, actualVariantFolder);
				IFile expectedFile = expectedVariantFolder.getFile(relativeCurrentFilePath);
				
				EObject expectedModel = EcoreIOUtil.loadModel(expectedFile);
				EObject actualModel = EcoreIOUtil.loadModel(actualFile);
				
				boolean modelsEqual = EcoreUtil.equals(expectedModel, actualModel);
				assertEquals("Expected and actual model do not match for \"" + relativeCurrentFilePath + "\", ", true, modelsEqual);
			} else if (actualMember instanceof IFolder) {
				IFolder actualSubFolder = (IFolder) actualMember;
				
				compareVariantFolders(actualSubFolder, expectedVariantFolder, actualVariantFolder);
			} else {
				throw new RuntimeException("Unexpected resource encountered.");
			}
			
		}
	}
}
