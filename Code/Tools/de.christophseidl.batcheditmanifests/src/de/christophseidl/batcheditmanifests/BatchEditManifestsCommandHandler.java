package de.christophseidl.batcheditmanifests;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BatchEditManifestsCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();
		
		BatchEditManifestsDialog dialog = new BatchEditManifestsDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.center();
		dialog.open();
		
		BatchEditManifestsInformation information = dialog.getResult(); 
		
		if (information != null) {
			BatchEditManifestsJob job = new BatchEditManifestsJob(information);
			job.schedule();
		}
	    
		return null;
	}
}
