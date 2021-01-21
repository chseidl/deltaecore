package org.deltaecore.core.generation.extension;

import org.deltaecore.core.extension.DEStaticDeltaDialectExtension;
import org.deltaecore.core.generation.general.DENameGenerator;
import org.deltaecore.core.generation.general.DEProjectSetupGenerator;
import org.eclipse.jdt.core.IJavaProject;

import de.christophseidl.util.eclipse.resourcehandlers.ManifestHandler;
import de.christophseidl.util.eclipse.resourcehandlers.pluginxml.ExtensionElement;
import de.christophseidl.util.eclipse.resourcehandlers.pluginxml.PluginXmlHandler;

public class DEExtensionGenerator {
	private DENameGenerator nameGenerator;
	private DEProjectSetupGenerator projectSetupGenerator;

	public DEExtensionGenerator(DENameGenerator nameGenerator, DEProjectSetupGenerator projectSetupGenerator) {
		this.nameGenerator = nameGenerator;
		this.projectSetupGenerator = projectSetupGenerator;
	}
	
	public void generate() {
		generateOrModifyPluginXml();
		modifyManifest();
	}
	
	private void generateOrModifyPluginXml() {
		IJavaProject dialectJavaProject = projectSetupGenerator.getDialectJavaProject();
		PluginXmlHandler pluginXmlHandler = new PluginXmlHandler(dialectJavaProject);
		
		//"Override" extension every time by deleting previously existing extensions
		
		//If it extends "org.deltaecore.deltadialect" already, remove the extension.
		if (pluginXmlHandler.hasExtension(DEStaticDeltaDialectExtension.EXTENSION_POINT_ID)) {
			pluginXmlHandler.removeExtensions(DEStaticDeltaDialectExtension.EXTENSION_POINT_ID);
		}
		
		ExtensionElement extension = new DEDecoreDialectExtensionElement(nameGenerator);
		pluginXmlHandler.addExtension(extension);

		pluginXmlHandler.save();
	}
	
	private void modifyManifest() {
		IJavaProject dialectJavaProject = projectSetupGenerator.getDialectJavaProject();
		ManifestHandler manifestHandler = new ManifestHandler(dialectJavaProject);
		
		//Make plugin a singleton if necessary (manifest)
		if (!manifestHandler.getSingleton()) {
			manifestHandler.setSingleton(true);
			manifestHandler.save();
		}
	}
}
