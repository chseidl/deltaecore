package org.deltaecore.core.variant.interpretation;

import org.deltaecore.core.decore.DEDeltaOperationCall;
import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;

//TODO: This should NOT be in this project but rather in org.deltaecore.core.variant instead.
//It is just a dirty workaround for problematic cross references - fix this!
public interface DEDeltaDialectInterpreter {
	public boolean interpretDeltaOperationCall(DEDeltaOperationCall deltaOperationCall, DEModelWriter modelWriter);
}
