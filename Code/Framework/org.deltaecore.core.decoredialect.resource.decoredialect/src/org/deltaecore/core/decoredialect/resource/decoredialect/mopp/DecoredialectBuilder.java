package org.deltaecore.core.decoredialect.resource.decoredialect.mopp;

import org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;

public class DecoredialectBuilder implements IDecoredialectBuilder {

	@Override
	public boolean isBuildingNeeded(URI uri) {
		// change this to return true to enable building of all resources
		return true;
	}

	@Override
	public IStatus build(DecoredialectResource resource, IProgressMonitor monitor) {
//		List<EObject> contents = resource.getContents();
//		if (!contents.isEmpty()) {
//			EObject content = contents.get(0);
//			
//			if (content instanceof DEDeltaDialect) {
//				DEDeltaDialect dialect = (DEDeltaDialect) content;
//				DECodeGenerator generator = new DECodeGenerator(dialect);
//				generator.generate();
//			}
//		}
		
		return Status.OK_STATUS;
	}

	@Override
	public IStatus handleDeletion(URI uri, IProgressMonitor monitor) {
		return Status.OK_STATUS;
	}

}
