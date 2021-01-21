package org.deltaecore.integration.ie.feature.featureide.data;

import org.deltaecore.integration.ie.feature.data.DEForeignFeatureModelImportExportData;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

public class DEFeatureIDEFeatureModelImportExportData extends DEForeignFeatureModelImportExportData {
	private IFeatureModel featureModel;

	public DEFeatureIDEFeatureModelImportExportData() {
		this(null);
	}
	
	public DEFeatureIDEFeatureModelImportExportData(IFeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public IFeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setFeatureModel(IFeatureModel featureModel) {
		this.featureModel = featureModel;
	}
}
