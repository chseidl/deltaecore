package org.deltaecore.core.decore.resource.decore.postprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.decore.resource.decore.IDecoreOptionProvider;
import org.deltaecore.core.decore.resource.decore.IDecoreOptions;
import org.deltaecore.core.decore.resource.decore.IDecoreResourcePostProcessor;
import org.deltaecore.core.decore.resource.decore.IDecoreResourcePostProcessorProvider;
import org.deltaecore.core.decore.resource.decore.mopp.DecoreResource;
import org.deltaecore.debug.DEDebug;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.emf.ecore.EObject;

import de.christophseidl.util.eclipse.ExtensionUtil;

/**
 * Allow multiple post processors.
 */
public class DEDEcorePostProcessorDistributor implements IDecoreOptionProvider, IDecoreResourcePostProcessorProvider, IDecoreResourcePostProcessor {
	
	@Override
	public Map<?, ?> getOptions() {
		return Collections.singletonMap(IDecoreOptions.RESOURCE_POSTPROCESSOR_PROVIDER, this);
	}

	@Override
	public IDecoreResourcePostProcessor getResourcePostProcessor() {
		return this;
	}

	@Override
	public void process(DecoreResource resource) {
		EObject root = resource.getContents().get(0);
		
		if (root instanceof DEDelta) {
			DEDelta delta = (DEDelta) root;
			
			List<IDEDecorePostProcessor> postProcessors = getPostProcessors();
			
			for (IDEDecorePostProcessor postProcessor : postProcessors) {
				try {
					postProcessor.process(delta);
				} catch(Exception e) {
					e.printStackTrace();
					DEDebug.println("ERROR: Post processor threw exception (" + e.getMessage() + ").");
				}
			}
		}
	}

	private List<IDEDecorePostProcessor> getPostProcessors() {
		List<IDEDecorePostProcessor> postProcessors = new ArrayList<IDEDecorePostProcessor>();
		
		List<IExtension> extensions = ExtensionUtil.getExtensions("org.deltaecore.core.decore.resource.decore.postprocessor");
		
		for (IExtension extension : extensions) {
			IConfigurationElement configurationElement = ExtensionUtil.getConfigurationElement("postprocessor", extension);
			try {
				IDEDecorePostProcessor postProcessor = (IDEDecorePostProcessor) configurationElement.createExecutableExtension("class");
				postProcessors.add(postProcessor);
			} catch (CoreException e) {
				e.printStackTrace();
				DEDebug.println("ERROR: Failed to load *.decore postprocessor. (" + e.getMessage() + ")");
			}
		}
		
		return postProcessors;
	}
	
	@Override
	public void terminate() {
	}
}
