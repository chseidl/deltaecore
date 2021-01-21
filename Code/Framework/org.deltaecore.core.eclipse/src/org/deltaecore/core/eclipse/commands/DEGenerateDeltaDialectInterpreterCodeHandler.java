package org.deltaecore.core.eclipse.commands;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.util.DEDEcoreDialectIOUtil;
import org.deltaecore.core.generation.DECodeGenerator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.eclipse.ui.JFaceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;

public class DEGenerateDeltaDialectInterpreterCodeHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile deltaDialectFile = DEDEcoreDialectIOUtil.getFirstActiveDeltaDialectFile();
		
		if (deltaDialectFile == null || !deltaDialectFile.exists()) {
			JFaceUtil.alertError("The delta dialect does not exist.");
			return null;
		}
		
		DEDeltaDialect dialect = EcoreIOUtil.loadModel(deltaDialectFile);
		DECodeGenerator generator = new DECodeGenerator(dialect);
		generator.generate();

		JFaceUtil.alertInformation("Successfully generated delta dialect interpreter code.");
		
		return null;
	}
}
