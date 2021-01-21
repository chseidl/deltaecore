package org.deltaecore.feature.eclipse.controls;

import org.deltaecore.common.eclipse.controls.DEVariantComposite;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.swt.SWTFactory;
import de.christophseidl.util.swt.SWTUtil;

public class DEVariantFolderDialog extends Dialog {

	protected boolean success;
	protected Shell shell;
	private DEVariantComposite variantFolderComposite;
	
	private IFolder variantFolder;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DEVariantFolderDialog(Shell parent, int style) {
		super(parent, style);
		setText("Derive Variant from Configuration");
		createContents();
	}

	public void initialize(IResource activeResource) {
		variantFolderComposite.initializeFromActiveResource(activeResource);
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open() {
		shell.open();
		shell.layout();
		SWTUtil.centerOverParent(shell);
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return success;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 140);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		variantFolderComposite = new DEVariantComposite(shell, SWT.NONE);
		variantFolderComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		SWTFactory.createDummyComposite(shell, true, true);
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		buttonComposite.setLayout(new GridLayout(2, false));

		Button okButton = SWTFactory.createDefaultButton(buttonComposite, "OK");
		Button cancelButton = SWTFactory.createDefaultButton(buttonComposite, "Cancel");
		
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				variantFolder = variantFolderComposite.getFolder();
				success = true;
				shell.close();
			}
		});
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				success = false;
				shell.close();
			}
		});
	}
	
	public IFolder getVariantFolder() {
		return variantFolder;
	}
}
