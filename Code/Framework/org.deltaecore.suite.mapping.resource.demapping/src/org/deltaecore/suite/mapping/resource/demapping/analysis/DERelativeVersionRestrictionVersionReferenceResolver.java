/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.suite.mapping.resource.demapping.analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class DERelativeVersionRestrictionVersionReferenceResolver implements org.deltaecore.suite.mapping.resource.demapping.IDemappingReferenceResolver<org.deltaecore.feature.expression.DERelativeVersionRestriction, org.deltaecore.feature.DEVersion> {
	
	private org.deltaecore.feature.expression.resource.expression.analysis.DERelativeVersionRestrictionVersionReferenceResolver delegate = new org.deltaecore.feature.expression.resource.expression.analysis.DERelativeVersionRestrictionVersionReferenceResolver();
	
	public void resolve(String identifier, org.deltaecore.feature.expression.DERelativeVersionRestriction container, EReference reference, int position, boolean resolveFuzzy, final org.deltaecore.suite.mapping.resource.demapping.IDemappingReferenceResolveResult<org.deltaecore.feature.DEVersion> result) {
		delegate.resolve(identifier, container, reference, position, resolveFuzzy, new org.deltaecore.feature.expression.resource.expression.IExpressionReferenceResolveResult<org.deltaecore.feature.DEVersion>() {
			
			public boolean wasResolvedUniquely() {
				return result.wasResolvedUniquely();
			}
			
			public boolean wasResolvedMultiple() {
				return result.wasResolvedMultiple();
			}
			
			public boolean wasResolved() {
				return result.wasResolved();
			}
			
			public void setErrorMessage(String message) {
				result.setErrorMessage(message);
			}
			
			public Collection<org.deltaecore.feature.expression.resource.expression.IExpressionReferenceMapping<org.deltaecore.feature.DEVersion>> getMappings() {
				throw new UnsupportedOperationException();
			}
			
			public String getErrorMessage() {
				return result.getErrorMessage();
			}
			
			public void addMapping(String identifier, URI newIdentifier) {
				result.addMapping(identifier, newIdentifier);
			}
			
			public void addMapping(String identifier, URI newIdentifier, String warning) {
				result.addMapping(identifier, newIdentifier, warning);
			}
			
			public void addMapping(String identifier, org.deltaecore.feature.DEVersion target) {
				result.addMapping(identifier, target);
			}
			
			public void addMapping(String identifier, org.deltaecore.feature.DEVersion target, String warning) {
				result.addMapping(identifier, target, warning);
			}
			
			public Collection<org.deltaecore.feature.expression.resource.expression.IExpressionQuickFix> getQuickFixes() {
				return Collections.emptySet();
			}
			
			public void addQuickFix(final org.deltaecore.feature.expression.resource.expression.IExpressionQuickFix quickFix) {
				result.addQuickFix(new org.deltaecore.suite.mapping.resource.demapping.IDemappingQuickFix() {
					
					public String getImageKey() {
						return quickFix.getImageKey();
					}
					
					public String getDisplayString() {
						return quickFix.getDisplayString();
					}
					
					public Collection<EObject> getContextObjects() {
						return quickFix.getContextObjects();
					}
					
					public String getContextAsString() {
						return quickFix.getContextAsString();
					}
					
					public String apply(String currentText) {
						return quickFix.apply(currentText);
					}
				});
			}
		});
		
	}
	
	public String deResolve(org.deltaecore.feature.DEVersion element, org.deltaecore.feature.expression.DERelativeVersionRestriction container, EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
