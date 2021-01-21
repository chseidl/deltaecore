package org.deltaecore.feature.analysis;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.analysis.util.FeatureModelConverter;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;

import solver.Solver;

public class FeatureModelSatisfiabilityChecker {
	public boolean checkSatisfiability(DEFeatureModel featureModel) {
		DEConstraintModel constraintModel = DEConstraintIOUtil.loadAccompanyingConstraintModel(featureModel);
		return checkSatisfiability(featureModel, constraintModel);
	}
	
	public boolean checkSatisfiability(DEFeatureModel featureModel, DEConstraintModel constraintModel) {
		FeatureModelConverter converter = new FeatureModelConverter();
		Solver solver = converter.convertFeatureModel(featureModel, constraintModel);
		
		return solver.findSolution();
	}
}
