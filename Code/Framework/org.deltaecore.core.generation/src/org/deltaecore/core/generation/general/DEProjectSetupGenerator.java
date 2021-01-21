package org.deltaecore.core.generation.general;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.jdt.core.IJavaProject;

import de.christophseidl.util.eclipse.generation.EclipseGenerationUtil;
import de.christophseidl.util.eclipse.resourcehandlers.ManifestHandler;

public class DEProjectSetupGenerator {
	private DENameGenerator nameGenerator;
	
	private IJavaProject dialectJavaProject;
	
	private IFolder srcFolder;
	private IFolder srcGenFolder;
	
	private IFile manifestFile;
	private IFile pluginXmlFile;
	
	public DEProjectSetupGenerator(DENameGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}
	
	public void generate() {
		ensureDialectProject();
		ensureSourceFolders();
		ensureManifest();
		ensurePluginXml();
	}
	
	private void ensureDialectProject() {
		try {
			String projectName = nameGenerator.getDialectProjectName();
			dialectJavaProject = EclipseGenerationUtil.createPluginProject(projectName, new NullProgressMonitor());
		} catch(Exception e) {
			throw new WrappedException(e);
		}
	}
	
	private void ensureSourceFolders() {
		srcFolder = EclipseGenerationUtil.ensureSourceFolder("src", dialectJavaProject);
		srcGenFolder = EclipseGenerationUtil.ensureSourceFolder("src-gen", dialectJavaProject);
	}
	
	private void ensureManifest() {
		String pluginId = nameGenerator.getDialectProjectName();
		manifestFile = EclipseGenerationUtil.ensureManifest(pluginId, dialectJavaProject);
		ManifestHandler manifestHandler = new ManifestHandler(manifestFile);
		
		final String deltaEcoreCorePluginId = "org.deltaecore.core";
		
		//Add dependency on Delta Ecore and re-export it
		if (!manifestHandler.requiresBundle(deltaEcoreCorePluginId)) {
			manifestHandler.addRequiredBundle(deltaEcoreCorePluginId + ";visibility:=reexport");
		}
		
		//TODO: Add dependency to domain plugin if it is a different project and the dependency does not already exist (re-export it).
		//FIXME: ManifestHandler cannot load manifest from plugins as it has wrong file path (that of a local project)
//		String domainPluginId = nameGenerator.getDomainPluginId();
//		if (!domainPluginId.equals(pluginId)) {
//			
//			if (!manifestHandler.requiresBundle(domainPluginId)) {
//				manifestHandler.addRequiredBundle(domainPluginId);
//			}
//		}

		//If a custom domain model element identifier resolver is specified, its package has to be exported if not already done so!
		String domainModelElementIdentifierResolverQualifiedPackageName = nameGenerator.getDomainModelElementIdentifierResolverQualifiedPackageName();
		
		if (domainModelElementIdentifierResolverQualifiedPackageName != null) {
			if (!manifestHandler.exportsPackage(domainModelElementIdentifierResolverQualifiedPackageName)) {
				manifestHandler.addExportedPackage(domainModelElementIdentifierResolverQualifiedPackageName);
			}
		}
		
		//TODO: Export created package
		
		manifestHandler.save();
	}
	
	private void ensurePluginXml() {
		pluginXmlFile = EclipseGenerationUtil.ensurePluginXml(dialectJavaProject);
		//TODO: build.properties?!
	}

	public IJavaProject getDialectJavaProject() {
		return dialectJavaProject;
	}

	public IFolder getSrcFolder() {
		return srcFolder;
	}

	public IFolder getSrcGenFolder() {
		return srcGenFolder;
	}

	public IFile getManifestFile() {
		return manifestFile;
	}

	public IFile getPluginXmlFile() {
		return pluginXmlFile;
	}
}
