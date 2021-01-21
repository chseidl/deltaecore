package org.deltaecore.core.generation.interpreter;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.generation.general.DENameGenerator;

public class DEDeltaDialectInterpreterCreatorSetup {
	private DENameGenerator nameGenerator;
	private DEDeltaDialect dialect;
	
	public DEDeltaDialectInterpreterCreatorSetup(DENameGenerator nameGenerator, DEDeltaDialect dialect) {
		this.nameGenerator = nameGenerator;
		this.dialect = dialect;
	}

	public DENameGenerator getNameGenerator() {
		return nameGenerator;
	}

	public DEDeltaDialect getDialect() {
		return dialect;
	}
}
