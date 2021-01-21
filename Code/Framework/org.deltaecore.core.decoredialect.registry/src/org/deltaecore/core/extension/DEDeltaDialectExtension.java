package org.deltaecore.core.extension;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.variant.interpretation.DEDeltaDialectInterpreter;
import org.deltaecore.extension.resolution.DEDomainModelElementIdentifierResolver;

public interface DEDeltaDialectExtension {
	public static final String EXTENSION_POINT_ID = "org.deltaecore.deltadialect";
	
	public DEDeltaDialect getDialect();
	public DEDeltaDialectInterpreter getInterpreter();
	public DEDomainModelElementIdentifierResolver getDomainModelElementIdentifierResolver();
	public void dispose();
}
