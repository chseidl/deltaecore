package org.deltaecore.integration.ie.feature.eclipse;

import org.deltaecore.feature.util.DEFeatureIOUtil;
import org.deltaecore.integration.ie.feature.splot.DESPLOTFeatureModelExporter;
import org.deltaecore.integration.ie.feature.splot.data.DESPLOTFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.splot.util.DESPLOTFeatureModelSerializer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.eclipse.ui.JFaceUtil;
import fm.FeatureModel;

public class ExportSPLOTFeatureModelCommandHandler extends AbstractHandler {
	private static final String splotFeatureModelFileExtension = "xml";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IFile deFeatureModelFile = DEFeatureIOUtil.getFirstActiveFeatureModelFile();
		
		if (deFeatureModelFile == null || !deFeatureModelFile.exists()) {
			JFaceUtil.alertError("The feature model does not exist.");
			return null;
		}
		
		//Export the DeltaEcore feature model
		DESPLOTFeatureModelExporter exporter = new DESPLOTFeatureModelExporter();
		
		DESPLOTFeatureModelImportExportData exportData = exporter.exportFeatureModel(deFeatureModelFile);
		FeatureModel featureModel = exportData.getFeatureModel();
		
		//Save the SPLOT feature model
		String serializedFeatureModel = DESPLOTFeatureModelSerializer.serializeFeatureModel(featureModel);
		
		IFile featureModelFile = ResourceUtil.deriveFile(deFeatureModelFile, splotFeatureModelFileExtension);
		ResourceUtil.writeToFile(serializedFeatureModel, featureModelFile);
		
		JFaceUtil.alertInformation("Feature model saved to " + featureModelFile.getFullPath());
		
		return null;
	}
}
