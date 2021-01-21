package org.deltaecore.feature.variant.extension;

import java.util.LinkedList;
import java.util.List;

import org.deltaecore.debug.DEDebug;
import org.eclipse.core.runtime.IExtension;

import de.christophseidl.util.eclipse.ExtensionUtil;

public class DEVariantGeneratorRegistry {
	private static DEVariantGeneratorRegistry instance = null;
	
	private List<DEVariantGeneratorExtension> variantGeneratorExtensions;
	
	private static DEVariantGeneratorRegistry getInstance() {
		if (instance == null) {
			instance = new DEVariantGeneratorRegistry();
		}
		
		return instance;
	}
	
	private DEVariantGeneratorRegistry() {
		variantGeneratorExtensions = new LinkedList<DEVariantGeneratorExtension>();
		
		initializeExtensions();
	}
	
	private void initializeExtensions() {
		List<IExtension> rawExtensions = ExtensionUtil.getExtensions(DEVariantGeneratorExtension.EXTENSION_POINT_ID);
		
		DEDebug.println("=================================================");
		DEDebug.println("Found " + rawExtensions.size() + " extension(s) to \"" + DEVariantGeneratorExtension.EXTENSION_POINT_ID + "\".");
		
		for (IExtension rawExtension : rawExtensions) {
			DEVariantGeneratorExtension variantGeneratorExtension = new DEVariantGeneratorExtension(rawExtension);
			registerVariantGeneratorExtension(variantGeneratorExtension);
		}
		
		DEDebug.println("=================================================");
	}
	
	private void registerVariantGeneratorExtension(DEVariantGeneratorExtension variantGeneratorExtension) {
		variantGeneratorExtensions.add(variantGeneratorExtension);
		
		String name = variantGeneratorExtension.getName();
		DEDebug.println("Registered variant generator \"" + name + "\".");
	}

	public static List<DEVariantGeneratorExtension> getVariantGeneratorExtensions() {
		//Make a defensive copy
		return new LinkedList<DEVariantGeneratorExtension>(getInstance().doGetVariantGeneratorExtensions());
	}
	
	public List<DEVariantGeneratorExtension> doGetVariantGeneratorExtensions() {
		return variantGeneratorExtensions;
	}
	
	public static DEVariantGeneratorExtension getExtensionById(String id) {
		return getInstance().doGetExtensionById(id);
	}
	
	public DEVariantGeneratorExtension doGetExtensionById(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		
		for (DEVariantGeneratorExtension variantGeneratorExtension : variantGeneratorExtensions) {
			if (id.equals(variantGeneratorExtension.getId())) {
				return variantGeneratorExtension;
			}
		}
		
		return null;
	}
}
