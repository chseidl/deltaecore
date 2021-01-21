/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.configuration.resource.deconfiguration.analysis;

import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.configuration.DEFeatureSelection;
import org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolveResult;
import org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolver;
import org.deltaecore.feature.configuration.util.DEConfigurationResolverUtil;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EReference;

public class DEFeatureSelectionFeatureReferenceResolver implements IDeconfigurationReferenceResolver<DEFeatureSelection, DEFeature> {
	
	public void resolve(String identifier, DEFeatureSelection container, EReference reference, int position, boolean resolveFuzzy, final IDeconfigurationReferenceResolveResult<DEFeature> result) {
		DEFeature feature = DEConfigurationResolverUtil.resolveFeature(identifier, container);
		
		if (feature != null) {
			result.addMapping(identifier, feature);
		}
	}
	
	public String deResolve(org.deltaecore.feature.DEFeature feature, DEFeatureSelection container, EReference reference) {
		return DEFeatureResolverUtil.deresolveFeature(feature);
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
