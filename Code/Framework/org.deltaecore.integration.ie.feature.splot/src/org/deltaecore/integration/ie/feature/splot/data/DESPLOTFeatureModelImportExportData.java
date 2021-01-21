package org.deltaecore.integration.ie.feature.splot.data;

import org.deltaecore.integration.ie.feature.data.DEForeignFeatureModelImportExportData;

import fm.FeatureModel;

public class DESPLOTFeatureModelImportExportData extends DEForeignFeatureModelImportExportData {
	private FeatureModel featureModel;

	public DESPLOTFeatureModelImportExportData() {
		this(null);
	}
	
	public DESPLOTFeatureModelImportExportData(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setFeatureModel(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}
}
