/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.configuration.resource.deconfiguration.analysis;

import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EReference;

import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEConfigurationFeatureModelReferenceResolver implements org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolver<org.deltaecore.feature.configuration.DEConfiguration, org.deltaecore.feature.DEFeatureModel> {
	
	private org.deltaecore.feature.configuration.resource.deconfiguration.analysis.DeconfigurationDefaultResolverDelegate<org.deltaecore.feature.configuration.DEConfiguration, org.deltaecore.feature.DEFeatureModel> delegate = new org.deltaecore.feature.configuration.resource.deconfiguration.analysis.DeconfigurationDefaultResolverDelegate<org.deltaecore.feature.configuration.DEConfiguration, org.deltaecore.feature.DEFeatureModel>();
	
	public void resolve(String identifier, org.deltaecore.feature.configuration.DEConfiguration container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolveResult<org.deltaecore.feature.DEFeatureModel> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
	}
	
	public String deResolve(org.deltaecore.feature.DEFeatureModel featureModel, org.deltaecore.feature.configuration.DEConfiguration container, EReference reference) {
		IPath relativeFeatureModelPath = EcoreResolverUtil.resolveRelativePathBetweenModels(featureModel, container);
		return relativeFeatureModelPath.toString();
	}

	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
