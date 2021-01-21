/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.feature.expression.resource.expression.analysis;

import java.util.Map;

import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.expression.util.DEExpressionResolverUtil;
import org.deltaecore.feature.util.DEFeatureResolverUtil;
import org.eclipse.emf.ecore.EReference;

public class DERelativeVersionRestrictionVersionReferenceResolver implements org.deltaecore.feature.expression.resource.expression.IExpressionReferenceResolver<org.deltaecore.feature.expression.DERelativeVersionRestriction, org.deltaecore.feature.DEVersion> {
	
	public void resolve(String identifier, org.deltaecore.feature.expression.DERelativeVersionRestriction container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.feature.expression.resource.expression.IExpressionReferenceResolveResult<org.deltaecore.feature.DEVersion> result) {
		DEVersion version = DEExpressionResolverUtil.resolveVersion(identifier, container);
		
		if (version != null) {
			result.addMapping(identifier, version);
		}
	}
	
	public String deResolve(org.deltaecore.feature.DEVersion element, org.deltaecore.feature.expression.DERelativeVersionRestriction container, EReference reference) {
		return DEFeatureResolverUtil.deresolveVersion(element);
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
