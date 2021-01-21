package org.deltaecore.feature.constraint.util;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.util.DEExpressionEvaluator;

public class DEConstraintEvaluator extends DEExpressionEvaluator {
	private List<DEFeature> selectedFeatures;
	private List<DEVersion> selectedVersions;
	
	private DEConstraint violatedConstraint;
	
	public boolean evaluate(DEConstraintModel constraintModel, List<DEFeature> selectedFeatures) {
		return evaluate(constraintModel, selectedFeatures, null);
	}
	
	public boolean evaluate(DEConstraintModel constraintModel, List<DEFeature> selectedFeatures, List<DEVersion> selectedVersions) {
		violatedConstraint = null;
		
		if (constraintModel == null) {
			//No constraints specified so they are satisfied.
			return true;
		}
		
		this.selectedFeatures = selectedFeatures;
		this.selectedVersions = selectedVersions;
		
		List<DEConstraint> constraints = constraintModel.getConstraints();
		
		for (DEConstraint constraint : constraints) {
			DEExpression rootExpression = constraint.getRootExpression();
			boolean isSatisfied = evaluateExpression(rootExpression);
			
			if (!isSatisfied) {
				violatedConstraint = constraint;
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected boolean isFeaturePresent(DEFeature feature) {
		if (selectedFeatures == null) {
			return false;
		}
		
		return selectedFeatures.contains(feature);
	}

	@Override
	protected boolean isVersionPresent(DEVersion version) {
		if (selectedVersions == null) {
			return false;
		}
		
		return selectedVersions.contains(version);
	}

	public DEConstraint getViolatedConstraint() {
		return violatedConstraint;
	}

}
