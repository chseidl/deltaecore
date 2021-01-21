package org.deltaecore.feature.variant.extension;

import org.deltaecore.feature.variant.DEVariantGenerator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public class DEVariantGeneratorExtension {
	public static final String EXTENSION_POINT_ID = "org.deltaecore.feature.variant.generator";
	
	private String id;
	private String name;
	private DEVariantGenerator generator;
	
	public DEVariantGeneratorExtension(IExtension rawExtension) {
		loadRawExtension(rawExtension);
	}
	
//	public DEVariantGeneratorExtension(String id, String name, DEVariantGenerator generator) {
//		this.id = id;
//		this.name = name;
//		this.generator = generator;
//	}
	
	private void loadRawExtension(IExtension rawExtension) {
		IConfigurationElement[] configurationElements = rawExtension.getConfigurationElements();
		
		try {
			for (IConfigurationElement configurationElement : configurationElements) {
				String configurationElementName = configurationElement.getName();
				
				if (configurationElementName.equals("variant_generator")) {
					id = configurationElement.getAttribute("id");
					name = configurationElement.getAttribute("name");
					
					Object generatorObject = configurationElement.createExecutableExtension("generator");
					
					if (generatorObject instanceof DEVariantGenerator) {
						generator = (DEVariantGenerator) generatorObject;
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public DEVariantGenerator getGenerator() {
		return generator;
	}
}
