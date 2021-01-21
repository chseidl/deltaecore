package org.deltaecore.core.extension;

import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;
import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEDynamicDeltaDialectExtension implements DEDeltaDialectExtension {
	private IFile deltaDialectFile;
	
	private DEDeltaDialect deltaDialect;
	private boolean deltaDialectInformationIsDirty;
	
	private IResourceChangeListener resourceChangeListener;
	
	public DEDynamicDeltaDialectExtension(IFile deltaDialectFile) {
		this.deltaDialectFile = deltaDialectFile;
		
		deltaDialectInformationIsDirty = true;
		resourceChangeListener = createResourceChangeListener();

		//Add listener
		IWorkspace workspace = ResourceUtil.getWorkspace();
		workspace.addResourceChangeListener(resourceChangeListener);
	}
	
	//TODO: Complete listener if file changes.
	protected IResourceChangeListener createResourceChangeListener() {
		return new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				int eventType = event.getType();
				
				if (eventType == IResourceChangeEvent.POST_CHANGE) {
					IResourceDelta rootDelta = event.getDelta();
					IPath path = deltaDialectFile.getFullPath();
					IResourceDelta dialectDelta = rootDelta.findMember(path);
					
					if (dialectDelta != null) {
						int kind = dialectDelta.getKind();
						
						if (kind == IResourceDelta.REMOVED) {
							DEDeltaDialectExtensionRegistry.unregisterDynamicDeltaDialect(deltaDialectFile);
//							System.out.println("dialect removed?");
							return;
						}
						
						if (kind == IResourceDelta.CHANGED) {
							//This appears on every key stroke but it is unproblematic as only the dirty flag is being set (actual work is delayed).
							deltaDialectInformationIsDirty = true;
//							System.out.println("dialect changed");
							return;
						}
					}
				}
			}
		};
	}
	protected void reload() {
		deltaDialect = EcoreIOUtil.loadModel(deltaDialectFile);
		
		System.out.println("reloaded");
		
		//TODO: Generate code
//		DECodeGenerator codeGenerator = new DECodeGenerator(deltaDialect);
//		codeGenerator.generateCode();
		
		deltaDialectInformationIsDirty = false;
	}
	
	protected void reloadIfNecessary() {
		if (deltaDialectInformationIsDirty) {
			reload();
		}
	}
	
	@Override
	public DEDeltaDialect getDialect() {
		reloadIfNecessary();
		
		return deltaDialect;
	}

	@Override
	public DEDeltaDialectInterpreter getInterpreter() {
		return new DEDeltaDialectInterpreter() {
			@Override
			public boolean interpretDeltaOperationCall(DEDeltaOperationCall deltaOperationCall, DEModelWriter modelWriter) {
				return true;
			}
		};
	}
	
//	@Override
//	public DEDeltaDialectInterpreter getInterpreter() {
//		DEDeltaDialect deltaDialect = getDialect();
//		
//		if (deltaDialect == null) {
//			return null;
//		}
//		
//		//TODO: Resolve dependency cycle due to plugin id constant.
//		//Use the same naming logic as the code generator to "guess" the right interpreter (unless someone messed with the setup, this should be correct).
////		DENameGenerator nameGenerator = new DENameGenerator(deltaDialect);
////		String deltaDialectInterpreterQualifiedClassName = nameGenerator.getDeltaDialectInterpreterQualifiedClassName();
////		
////		return (DEDeltaDialectInterpreter) instantiateClass(deltaDialectInterpreterQualifiedClassName);
//		return null;
//	}

	@Override
	public DEDomainModelElementIdentifierResolver getDomainModelElementIdentifierResolver() {
		return null;
	}
	
//	@Override
//	public DEDomainModelElementIdentifierResolver getDomainModelElementIdentifierResolver() {
//		DEDeltaDialect deltaDialect = getDialect();
//		
//		if (deltaDialect == null) {
//			return null;
//		}
//		
//		DEJavaClassReference domainModelElementIdentifierResolverClassReference = deltaDialect.getDomainModelElementIdentifierResolverClassReference();
//		String qualifiedDomainModelElementIdentifierResolverClassName = domainModelElementIdentifierResolverClassReference.getQualifiedNameOfReferencedJavaClass();
//		
//		return (DEDomainModelElementIdentifierResolver) instantiateClass(qualifiedDomainModelElementIdentifierResolverClassName);
//	}

	//TODO: This might be tricky as the class is in the current workspace and not loaded within a plugin.
	protected Class<?> loadClassFromDeltaDialectPlugin(String qualifiedClassName) {
		//TODO: Get a reference class from somewhere!
		Class<?> fromPlugin = null;
		
		try {
			ClassLoader classLoader = fromPlugin.getClassLoader();
			return classLoader.loadClass(qualifiedClassName);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected Object instantiateClass(String qualifiedClassName) {
		Class<?> c = loadClassFromDeltaDialectPlugin(qualifiedClassName);
		
		if (c == null) {
			return null;
		}
		
		try {
			return c.newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void dispose() {
		//Remove listener
		IWorkspace workspace = ResourceUtil.getWorkspace();
		workspace.removeResourceChangeListener(resourceChangeListener);
	}

	public IFile getDeltaDialectFile() {
		return deltaDialectFile;
	}
}
