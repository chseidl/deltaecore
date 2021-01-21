package org.deltaecore.suite.mapping.util;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.DEDeltaInvokation;
import org.deltaecore.feature.expression.DEExpression;
import org.deltaecore.feature.expression.util.DEExpressionEvaluator;
import org.deltaecore.suite.mapping.DEMapping;

public abstract class DEMappingEvaluator extends DEExpressionEvaluator {
	public List<DEDelta> evaluateMapping(DEMapping mapping) {
		DEExpression expression = mapping.getExpression();
		boolean expressionMatchesConfiguration = evaluateExpression(expression);
		
		if (expressionMatchesConfiguration) {
			//TODO: Perspectively, this should return the list of delta invokations
			//so that potential arguments to parameterized deltas can be evaluated.
			List<DEDelta> deltas = new ArrayList<DEDelta>();
			List<DEDeltaInvokation> deltaInvokations = mapping.getDeltaInvokations();
			
			for (DEDeltaInvokation deltaInvokation : deltaInvokations) {
				DEDelta delta = deltaInvokation.getDelta();
				deltas.add(delta);
			}
			
			return deltas;
		}
		
		return new ArrayList<DEDelta>();
	}
}
