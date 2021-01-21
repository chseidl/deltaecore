package org.deltaecore.integration.ie.feature;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.integration.ie.feature.data.DEFeatureModelImportExportData;
import org.deltaecore.integration.ie.feature.data.DEForeignFeatureModelImportExportData;
import org.eclipse.core.resources.IFile;

public abstract class DEFeatureModelImporter<T extends DEForeignFeatureModelImportExportData> extends DEFeatureModelImporterExporterBase {
	protected void reset() {
	}
	
	public DEFeatureModelImportExportData importFeatureModel(IFile foreignFeatureModelFile) {
		if (foreignFeatureModelFile == null || !foreignFeatureModelFile.exists()) {
			//TODO:
			return null;
		}
		
		T foreignFeatureModelImportData = loadFeatureModel(foreignFeatureModelFile);
		
		if (foreignFeatureModelImportData == null) {
			//TODO:
			return null;
		}
		
		return importFeatureModel(foreignFeatureModelImportData);
	}
	
	protected abstract T loadFeatureModel(IFile file);
	
	public DEFeatureModelImportExportData importFeatureModel(T foreignFeatureModelImportData) {
		reset();
		
		DEFeatureModelImportExportData featureModelImportData = doImportFeatureModel(foreignFeatureModelImportData);
		
		DEFeatureModel featureModel = featureModelImportData.getFeatureModel();
		DEConstraintModel constraintModel = featureModelImportData.getConstraintModel();
		
		checkFeatureModel(featureModel);
		
		if (constraintModel != null) {
			checkConstraintModel(constraintModel);
		}
		
		return featureModelImportData;
	}
	
	protected abstract DEFeatureModelImportExportData doImportFeatureModel(T foreignFeatureModelImportData);
}
