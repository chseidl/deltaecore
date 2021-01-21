package org.deltaecore.core.generation.general;

import java.util.List;

import org.deltaecore.core.decorebase.DEJavaClassReference;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import de.christophseidl.util.eclipse.resourcehandlers.ManifestHandler;
import de.christophseidl.util.ecore.EcoreResolverUtil;
import de.christophseidl.util.java.StringUtil;

public class DENameGenerator {
	private DEDeltaDialect dialect;
	
	private String upperPrefix;
	private String domainPackageName;
	
	public DENameGenerator(DEDeltaDialect dialect) {
		this.dialect = dialect;
		
		initialize();
	}
	
	private void initialize() {
		EPackage domainPackage = dialect.getDomainPackage();
		
		//TODO: Check whether the name may contain dots or other characters illegal for simple Java identifiers.
		String name = domainPackage.getName();
//		String prefix = domainPackage.getNsPrefix();
		
		upperPrefix = StringUtil.firstToUpper(name);
		domainPackageName = createDomainPackageName();
	}
	
	private URI getDialectResourceURI() {
		Resource dialectResource = dialect.eResource();
		return dialectResource.getURI();
	}
	
	//TODO: Don't need a IProject here, the root IFolder of the project would do.
	private IProject getDomainProject() {
		EPackage domainPackage = dialect.getDomainPackage();
		return EcoreResolverUtil.resolveProjectFromEPackage(domainPackage);
	}
	
	public String getDomainPluginId() {
		IProject domainProject = getDomainProject();
		ManifestHandler manifestHandler = new ManifestHandler(domainProject);
		
		String pluginId = manifestHandler.getPluginId();
		
		return pluginId;
	}
	
	
	private String createDomainPackageName() {
		//E.g. "eu.vicci.ecosystem.sft";
		
		//By convention, the Java interface of generated classes
		//is located in the root package of the generated code.
		//Use this to retrieve the base package of the domain.
		EPackage domainPackage = dialect.getDomainPackage();
		String domainPackageName = getDomainPackageNameForEPackage(domainPackage);
		
		if (domainPackageName != null) {
			return domainPackageName;
		}
		
		throw new UnsupportedOperationException("Unable to determine domain Java package from domain Ecore package.");
	}
	
	private String getDomainPackageNameForEPackage(EPackage ePackage) {
		//Try to find the base package name from a registered gen model.
		String basePackage = EcoreResolverUtil.resolveBasePackageFromRegisteredGenModel(ePackage);
		
		if (basePackage != null) {
			String packageName = ePackage.getName();
			return basePackage + "." + packageName;
		}
		
		//NOTE: There is a SLIGHT chance that this will create invalid results. If the first declared EClass uses
		//a custom interface that is located in a package different from the base package of the notation, that package
		//would be resolved - which is a mistake. However, for practical purposes, this will do for now.
		List<EClassifier> eClassifiers = ePackage.getEClassifiers();
		
		for (EClassifier eClassifier : eClassifiers) {
			if (eClassifier instanceof EClass) {
				EClass eClass = (EClass) eClassifier;
				
				Class<?> c = eClass.getInstanceClass();
				
				//If the model is not aware of its generated code, then the returned
				//value will be null regardless of whether the code exists or not.
				if (c != null) {
					Package p = c.getPackage();
					
					String packageName = p.getName();
					return packageName;
				}
			}
		}
		
		//Failed to resolve from eClassifiers. See if there are some more packages to try.
		//This WILL produce invalid results but at least it gives us something to work with.
		//In the worst case, the created classes are located in a package with one too many
		//nesting levels. 
		List<EPackage> eSubPackages = ePackage.getESubpackages();
		
		for (EPackage eSubPackage : eSubPackages) {
			//The result found here might not be entirely accurate but better than none.
			String intermediateResult = getDomainPackageNameForEPackage(eSubPackage);
			
			if (intermediateResult != null) {
				return intermediateResult;
			}
		}
		
		//No idea what to do further...
		return null;
	}
	
	public String getDialectProjectName() {
		//Project already exists (and is not generated), use its name.
		URI dialectResourceURI = getDialectResourceURI();
		IProject project = EcoreResolverUtil.resolveProjectFromResourceURI(dialectResourceURI);
		
		return project.getName();
	}
	
	public String getDeltaDialectBasePackage() {
		return domainPackageName + ".delta";
	}
	
	public String getDialectRelativeFilename() {
		//E.g., "model/SFT.decoredialect";
		URI dialectResourceURI = getDialectResourceURI();
		
		IProject project = EcoreResolverUtil.resolveProjectFromResourceURI(dialectResourceURI);
		IPath projectPath = project.getFullPath();

		IFile dialectResourceFile = EcoreResolverUtil.resolveRelativeFileFromResourceURI(dialectResourceURI);
		IPath workspaceRelativeDialectResourcePath = dialectResourceFile.getFullPath();
		
		IPath projectRelativeDialectResourcePath = workspaceRelativeDialectResourcePath.makeRelativeTo(projectPath);
		return projectRelativeDialectResourcePath.toString();
	}
	
	public String getAbstractDeltaDialectInterpreterSimpleClassName() {
		//E.g., "SFTAbstractDeltaDialectInterpreter";
		return upperPrefix + "AbstractDeltaDialectInterpreter";
	}
	
	public String getAbstractDeltaDialectInterpreterQualifiedClassName() {
		return getDeltaDialectBasePackage() + "." + getAbstractDeltaDialectInterpreterSimpleClassName();
	}
	
	public String getDeltaDialectInterpreterSimpleClassName() {
		//E.g., "SFTDeltaDialectInterpreter";
		return upperPrefix + "DeltaDialectInterpreter";
	}
	
	public String getDeltaDialectInterpreterQualifiedClassName() {
		return getDeltaDialectBasePackage() + "." + getDeltaDialectInterpreterSimpleClassName();
	}
	
	public String getDomainModelElementIdentifierResolverQualifiedClassName() {
		DEJavaClassReference domainModelElementIdentifierResolverClassReference = dialect.getDomainModelElementIdentifierResolverClassReference();
		
		if (domainModelElementIdentifierResolverClassReference == null) {
			//This is a valid case as the element is optional.
			return null;
		}
		
		return domainModelElementIdentifierResolverClassReference.getQualifiedNameOfReferencedJavaClass();
	}
	
	public String getDomainModelElementIdentifierResolverQualifiedPackageName() {
		String qualifiedClassName = getDomainModelElementIdentifierResolverQualifiedClassName();
		
		if (qualifiedClassName == null) {
			return null;
		}
		
		int lastDotIndex = qualifiedClassName.lastIndexOf(".");
		
		if (lastDotIndex == -1) {
			//Default package has no string representation.
			return null;
		}
		
		return qualifiedClassName.substring(0, lastDotIndex);
	}
	
	public void generate() {
		//Dummy. Only here for the sake of convention.
	}
}
