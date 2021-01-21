package org.deltaecore.core.variant.requirements;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.decorebase.util.DEDEcoreBaseUtil;
import org.deltaecore.debug.DEDebug;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import de.christophseidl.util.java.CollectionsUtil;
import de.christophseidl.util.java.util.FormattingDelegate;

public class DEDeltaRequirementsCompleter {
	
	public List<DEDelta> completeRequirements(List<DEDelta> deltas) throws DEDeltaRequirementsCycleException {
		try {
			return completeRequirements(deltas, true);
		}
		catch (DEDeltaAbstractDeltasInInputException e) {
			//This is not happening as abstract deltas are allowed.
			throw new UnsupportedOperationException("Should never get here!");
		}
	}
	
	public List<DEDelta> completeRequirements(List<DEDelta> deltas, boolean allowAbstractDeltasInInput) throws DEDeltaAbstractDeltasInInputException, DEDeltaRequirementsCycleException {
		if (!allowAbstractDeltasInInput) {
			checkForAbstractDeltasInInput(deltas);
		}
		
		deltas = removeDuplicateDeltas(deltas);
		
		List<DEDelta> completedRequirements = new ArrayList<DEDelta>();
		
		for (DEDelta delta : deltas) {
			List<DEDelta> transitivelyRequiredElements = DEDEcoreResolverUtil.getAllTransitivelyRequiredDeltas(delta);
			
			for (DEDelta requiredDelta : transitivelyRequiredElements) {
				if (!DEDEcoreBaseUtil.containsSimilar(requiredDelta, completedRequirements)) {
					completedRequirements.add(requiredDelta);
				}
//				else {
//					DEDebug.println("Discarding duplicate required element: " + requiredElement);
//				}
			}
		}
		
		DEDebug.println("Completed requirements: " + CollectionsUtil.toString(completedRequirements, new FormattingDelegate<EObject>() {
			@Override
			public String format(EObject value) {
				if (value instanceof DEDelta) {
					DEDelta delta = (DEDelta) value;
					return delta.getName();
				}
				
				Resource resource = value.eResource();
				URI uri = resource.getURI();
				
				return uri.lastSegment();
			}
		}));
		
		return completedRequirements;
	}
	
	private void checkForAbstractDeltasInInput(List<DEDelta> originalDeltas) throws DEDeltaAbstractDeltasInInputException {
		List<DEDelta> abstractDeltas = new ArrayList<DEDelta>();
		
		for (DEDelta originalDelta : originalDeltas) {
			if (originalDelta.isAbstract()) {
				abstractDeltas.add(originalDelta);
			}
		}
		
		if (!abstractDeltas.isEmpty()) {
			throw new DEDeltaAbstractDeltasInInputException(abstractDeltas);
		}
	}
	
	public static List<DEDelta> removeDuplicateDeltas(List<DEDelta> deltas) {
		//Check if the same delta appears multiple times in the input and if so remove later references.
		List<DEDelta> cleanedDeltas = new ArrayList<DEDelta>();
		
		for (DEDelta delta : deltas) {
			if (!DEDEcoreBaseUtil.containsSimilar(delta, cleanedDeltas)) {
				cleanedDeltas.add(delta);
			} else {
				DEDebug.println("Discarding duplicate delta " + delta.getName() + " (" + delta + " )");
			}
		}
		
		return cleanedDeltas;
	}
}
