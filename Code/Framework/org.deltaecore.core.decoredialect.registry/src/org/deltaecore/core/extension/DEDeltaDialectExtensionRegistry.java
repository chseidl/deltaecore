package org.deltaecore.core.extension;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.extension.resolution.DEDefaultDomainModelElementIdentifierResolver;
import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;
import org.deltaecore.debug.DEDebug;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EPackage;

import de.christophseidl.util.eclipse.ExtensionUtil;

//TODO: Make this dynamic aware

public class DEDeltaDialectExtensionRegistry {
	private static DEDeltaDialectExtensionRegistry instance = null;
	
	//NOTE: Do not use a map for lookup anymore to allow dynamically changing namespace URIs for dynamically registered delta dialects.
	private List<DEDeltaDialectExtension> deltaDialectExtensions;
	
	private static DEDomainModelElementIdentifierResolver defaultDomainModelElementIdentifierResolver;
	
	private DEDeltaDialectExtensionRegistry() {
		deltaDialectExtensions = new LinkedList<DEDeltaDialectExtension>();
		
		initializeExtensions();
		
//		IExtensionRegistry registry = Platform.getExtensionRegistry();
//		registry.addRegistryChangeListener(new IRegistryChangeListener() {
//			
//			@Override
//			public void registryChanged(IRegistryChangeEvent event) {
//				DEDebug.println("Registry changed: " + event);
//			}
//		});
//		
//		
//		IExtensionTracker extensionTracker = new ExtensionTracker(registry);
//		extensionTracker.registerHandler(new IExtensionChangeHandler() {
//			@Override
//			public void addExtension(IExtensionTracker tracker, IExtension extension) {
//				DEDebug.println("Extension added: " + extension);				
//			}
//			
//			@Override
//			public void removeExtension(IExtension extension, Object[] objects) {
//				DEDebug.println("Extension removed: " + extension);
//			}
//		}, null);
	}
	
	private static DEDeltaDialectExtensionRegistry getInstance() {
		if (instance == null) {
			instance = new DEDeltaDialectExtensionRegistry();
		}
		
		return instance;
	}

	private void initializeExtensions() {
		List<IExtension> rawExtensions = ExtensionUtil.getExtensions(DEDeltaDialectExtension.EXTENSION_POINT_ID);
		
		DEDebug.println("=================================================");
		DEDebug.println("Found " + rawExtensions.size() + " extension(s) to \"" + DEDeltaDialectExtension.EXTENSION_POINT_ID + "\".");
		
		for (IExtension rawExtension : rawExtensions) {
			DEStaticDeltaDialectExtension deltaDialectExtension = new DEStaticDeltaDialectExtension(rawExtension);
			registerStaticDeltaDialectExtension(deltaDialectExtension);
		}
		
		DEDebug.println("=================================================");
	}
	
//	private void initializeDeltaInterpreterExtensions() {
//		IExtensionRegistry registry = Platform.getExtensionRegistry();
//		IExtensionPoint point = registry.getExtensionPoint(DEDeltaDialectExtension.EXTENSION_POINT_ID);
//		
//		if (point == null) {
//			System.err.println("Extension point \"" + DEDeltaDialectExtension.EXTENSION_POINT_ID + "\" not found.");
//			return;
//		}
//		
//		IExtension[] rawExtensions = point.getExtensions();
//		
//		DEDebug.println("=================================================");
//		DEDebug.println("Found " + rawExtensions.length + " extension(s) to \"" + DEDeltaDialectExtension.EXTENSION_POINT_ID + "\".");
//		
//		for (IExtension rawExtension : rawExtensions) {
//			DEDeltaDialectExtension extension = new DEDeltaDialectExtension(rawExtension);
//			DEDeltaDialect dialect = extension.getDialect();
//			EPackage domainPackage = dialect.getDomainPackage();
//			String namespaceURI = domainPackage.getNsURI();
//			
//			DEDebug.println("Registered delta dialect extension for \"" + namespaceURI + "\".");
//			
//			deltaDialectExtensions.put(namespaceURI, extension);
//		}
//		DEDebug.println("=================================================");
//	}

	public static DEDeltaDialect getDeltaDialect(String namespaceURI) {
		DEDeltaDialectExtension extension = getExtension(namespaceURI);
		
		if (extension == null) {
			return null;
		}
		
		return extension.getDialect();
	}
	
	public static DEDeltaDialectInterpreter getDeltaDialectInterpreter(String namespaceURI) {
		DEDeltaDialectExtension extension = getExtension(namespaceURI);
		
		if (extension == null) {
			return null;
		}
		
		return extension.getInterpreter();
	}
	
	public static DEDomainModelElementIdentifierResolver getDomainModelElementIdentifierResolver(String namespaceURI) {
		DEDeltaDialectExtension extension = getExtension(namespaceURI);
		
		if (extension == null) {
			//If the extension is not registered altogether, there is also no point of
			//returning a default domain model element identifier resolver. 
			return null;
		}
		
		DEDomainModelElementIdentifierResolver domainModelElementIdentifierResolver = extension.getDomainModelElementIdentifierResolver();
		
		//If no specialized domain model element identifier resolver is registered, use the default version.
		if (domainModelElementIdentifierResolver == null) {
			return getDefaultDomainModelElementIdentifierResolver();
		}
		
		return domainModelElementIdentifierResolver;
	}
	
