package org.deltaecore.feature.configuration.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.constraint.DEConstraint;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.resource.deconstraints.mopp.DeconstraintsPrinter;
import org.deltaecore.feature.constraint.util.DEConstraintEvaluator;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;

public class DEConfigurationChecker {
	private String errorMessage;
	
	public boolean isConfigurationValid(DEConfiguration configuration) {
		resetErrorMessage();
		List<DEFeature> selectedFeatures = DEConfigurationUtil.getSelectedFeatures(configuration);
		
		if (selectedFeatures.isEmpty()) {
			setErrorMessage("Configuration must at least contain the root feature.");
			return false;
		}
		
		for (DEFeature feature : selectedFeatures) {
			//This ensures that the root feature is selected and that all rules of the feature model are satisfied.
			
			//For each selected feature...
				
			//... the parent has to be in the configuration (except for the root feature)
			DEGroup parentGroup = feature.getParentOfFeature();
			
			if (parentGroup != null) {
				DEFeature parentFeature = parentGroup.getParentOfGroup();
				
				if (parentFeature != null && !DEConfigurationUtil.configurationContains(configuration, parentFeature)) {
					setErrorMessage("Configuration must contain feature \"" + parentFeature.getName() + "\" as parent of \"" + feature.getName() + "\".");
					return false;
				}
			}

			
			//... all group constraints have to be satisfied
			List<DEGroup> childGroups = feature.getGroups();
			
			for (DEGroup childGroup : childGroups) {
				//... all constraints of child features have to be met (mandatory)
				List<DEFeature> childFeatures = childGroup.getFeatures();
				int numberOfSelectedFeaturesInGroup = 0;
				
				for (DEFeature childFeature : childFeatures) {
					boolean childFeatureIsSelected = DEConfigurationUtil.configurationContains(configuration, childFeature);
					
					if (childFeature.isMandatory() && !childFeatureIsSelected) {
						setErrorMessage("Configuration must contain the mandatory feature \"" + childFeature.getName() + "\" as child of \"" + feature.getName() + "\".");
						return false;
					}
					
					if (childFeatureIsSelected) {
						numberOfSelectedFeaturesInGroup++;
					}
				}
				
				//... all group cardinalities have to be satisfied
				int minCardinality = childGroup.getMinCardinality();
				int maxCardinality = childGroup.getMaxCardinality();
				
				if (numberOfSelectedFeaturesInGroup < minCardinality || numberOfSelectedFeaturesInGroup > maxCardinality) {
					
					setErrorMessage("Configuration must contain (" + minCardinality + ".." + maxCardinality + ") child features of " + feature.getName() + " (currently contains " + numberOfSelectedFeaturesInGroup + ").");
					return false;
				}
			}
				
			
			//If a feature specifies versions, exactly one of them has to be in the configuration.
			List<DEVersion> versions = feature.getVersions();
			
			if (!versions.isEmpty()) {
				List<DEVersion> selectedVersions = DEConfigurationUtil.getSelectedVersionsForFeature(configuration, feature);
				int numberOfSelectedVersions = selectedVersions.size();

				if (numberOfSelectedVersions != 1) {
					setErrorMessage("Configuration must contain exactly one version for feature \"" + feature.getName() + "\" (currently has " + numberOfSelectedVersions + ").");
					return false;
				}
			}
				
		}
		
		//The constraints have to be satisfied.
		DEFeatureModel featureModel = configuration.getFeatureModel();
		DEConstraintModel constraintModel = DEConstraintIOUtil.loadAccompanyingConstraintModel(featureModel);
		List<DEVersion> selectedVersions = DEConfigurationUtil.getSelectedVersions(configuration);
		
		DEConstraintEvaluator constraintEvaluator = new DEConstraintEvaluator();
		boolean allConstraintsSatisfied = constraintEvaluator.evaluate(constraintModel, selectedFeatures, selectedVersions);
		
		if (!allConstraintsSatisfied) {

			try {
				DEConstraint violatedConstraint = constraintEvaluator.getViolatedConstraint();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				DeconstraintsPrinter printer = new DeconstraintsPrinter(outputStream, null);
				printer.print(violatedConstraint);
				String formattedConstraint = outputStream.toString();
				setErrorMessage("Constraint \"" + formattedConstraint + "\" is violated.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		return true;
	}

	private void resetErrorMessage() {
		errorMessage = "";
	}
	
	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
