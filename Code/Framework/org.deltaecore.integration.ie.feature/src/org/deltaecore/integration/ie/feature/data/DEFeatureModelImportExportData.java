package org.deltaecore.integration.ie.feature.data;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.DEConstraintModel;

public class DEFeatureModelImportExportData {
	private DEFeatureModel featureModel;
	private DEConstraintModel constraintModel;
	
	public DEFeatureModelImportExportData(DEFeatureModel featureModel, DEConstraintModel constraintModel) {
		this.featureModel = featureModel;
		this.constraintModel = constraintModel;
	}

	public DEFeatureModel getFeatureModel() {
		return featureModel;
	}

	public void setFeatureModel(DEFeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public DEConstraintModel getConstraintModel() {
		return constraintModel;
	}

	public void setConstraintModel(DEConstraintModel constraintModel) {
		this.constraintModel = constraintModel;
	}
}
