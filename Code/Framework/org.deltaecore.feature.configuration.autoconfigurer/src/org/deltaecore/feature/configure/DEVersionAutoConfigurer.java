package org.deltaecore.feature.configure;

import java.util.ArrayList;
import java.util.List;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.constraint.DEConstraintModel;

import solver.Solver;
import util.ESat;

//Expected input: a preconfiguration of the HFM satisfying the following
//- selection of features must satisfy the underlying FM  and its (non-version aware) constraints
//- if there are versions in the configuration, they must satisfy the rules of the HFM and its
//  constraints (at most one per feature, not violate version range restrictions etc.)
//
//Output: a list of all valid configurations of the HFM (if one exists) containing exactly one version of
//each initially selected feature that satisfies the HFM and its constraints.
public class DEVersionAutoConfigurer {
	
	public static DEConfiguration autoCompleteVersionsInConfiguration(DEConfiguration initialConfiguration, DEConstraintModel constraintModel) {
		
		return findSolutionTheOldWay(initialConfiguration, constraintModel);
		//return findSolutionTheNewWay(initialConfiguration, constraintModel);
	}
	
	/**
	 * The old implementation of finding the best matching configuration<br />
	 * <b style="color:red">CAUTION!</b> Feasible, but not scalable and efficient.
	 * @param initialConfiguration  
	 * @param constraintModel 
	 * @return The best matching solution
	 */
	private static DEConfiguration findSolutionTheOldWay(DEConfiguration initialConfiguration, DEConstraintModel constraintModel) {
		Solver solver = new Solver("DEFEatureModel Version Auto-Configuration");
		DEFeatureModelEncoderAndDecoder encoderAndDecoder = new DEFeatureModelEncoderAndDecoder(solver);
		
		encoderAndDecoder.encodeConfiguration(initialConfiguration, constraintModel);
		
		solver.findSolution();
		
		List<DEConfiguration> allPossibleConfigurations = new ArrayList<DEConfiguration>();
		
		if (solver.isFeasible() == ESat.TRUE) {
			do {
				DEConfiguration configuration = encoderAndDecoder.decodeCurrentConfiguration();
				allPossibleConfigurations.add(configuration);
			} while (solver.nextSolution());
		}
		
		allPossibleConfigurations = DEConfigurationScorer.sortConfigurationsByScore(allPossibleConfigurations);
		
		if (allPossibleConfigurations.isEmpty()) {
			//TODO: Better exception
			throw new UnsupportedOperationException("Unable to determine valid version constellation.");
		}
		
		DEConfiguration bestConfiguration = allPossibleConfigurations.get(0);
		return bestConfiguration;
	}
	
//	/**
//	 * The new implementation of finding the best matching configuration using an evaluation method
//	 * @param initialConfiguration
//	 * @param constraintModel
//	 * @return The best matching solution
//	 */
//	private static DEConfiguration findSolutionTheNewWay(DEConfiguration initialConfiguration, DEConstraintModel constraintModel) {
//		Solver solver = new Solver("DEFEatureModel Version Auto-Configuration");
//		DEFeatureModelEncoderAndDecoder encoderAndDecoder = new DEFeatureModelEncoderAndDecoder(solver);
//		
//		encoderAndDecoder.encodeConfiguration(initialConfiguration, constraintModel);
//		
//		DEConfigurationScore scorer = new DEConfigurationLatestScore(solver, encoderAndDecoder);
//		List<IntVar<?>> objectives = scorer.encodeScore();
//		
//		if(objectives.size() == 1) {
//			IntVar<?> objective = objectives.get(0);
//			solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, objective);
//		} else {
//			// more or less objectives shouldn't appear here
//			throw new UnsupportedOperationException("Unable to execute solver. Invalid number of objective to optimize found (should be one).");
//		}
//		
//		DEConfiguration configuration = null;
//		if (solver.isFeasible() == ESat.TRUE) {
//			configuration = encoderAndDecoder.decodeCurrentConfiguration();
//		}
//		
//		if (configuration == null) {
//			//TODO: Better exception
//			throw new UnsupportedOperationException("Unable to determine valid version constellation.");
//		}
//		
//		return configuration;
//	}
}
