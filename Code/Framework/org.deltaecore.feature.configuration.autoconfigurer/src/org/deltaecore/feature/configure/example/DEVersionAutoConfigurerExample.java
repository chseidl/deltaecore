package org.deltaecore.feature.configure.example;

import org.deltaecore.debug.DEDebug;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.configure.DEVersionAutoConfigurer;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.constraint.util.DEConstraintFormatter;

public class DEVersionAutoConfigurerExample {
	public static void main(String[] args) {
		DEFeatureModelSampleCreator hfmSampleCreator = new DEFeatureModelSampleCreator();
		
		//Setup
		DEConfiguration initialConfigurationElements = createConfiguration1(hfmSampleCreator);
		DEConstraintModel constraintModel = hfmSampleCreator.getConstraintModel();
		
		DEDebug.println("Constraint model");
		DEDebug.println(DEConstraintFormatter.formatConstraintModel(constraintModel));
		
		
		DEDebug.println("Initial configuration:");
//		DEFeatureUtil.printConfiguration(initialConfiguration);
		DEDebug.println();
		
		
		//Execution
//		List<List<? extends DEConfigurationArtifact>> allPossibleConfigurations = 
		DEVersionAutoConfigurer.autoCompleteVersionsInConfiguration(initialConfigurationElements, constraintModel);
		
		//Ouput
//		printAllPossibleConfigurations(allPossibleConfigurations);
	}
	
	protected static DEConfiguration createConfiguration1(DEFeatureModelSampleCreator hfmSampleCreator) {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getTurtleBotFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getEngineFeature(), configuration, false);
		
		return configuration;
	}
	
	protected static DEConfiguration createConfiguration2(DEFeatureModelSampleCreator hfmSampleCreator) {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getTurtleBotFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getEngineFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getMovementFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getKeyboardFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getWebserviceFeature(), configuration, false);
		
		return configuration;
	}
	
	protected static DEConfiguration createConfiguration3(DEFeatureModelSampleCreator hfmSampleCreator) {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getTurtleBotFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getEngineFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getMovementFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getGamepadFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getWebserviceFeature(), configuration, false);
		
		return configuration;
	}
	
	protected static DEConfiguration createConfiguration4(DEFeatureModelSampleCreator hfmSampleCreator) {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getTurtleBotFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getEngineFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getMovementFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getAutonomousFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getDetectionFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getBumpFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getInfraredFeature(), configuration, false);
		
		return configuration;
	}
	
	protected static DEConfiguration createConfiguration5(DEFeatureModelSampleCreator hfmSampleCreator) {
		DEConfiguration configuration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getTurtleBotFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getEngineFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getMovementFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getAutonomousFeature(), configuration, false);
		
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getDetectionFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getBumpFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getInfraredFeature(), configuration, false);
		DEConfigurationUtil.addFeatureToConfiguration(hfmSampleCreator.getUltrasoundFeature(), configuration, false);
		
		return configuration;
	}
}
