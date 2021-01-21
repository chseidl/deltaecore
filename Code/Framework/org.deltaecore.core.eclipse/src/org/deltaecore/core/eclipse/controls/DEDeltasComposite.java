package org.deltaecore.core.eclipse.controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.swt.SWTFactory;
import de.christophseidl.util.swt.controls.SelectMultipleFilesComposite;

public class DEDeltasComposite extends SelectMultipleFilesComposite {
	private Button autoSortButton;
	
	public DEDeltasComposite(Composite parent, int style) {
		super(parent, style, "Deltas");
		
		registerListeners();
	}

	private void registerListeners() {
		autoSortButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				onAutoSort();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	private void onAutoSort() {
		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();
		MessageDialog.openWarning(shell, "TODO", "Auto sort not implemented yet. Deltas will be sorted upon variant creation.");
	}

	@Override
	protected String createResourceDialogTitle() {
		return "Select Deltas";
	}
	
	protected void assembleAutoSortButton(Composite buttonBar) {
		autoSortButton = SWTFactory.createDefaultButton(buttonBar, "Auto Sort");
	}
	
	@Override
	protected void doAssembleButtonBar(Composite buttonBar) {	
		assembleAddButton(buttonBar);
		SWTFactory.createVerticalDummyComposite(buttonBar);
		assembleMoveUpButton(buttonBar);
		
		assembleAutoSortButton(buttonBar);
		
		assembleMoveDownButton(buttonBar);
		SWTFactory.createVerticalDummyComposite(buttonBar);
		assembleRemoveButton(buttonBar);
	}
	
	@Override
	protected String[] createFileChooserDialogFilterExtensions() {
		return new String[] {"*.decore", "*.*"};
	}
	
	@Override
	protected List<IFile> createInitiallySelectedFiles() {
		//TODO: TEMP for development
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		List<IFile> files = new ArrayList<IFile>();
		
//		IPath delta1Path = new Path("ExampleDeltaUsage/deltas/SFT_Delta1.decore");
//		IFile delta1File = workspaceRoot.getFile(delta1Path);
//		files.add(delta1File);

//		IPath delta2Path = new Path("ExampleDeltaUsage/deltas/SFT_Delta2.decore");
//		IFile delta2File = workspaceRoot.getFile(delta2Path);
//		files.add(delta2File);
		
		return files;
	}
	
	@Override
	protected void doUpdateButtonEnabledState(boolean isFileSelected) {
		super.doUpdateButtonEnabledState(isFileSelected);
		
		autoSortButton.setEnabled(false);
		//TODO: Re-enable this when functionality is implemented-
//		autoSortButton.setEnabled(isFileSelected);
	}
}
