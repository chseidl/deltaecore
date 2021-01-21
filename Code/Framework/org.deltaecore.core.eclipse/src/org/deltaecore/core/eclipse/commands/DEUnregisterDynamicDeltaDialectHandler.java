package org.deltaecore.core.eclipse.commands;

import org.deltaecore.core.extension.DEDeltaDialectExtensionRegistry;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.eclipse.ui.SelectionUtil;

public class DEUnregisterDynamicDeltaDialectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile deltaDialectFile = SelectionUtil.getFirstActiveIFileWithExtension("decoredialect");
		
		Display display = Display.getCurrent();
		Shell parent = display.getActiveShell();
		
		if (deltaDialectFile == null) {
			String title = "Error";
			String message = "Need to select a *.decoredialect file.";
			
			MessageDialog.openError(parent, title, message);
			return null;
		}
		
		DEDeltaDialectExtensionRegistry.unregisterDynamicDeltaDialect(deltaDialectFile);
		
		return null;
	}
}
