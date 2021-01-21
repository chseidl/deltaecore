package org.deltaecore.core.variant;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deltaecore.core.applicationorderconstraint.DEApplicationOrderConstraintModel;
import org.deltaecore.core.applicationorderconstraint.util.DEApplicationOrderConstraintIOUtil;
import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.util.DEDEcoreResolverUtil;
import org.deltaecore.core.decore.util.DEDeltaRequirementsCycleException;
import org.deltaecore.core.variant.interpretation.DEDeltaInterpreter;
import org.deltaecore.core.variant.order.DEDeltaSorter;
import org.deltaecore.core.variant.requirements.DEDeltaRequirementsCompleter;
import org.deltaecore.debug.DEDebug;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import de.christophseidl.util.ecore.EcoreCopier2;
import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEVariantCreator {
	private static DEDeltaRequirementsCompleter deltaCompleter = new DEDeltaRequirementsCompleter();
	private static DEDeltaSorter deltaSorter = new DEDeltaSorter();
	private static DEDeltaInterpreter deltaInterpreter = new DEDeltaInterpreter();
	
	
	private boolean loadApplicationOrderConstraintModelAutomatically;
	private DEApplicationOrderConstraintModel applicationOrderConstraintModel;
	
	public DEVariantCreator() {
		loadApplicationOrderConstraintModelAutomatically = true;
		applicationOrderConstraintModel = null;
	}
	
	//Full variant derivation
	public void createAndSaveVariantFromDeltaFiles(List<IFile> deltaFiles, IFolder variantFolder) throws DEDeltaRequirementsCycleException {
		List<DEDelta> deltas = EcoreIOUtil.loadModels(deltaFiles);
		createAndSaveVariantFromDeltas(deltas, variantFolder);
	}

	public List<Resource> createVariantFromDeltaFiles(List<IFile> deltaFiles) throws DEDeltaRequirementsCycleException {
		List<DEDelta> deltas = EcoreIOUtil.loadModels(deltaFiles);
		return createVariantFromDeltas(deltas);
	}
	
	public void createAndSaveVariantFromDeltas(List<DEDelta> originalDeltas, IFolder variantFolder) throws DEDeltaRequirementsCycleException {
		List<Resource> affectedResources = createVariantFromDeltas(originalDeltas);
		
		//Write all affected resources to disk
		EcoreIOUtil.saveResourcesAs(affectedResources, variantFolder);
	}

	
	//Actual variant derivation procedure
	public List<Resource> createVariantFromDeltas(List<DEDelta> deltas) throws DEDeltaRequirementsCycleException {
		DEDebug.println("=============== Started FULL variant creation. ===============");
		DEDebug.println();
		
		List<Resource> affectedResources = doCreateVariantFromDeltas(deltas);
		
		DEDebug.println();
		DEDebug.println("=============== Finished FULL variant creation. ===============");
		DEDebug.println();
		DEDebug.println();
		
		return affectedResources;
	}
	
	/**
	 * @param originalDeltas The deltas to be applied in the course of variant creation.
	 * @return A list of resources that were affected (modified or created) during variant creation.
	 * 
	 * @throws DEDeltaRequirementsCycleException
	 */
	protected List<Resource> doCreateVariantFromDeltas(List<DEDelta> originalDeltas) throws DEDeltaRequirementsCycleException {
		//TODO: Reload all models to avoid caching issues!!! (take care with building partial variants, some other issues here!)
//		reloadAllModels(deltas);
		
		if (originalDeltas == null || originalDeltas.isEmpty()) {
			//TODO: throw an exception on failure
			return null;
		}
		
		//Collect all transitively required delta modules.
		List<DEDelta> originalRequiredDeltas = deltaCompleter.completeRequirements(originalDeltas);
		
		
		//Collect the modified resources from these delta modules (They have to be instantiated once as they are only referenced via file paths).
		List<Resource> originalModifiedResources = DEDEcoreResolverUtil.collectModifiedResources(originalRequiredDeltas);

		//Copy resources of the entirety of delta modules and modified resources to a new resource set.
		//Create a self-contained copy to avoid tempering with original models that are referenced elsewhere but keep a mapping of originals to copies.
		EcoreCopier2 copier = new EcoreCopier2();
		
		List<DEDelta> copiedRequiredDeltaModules = copier.copyAllWithResources(originalRequiredDeltas);
		List<Resource> copiedModifiedResources = copier.copyAllResources(originalModifiedResources);
		
		//Keep a copy of the mapping between original and copied elements for inverse lookup (e.g., for creating Eclipse error markers)
		Map<EObject, EObject> originalToCopyMap = new HashMap<EObject, EObject>(copier);
		
		
			
		//Sort the copied delta modules.
		//Factor in application order constraints depending on the settings on how to process them
		DEApplicationOrderConstraintModel localApplicationOrderConstraintModel = null;
		
		if (loadApplicationOrderConstraintModelAutomatically) {
			localApplicationOrderConstraintModel = DEApplicationOrderConstraintIOUtil.loadApplicationOrderConstraintModel(copiedRequiredDeltaModules);
		} else {
			localApplicationOrderConstraintModel = applicationOrderConstraintModel;
		}
		
		List<DEDelta> sortedDeltas = deltaSorter.sort(copiedRequiredDeltaModules, localApplicationOrderConstraintModel);

		
		List<Resource> affectedResources = new LinkedList<Resource>(copiedModifiedResources);
		
		
		//Interpret the deltas on this set of copied resources.
		for (DEDelta delta : sortedDeltas) {
			DEDebug.println("Interpreting delta \"" + delta.getName() + "\".");
			List<Resource> affectedResourcesOfDeltaModule = deltaInterpreter.interpret(delta, affectedResources, originalToCopyMap);
			
			//This is not particularly good for performance - change some time.
			for (Resource affectedResourceOfDeltaModule : affectedResourcesOfDeltaModule) {
				if (!affectedResources.contains(affectedResourceOfDeltaModule)) {
					affectedResources.add(affectedResourceOfDeltaModule);
				}
			}
		}
		
		return affectedResources;
	}
	
	//Partial variant derivation
	public void updatePartialVariantAsBasisForDelta(DEDelta originalDelta) {
		DEDebug.println("=============== Started PARTIAL variant creation for \"" + originalDelta.getName() + "\". ===============");
		DEDebug.println();
		
		try {
			doCreateVariantFromDeltas(Collections.singletonList(originalDelta));
		} catch (DEDeltaRequirementsCycleException e) {
			//TODO: Create error marker
			System.err.println("Cycle in requirements");
		}
		
		DEDebug.println();
		DEDebug.println("=============== Finished PARTIAL variant creation for \"" + originalDelta.getName() + "\". ===============");
		DEDebug.println();
		DEDebug.println();
	}

	
	
	public boolean getLoadApplicationOrderConstraintModelAutomatically() {
		return loadApplicationOrderConstraintModelAutomatically;
	}

	public void setLoadApplicationOrderConstraintModelAutomatically(boolean loadApplicationOrderConstraintModelAutomatically) {
		this.loadApplicationOrderConstraintModelAutomatically = loadApplicationOrderConstraintModelAutomatically;
	}

	public DEApplicationOrderConstraintModel getApplicationOrderConstraintModel() {
		return applicationOrderConstraintModel;
	}

	/**
	 * Sets the application order constraint model manually. This automatically disables automatic loading of the application order constraint model.
	 */
	public void setApplicationOrderConstraintModel(DEApplicationOrderConstraintModel applicationOrderConstraintModel) {
		this.applicationOrderConstraintModel = applicationOrderConstraintModel;
		setLoadApplicationOrderConstraintModelAutomatically(false);
	}
}
