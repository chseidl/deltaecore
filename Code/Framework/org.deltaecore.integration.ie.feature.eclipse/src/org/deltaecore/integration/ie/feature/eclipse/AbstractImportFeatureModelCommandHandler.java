package org.deltaecore.integration.ie.feature.eclipse;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;
import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.deltaecore.integration.ie.feature.DEFeatureModelImporter;
import org.deltaecore.integration.ie.feature.data.DEFeatureModelImportExportData;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.eclipse.ui.JFaceUtil;
import de.christophseidl.util.eclipse.ui.SelectionUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public abstract class AbstractImportFeatureModelCommandHandler extends AbstractHandler {
	private String featureModelFileExtension;
	
	public AbstractImportFeatureModelCommandHandler(String featureModelFileExtension) {
		this.featureModelFileExtension = featureModelFileExtension;
	}
	
	protected abstract DEFeatureModelImporter<?> createFeatureModelImporter();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile selectedFile = SelectionUtil.getFirstActiveIFileWithExtension(featureModelFileExtension);
		
		if (selectedFile == null || !selectedFile.exists()) {
			JFaceUtil.alertError("No feature model with extension ." + featureModelFileExtension + " specified for import.");
			//TODO: Error
			return null;
		}
		
		//Import the foreign feature model
		DEFeatureModelImporter<?> importer = createFeatureModelImporter();
		DEFeatureModelImportExportData importData = importer.importFeatureModel(selectedFile);
		
		
		//Save the DeltaEcore feature model
		DEFeatureModel featureModel = importData.getFeatureModel();
		
		String featureModelFileExtension = DEFeatureIOUtil.getDefaultFileExtension();
		IFile featureModelFile = ResourceUtil.deriveFile(selectedFile, featureModelFileExtension);
		EcoreIOUtil.saveModelAs(featureModel, featureModelFile);
		
		String message = "Feature model saved to " + featureModelFile.getFullPath();
		
		
		//Save the DeltaEcore constraint model (if any)
		DEConstraintModel constraintModel = importData.getConstraintModel();

		if (constraintModel != null) {
			String constraintModelFileExtension = DEConstraintIOUtil.getDefaultFileExtension();
			IFile constraintModelFile = ResourceUtil.deriveFile(selectedFile, constraintModelFileExtension);
			EcoreIOUtil.saveModelAs(constraintModel, constraintModelFile);
			
			message += " and constraint model saved to " + constraintModelFile.getFullPath();
		}
		
		message += ".";
		
		JFaceUtil.alertInformation(message);
		
		return null;
	}
}
