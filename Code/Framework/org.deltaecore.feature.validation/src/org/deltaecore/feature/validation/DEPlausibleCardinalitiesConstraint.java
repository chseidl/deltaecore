package org.deltaecore.feature.validation;

import java.util.List;

import org.deltaecore.feature.DECardinalityBasedElement;
import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.core.runtime.IStatus;

import de.christophseidl.util.ecore.validation.AbstractConstraint;

public class DEPlausibleCardinalitiesConstraint extends AbstractConstraint<DECardinalityBasedElement> {

	@Override
	protected IStatus doValidate(DECardinalityBasedElement cardinalityBasedElement) {
		int minCardinality = cardinalityBasedElement.getMinCardinality();
		int maxCardinality = cardinalityBasedElement.getMaxCardinality();
		
		if (minCardinality > maxCardinality) {
			String message = "The minimum cardinality has to be less than or equal to the maximum cardinality.";
			return createErrorStatus(message, cardinalityBasedElement);
		}
		
		if (cardinalityBasedElement instanceof DEFeature) {
			DEFeature feature = (DEFeature) cardinalityBasedElement;
		
			if (DEFeatureUtil.isRootFeature(feature)) {
				if (!feature.isMandatory()) {
					String message = "The root feature has to be mandatory with cardinality (1..1).";
					return createErrorStatus(message, feature);
				}
			}
		
			if (minCardinality < 0 || minCardinality > 1) {
				String message = "The minimum cardinality of a feature has to be either 0 or 1.";
				return createErrorStatus(message, feature);
			}
			
			if (maxCardinality > 1) {
				String message = "Cloned features are not supported (maximum feature cardinality has to be less than or equal to 1).";
				return createErrorStatus(message, feature);
			}
		}
		
		if (cardinalityBasedElement instanceof DEGroup) {
			DEGroup group = (DEGroup) cardinalityBasedElement;
			List<DEFeature> features = group.getFeatures();
			
			int numberOfFeatures = features.size();
			int numberOfMandatoryFeatures = DEFeatureUtil.getNumberOfMandatoryFeatures(features);
			
			//Minimum cardinality >= # of mandatory features
			if (minCardinality < numberOfMandatoryFeatures) {
				String message = "The current minimum cardinality does not permit selection of all mandatory features.";
				return createErrorStatus(message, group);
			}
			
			//Maximum cardinality <= # of features
			if (maxCardinality > numberOfFeatures) {
				String message = "The current maximum cardinality permits selecting more features than are available.";
				
				//Only warning
				return createWarningStatus(message, group);
			}
		}
		
		return createSuccessStatus();
	}
}
