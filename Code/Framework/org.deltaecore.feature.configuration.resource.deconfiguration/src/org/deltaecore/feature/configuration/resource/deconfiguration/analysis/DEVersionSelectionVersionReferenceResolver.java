/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.configuration.resource.deconfiguration.analysis;

import java.util.Map;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.configuration.DEVersionSelection;
import org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolveResult;
import org.deltaecore.feature.configuration.resource.deconfiguration.IDeconfigurationReferenceResolver;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EReference;

public class DEVersionSelectionVersionReferenceResolver implements IDeconfigurationReferenceResolver<DEVersionSelection, DEVersion> {
	
	public void resolve(String identifier, DEVersionSelection container, EReference reference, int position, boolean resolveFuzzy, final IDeconfigurationReferenceResolveResult<DEVersion> result) {
		DEFeature feature = container.getFeature();
		
		if (feature != null) {
			DEVersion version = DEFeatureResolverUtil.resolveVersion(identifier, feature);
			
			if (version != null) {
				result.addMapping(identifier, version);
			}
		}
	}
	
	public String deResolve(DEVersion version, DEVersionSelection container, EReference reference) {
		return DEFeatureResolverUtil.deresolveVersion(version);
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