	private static DEDomainModelElementIdentifierResolver getDefaultDomainModelElementIdentifierResolver() {
		//Instantiate the default domain model element identifier resolver lazily.
		if (defaultDomainModelElementIdentifierResolver == null) {
			defaultDomainModelElementIdentifierResolver = new DEDefaultDomainModelElementIdentifierResolver();
		}
		
		return defaultDomainModelElementIdentifierResolver;
	}
	
	
	private static DEDeltaDialectExtension getExtension(String namespaceURI) {
		return getInstance().doGetExtension(namespaceURI);
	}
	
	private DEDeltaDialectExtension doGetExtension(String searchedNamespaceURI) {
		int oneAfterLastIndex = deltaDialectExtensions.size();
		ListIterator<DEDeltaDialectExtension> iterator = deltaDialectExtensions.listIterator(oneAfterLastIndex);
		
		//Iterate backwards to accept potential overrides
		while(iterator.hasPrevious()) {
			DEDeltaDialectExtension extension = iterator.previous();
			
			DEDeltaDialect dialect = extension.getDialect();
			EPackage domainPackage = dialect.getDomainPackage();
			
			if (domainPackage != null) {
				String namespaceURI = domainPackage.getNsURI();
				
				if (namespaceURI != null && namespaceURI.equals(searchedNamespaceURI)) {
					return extension;
				}
			}
		}
		
		return null;
	}
	
	
	//TODO: Some logic/notification regarding overriding existing dialects etc.
	public static void registerDynamicDeltaDialect(IFile deltaDialectFile) {
		DEDynamicDeltaDialectExtension dynamicDeltaDialectExtension = new DEDynamicDeltaDialectExtension(deltaDialectFile);
		getInstance().doRegisterDeltaDialectExtension(dynamicDeltaDialectExtension);
		
		DEDebug.println("Registered dynamic delta dialect extension for \"" + deltaDialectFile.getFullPath() + "\".");
	}
	
	private void registerStaticDeltaDialectExtension(DEStaticDeltaDialectExtension deltaDialectEclipseExtension) {
		doRegisterDeltaDialectExtension(deltaDialectEclipseExtension);
		
		DEDeltaDialect dialect = deltaDialectEclipseExtension.getDialect();
		EPackage domainPackage = dialect.getDomainPackage();
		String namespaceURI = domainPackage.getNsURI();
		
		DEDebug.println("Registered delta dialect extension for \"" + namespaceURI + "\".");
	}
	
	protected void doRegisterDeltaDialectExtension(DEDeltaDialectExtension deltaDialectExtension) {
		deltaDialectExtensions.add(deltaDialectExtension);
	}
	
	
	public static void unregisterDynamicDeltaDialect(IFile searchedDeltaDialectFile) {
		getInstance().doUnregisterDynamicDeltaDialect(searchedDeltaDialectFile);
	}
	
	private void doUnregisterDynamicDeltaDialect(IFile searchedDeltaDialectFile) {
		IPath searchedDeltaDialectFilePath = searchedDeltaDialectFile.getFullPath();
		
		int oneAfterLastIndex = deltaDialectExtensions.size();
		ListIterator<DEDeltaDialectExtension> iterator = deltaDialectExtensions.listIterator(oneAfterLastIndex);
		
		//Iterate backwards to accept potential overrides
		while(iterator.hasPrevious()) {
			DEDeltaDialectExtension extension = iterator.previous();
			
			if (extension instanceof DEDynamicDeltaDialectExtension) {
				DEDynamicDeltaDialectExtension dynamicExtension = (DEDynamicDeltaDialectExtension) extension;
				IFile deltaDialectFile = dynamicExtension.getDeltaDialectFile();
				IPath deltaDialectFilePath = deltaDialectFile.getFullPath();
				
				if (deltaDialectFilePath.equals(searchedDeltaDialectFilePath)) {
					doUnregisterDeltaDialectExtension(dynamicExtension);
					DEDebug.println("Unregistered dynamic delta dialect extension for \"" + deltaDialectFilePath + "\".");
					//Keep going, in case there were multiple dynamic extensions registered (which is a mistake but is only filtered by UI).
				}
			}
		}
	}
	
//	private static void unregisterEclipseDeltaDialectExtension(DEEclipseDeltaDialectExtension deltaDialectEclipseExtension) {
//		getInstance().doUnregisterDeltaDialectExtension(deltaDialectEclipseExtension);
//	}
	
	protected void doUnregisterDeltaDialectExtension(DEDeltaDialectExtension deltaDialectExtension) {
		deltaDialectExtensions.remove(deltaDialectExtension);
		deltaDialectExtension.dispose();
	}
}
