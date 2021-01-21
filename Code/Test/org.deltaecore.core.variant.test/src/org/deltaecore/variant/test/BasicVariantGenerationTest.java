package org.deltaecore.variant.test;

import org.junit.Test;

//TODO: The CFD delta dialect seems to act up (don't know how or why). Doesn'T come to "completed test case" output but doesn't give any hint.
public class BasicVariantGenerationTest extends AbstractVariantGenerationTest {
	@Test
	public void testBasicVariantGeneration1(){
		testSuccessfulVariantGeneration("BasicVariantGeneration1", "SFT", new String[] {"SFT_Delta1"});
	}
	
//	@Test
//	public void testBasicVariantGeneration2(){
//		testSuccessfulVariantGeneration("BasicVariantGeneration2", "CFD", new String[] {"CFD_Delta1"});
//	}
	
	@Test
	public void testBasicVariantGeneration3(){
		testSuccessfulVariantGeneration("BasicVariantGeneration3", "GSN", new String[] {"GSN_Delta1"});
	}
	
	@Test
	public void testBasicVariantGeneration4(){
		testSuccessfulVariantGeneration("BasicVariantGeneration4", "FeatureModel", new String[] {"EvolutionIteration1"});
	}
	
	@Test
	public void testBasicVariantGeneration5(){
		testSuccessfulVariantGeneration("BasicVariantGeneration5", "Ecore", new String[] {"ModifySFTMetaModel"});
	}
	
	
//	@Test
//	public void testVariantGenerationWithIndependentDeltas1(){
//		testSuccessfulVariantGeneration("VariantGenerationWithIndependentDeltas1", "CFD", new String[] {"CFD_Delta1", "CFD_Delta2"});
//	}
	
	
	
	@Test
	public void testVariantGenerationWithRequiredDeltas1() {
		testSuccessfulVariantGeneration("VariantGenerationWithRequiredDeltas1", "SFT", new String[] {"SFT_Delta2"});
	}
	
//	@Test
//	public void testVariantGenerationWithRequiredDeltas2(){
//		testSuccessfulVariantGeneration("VariantGenerationWithRequiredDeltas2", "CFD", new String[] {"CFD_Delta2", "CFD_Delta3"});
//	}
	
	@Test
	public void testVariantGenerationWithRequiredDeltas3() {
		testSuccessfulVariantGeneration("VariantGenerationWithRequiredDeltas3", "FeatureModel", new String[] {"EvolutionIteration3"});
	}
	
	
	
	@Test
	public void testVariantGenerationWithRedundantDeltas1() {
		testSuccessfulVariantGeneration("VariantGenerationWithRedundantDeltas1", "SFT", new String[] {"SFT_Delta1", "SFT_Delta2"});
	}
	
//	@Test
//	public void testVariantGenerationWithRedundantDeltas2(){
//		testSuccessfulVariantGeneration("VariantGenerationWithRedundantDeltas2", "CFD", new String[] {"CFD_Delta1", "CFD_Delta2", "CFD_Delta3"});
//	}
	
	@Test
	public void testVariantGenerationWithRedundantDeltas3() {
		testSuccessfulVariantGeneration("VariantGenerationWithRedundantDeltas3", "FeatureModel", new String[] {"EvolutionIteration2", "EvolutionIteration3"});
	}
	
	@Test
	public void testVariantGenerationWithRedundantDeltas4() {
		testSuccessfulVariantGeneration("VariantGenerationWithRedundantDeltas4", "FeatureModel", new String[] {"EvolutionIteration1", "EvolutionIteration4"});
	}
}
