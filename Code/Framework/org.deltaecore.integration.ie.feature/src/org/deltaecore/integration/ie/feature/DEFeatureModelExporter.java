package org.deltaecore.integration.ie.feature;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;
import org.deltaecore.integration.ie.feature.data.DEForeignFeatureModelImportExportData;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.ecore.EcoreIOUtil;

//TODO: What about versions? Dummy features? if so, make a converter of HFM to FM in DE before

public abstract class DEFeatureModelExporter<T extends DEForeignFeatureModelImportExportData> extends DEFeatureModelImporterExporterBase {
	
//	public void exportFeatureModelToFile(DEFeatureModel featureModelToExport, IFile file) {
//		T exportedFeatureModel = exportFeatureModel(featureModelToExport);
//		writeFeatureModelToFile(exportedFeatureModel, file);
//	}
//	
//	protected abstract void writeFeatureModelToFile(T exportedFeatureModel, IFile file);
	
	public T exportFeatureModel(IFile featureModelToExportFile) {
		DEFeatureModel featureModelToExport = EcoreIOUtil.loadModel(featureModelToExportFile);
		return exportFeatureModel(featureModelToExport);
	}
	
	public T exportFeatureModel(DEFeatureModel featureModelToExport) {
		DEConstraintModel constraintModelToExport = DEConstraintIOUtil.loadAccompanyingConstraintModel(featureModelToExport);
		return exportFeatureModel(featureModelToExport, constraintModelToExport);
	}
	
	public T exportFeatureModel(DEFeatureModel featureModelToExport, DEConstraintModel constraintModelToExport) {
		checkFeatureModel(featureModelToExport);
		checkConstraintModel(constraintModelToExport);
		
		reset();

		return doExportFeatureModel(featureModelToExport, constraintModelToExport);
	}
	
	protected abstract T doExportFeatureModel(DEFeatureModel featureModelToExport, DEConstraintModel constraintModelToExport);
	
	protected void reset() {
	}
}
