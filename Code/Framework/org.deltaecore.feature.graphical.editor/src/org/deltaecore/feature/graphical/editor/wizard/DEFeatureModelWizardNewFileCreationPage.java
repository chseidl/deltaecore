package org.deltaecore.feature.graphical.editor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import de.christophseidl.util.eclipse.ResourceUtil;

public class DEFeatureModelWizardNewFileCreationPage extends WizardNewFileCreationPage {

	public DEFeatureModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection) {
		super(pageId, selection);
		
		setTitle("TODO: Title");
		setDescription("TODO: Description");
		setFileName("FeatureModel.feature");
	}
	
	@Override
	protected boolean validatePage() {
		return true;
	}

	public IFile getModelFile() {
		IPath containerFullPath = getContainerFullPath();
		String filename = getFileName();
		IPath filePath = containerFullPath.append(filename);
		String filePathString = filePath.toString();
		IFile file = ResourceUtil.getLocalFile(filePathString);
		String fileExtension = file.getFileExtension();
		
		//Ensure that the file has the right extension
		if (fileExtension.equalsIgnoreCase("defeaturemodel")) {
			return file;
		}
		
		return ResourceUtil.getLocalFile(filePathString + ".defeaturemodel");
	}
}
