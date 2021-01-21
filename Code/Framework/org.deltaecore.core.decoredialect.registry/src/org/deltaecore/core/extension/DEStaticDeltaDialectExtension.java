package org.deltaecore.core.extension;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEStaticDeltaDialectExtension implements DEDeltaDialectExtension {
	private DEDeltaDialect dialect;
	private DEDeltaDialectInterpreter interpreter;
	private DEDomainModelElementIdentifierResolver domainModelElementIdentifierResolver;
	
	public DEStaticDeltaDialectExtension(DEDeltaDialect dialect, DEDeltaDialectInterpreter interpreter) {
		this(dialect, interpreter, null);
	}
	
	public DEStaticDeltaDialectExtension(DEDeltaDialect dialect, DEDeltaDialectInterpreter interpreter, DEDomainModelElementIdentifierResolver domainModelElementIdentifierResolver) {
		this.dialect = dialect;
		this.interpreter = interpreter;
		this.domainModelElementIdentifierResolver = domainModelElementIdentifierResolver;
	}

	public DEStaticDeltaDialectExtension(IExtension rawExtension) {
		loadRawExtension(rawExtension);
	}
	
	@SuppressWarnings("deprecation")
	private void loadRawExtension(IExtension rawExtension) {
		IConfigurationElement[] configurationElements = rawExtension.getConfigurationElements();
		String pluginId = rawExtension.getNamespaceIdentifier();
		
		try {
			for (IConfigurationElement configurationElement : configurationElements) {
				String configurationElementName = configurationElement.getName();
				
				if (configurationElementName.equals("dialect")) {
					String relativeModelFilename = configurationElement.getAttribute("model");
					dialect = EcoreIOUtil.loadModelFromPlugin(pluginId, relativeModelFilename, DEDeltaDialect.class);
					
//					try {
//						URL url = FileLocator.find(Platform.getBundle(pluginId), new Path(relativeModelFilename), null);
//						url = FileLocator.resolve(url);
//						File file = new File(url.toURI());
//						dialect = EcoreIOUtil.loadModel(file, DEDeltaDialect.class);
//					} catch(Exception e) {
//						e.printStackTrace();
//					}
					
					Object deltaInterpreterObject = configurationElement.createExecutableExtension("interpreter");
					
					if (deltaInterpreterObject instanceof DEDeltaDialectInterpreter) {
						interpreter = (DEDeltaDialectInterpreter) deltaInterpreterObject;
					}
					
					
					String domainModelElementResolverQualifiedClassName = configurationElement.getAttribute("domainModelElementIdentifierResolver");
					
					if (domainModelElementResolverQualifiedClassName != null && !domainModelElementResolverQualifiedClassName.isEmpty()) {
						Object domainModelElementIdentifierResolverObject = configurationElement.createExecutableExtension("domainModelElementIdentifierResolver");
						
						if (domainModelElementIdentifierResolverObject instanceof DEDomainModelElementIdentifierResolver) {
							domainModelElementIdentifierResolver = (DEDomainModelElementIdentifierResolver) domainModelElementIdentifierResolverObject;
						}
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public DEDeltaDialect getDialect() {
		return dialect;
	}

	@Override
	public DEDeltaDialectInterpreter getInterpreter() {
		return interpreter;
	}

	@Override
	public DEDomainModelElementIdentifierResolver getDomainModelElementIdentifierResolver() {
		return domainModelElementIdentifierResolver;
	}

	@Override
	public void dispose() {
	}
}
