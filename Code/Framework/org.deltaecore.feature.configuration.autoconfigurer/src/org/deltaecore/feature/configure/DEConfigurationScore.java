package org.deltaecore.feature.configure;

import java.util.List;

import solver.Solver;
import solver.variables.IntVar;

public abstract class DEConfigurationScore {
	
	protected Solver solver;
	protected DEFeatureModelEncoderAndDecoder encoderAndDecoder;
	
	public DEConfigurationScore(Solver solver, DEFeatureModelEncoderAndDecoder encoderAndDecoder) {
		this.solver = solver;
		this.encoderAndDecoder = encoderAndDecoder;
	}
	
	/**
	 * Starts the scoring process and encodes the scoring into the solver
	 * @return Returns the optimization objectives
	 */
	public abstract List<IntVar<?>> encodeScore();
}
