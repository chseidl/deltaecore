package org.deltaecore.core.decoredialect.impl.custom;

import org.deltaecore.core.decoredialect.impl.DEModelElementParameterImpl;

//TODO: Might be obsolete without fuzzy matching of references
public class DEModelElementParameterImplCustom extends DEModelElementParameterImpl {

//	@Override
//	public EReference getReference() {
//		EReference containingReference = super.getContainingReference();
//		
//		if (containingReference != null) {
//			return containingReference;
//		}
//		
//		//No containing reference specified - make a guess.
//		List<EReference> containingReferenceCandidates = DEDEcoreDialectResolverUtil.resolveContainingReferenceCandidates(this);
//		
//		if (containingReferenceCandidates.isEmpty()) {
//			return null;
//		}
//		
//		return containingReferenceCandidates.get(0);
//	}
}
