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

public class DERegisterDynamicDeltaDialectHandler extends AbstractHandler {

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
		
		{
			String title = "Registered Dynamic Delta Dialect";
			String message = "Registering a dynamic delta dialect is a development tool that lets you use the delta dialect in the currently running Eclipse instance, e.g., during development to immediately apply delta operations in tentative delta modules.\n";
			message += "\n";
			message += "Things worth noting:\n";
			message += "1. Delta operations will be updated automatically and will be available in delta modules immediately.\n";
//			message += "2. Code for the interpreter will be generated automatically.\n";
			message += "2. Interpreters for the delta dialect are not functional so that cariant cannot be derived.\n";
			message += "3. Custom identifier resolvers are not supported.\n";
			message += "4. Dynamic delta dialects have to be registered with each start.\n";
			message += "5. IMPORTANT: This is NOT a production tool!\n";
			message += "\n";
			message += "You may unregister a dynamic delta dialect using the context menu.";
			
			MessageDialog.openInformation(parent, title, message);
		}
		
		DEDeltaDialectExtensionRegistry.registerDynamicDeltaDialect(deltaDialectFile);
		
		return null;
	}
}
