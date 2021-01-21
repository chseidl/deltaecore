package org.deltaecore.core.variant.order;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.applicationorderconstraint.DEApplicationOrderConstraintModel;
import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.variant.dependency.DEDependencyGraph;
import org.deltaecore.debug.DEDebug;

import de.christophseidl.util.java.CollectionsUtil;
import de.christophseidl.util.java.util.FormattingDelegate;

public class DEDeltaSorter {
	
	public List<DEDelta> sort(List<DEDelta> deltas) {
		return sort(deltas, null);
	}
	
	public List<DEDelta> sort(List<DEDelta> deltas, DEApplicationOrderConstraintModel aocModel) {
		DEDependencyGraph dependencyGraph = new DEDependencyGraph(deltas);
		
		//Factor in application order constraints if present
		if (aocModel != null) {
			dependencyGraph.processApplicationOrderConstraints(aocModel);
		}
		
		List<DEDelta> orderedDeltas = new ArrayList<DEDelta>();
		
		while(dependencyGraph.hasUnprocessedNodes()) {
			List<DEDelta> deltasOfLevel = dependencyGraph.placeDeltasOfCurrentLevel();
			
			if (deltasOfLevel.isEmpty()) {
				@SuppressWarnings("deprecation")
				String debugMessage = dependencyGraph.createInternalStatusMessage();
				DEDebug.println(debugMessage);
				
				throw new UnsupportedOperationException("No nodes without requirements detected in this cycle. This might be a dependency cycle or a programming error.");
			}
			
			
			//Place all the nodes of this cycle in a sequence.
			for (DEDelta delta : deltasOfLevel) {
				//Add to the end of the list as the already placed deltas were (indirectly) required by this delta.
				orderedDeltas.add(delta);
			}
		}
		
		DEDebug.println("Determined delta order: " + CollectionsUtil.toString(orderedDeltas, new FormattingDelegate<DEDelta>() {
			@Override
			public String format(DEDelta value) {
				return value.getName();
			}
		}));
		
		return orderedDeltas;
	}
}
