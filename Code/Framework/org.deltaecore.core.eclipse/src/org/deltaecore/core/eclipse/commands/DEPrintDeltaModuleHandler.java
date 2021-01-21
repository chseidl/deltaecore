package org.deltaecore.core.eclipse.commands;

import org.deltaecore.core.decore.resource.decore.util.DEDecorePrintUtil;
import org.deltaecore.debug.DEDebug;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;

import de.christophseidl.util.eclipse.ui.SelectionUtil;

//TODO: Have to make this only visible/available when one/multiple "decore_plain" file(s) are selected
//TODO: Have to implement this for multiple files
//TODO: Have to write to disk
public class DEPrintDeltaModuleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		SelectionUtil.getSelectedIFilesWithExtension("decore_plain");
		IFile file = SelectionUtil.getFirstSelectedIFileWithExtension("decore_plain");
		String decoreText = DEDecorePrintUtil.printDecorePlain(file);
		
		DEDebug.println();
		DEDebug.println("============== PRINTING ==============");
		DEDebug.println("==== (DEPrintDeltaModuleHandler) =====");
		DEDebug.println();
		DEDebug.println(decoreText);
		DEDebug.println();
		DEDebug.println("=====================================");
		DEDebug.println();
		
		return null;
	}
}
