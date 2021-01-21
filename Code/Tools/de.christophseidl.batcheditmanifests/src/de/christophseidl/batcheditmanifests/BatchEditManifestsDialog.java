package de.christophseidl.batcheditmanifests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import eu.vicci.ecosystem.util.eclipse.ui.DefaultControlsFactory;
import eu.vicci.ecosystem.util.eclipse.ui.SWTUtil;

public class BatchEditManifestsDialog extends Dialog {

	protected BatchEditManifestsInformation result;
	protected Shell shell;
	
	private Text projectNamePatternText;

	private Button bundleVersionCheckbox;
	private Text bundleVersionText;
	
	private Button bundleVendorCheckbox;
	private Text bundleVendorText;
	
	private Button bundleRequiredExecutionEnvironmentCheckbox;
	private Text bundleRequiredExecutionEnvironmentText;

	private Button okButton;
	private Button cancelButton;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BatchEditManifestsDialog(Shell parent, int style) {
		super(parent, style);
		setText("Batch Edit Manifests");
		
		createContents();
		registerListeners();
		
//		projectNamePatternText.setText("de.example.test?");
//		bundleVersionText.setText("1.0.0");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public void open() {
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 250);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Group affectedProjectsGroup = new Group(shell, SWT.NONE);
		affectedProjectsGroup.setLayout(new GridLayout(2, false));
		affectedProjectsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		affectedProjectsGroup.setText("Affected Projects");
		
		Label projectNamePatternLabel = DefaultControlsFactory.createLabel(affectedProjectsGroup, "Project Name Pattern:");
		projectNamePatternLabel.setToolTipText("May use wildcards ? (any character) and * (any character sequence).");
		
		projectNamePatternText = new Text(affectedProjectsGroup, SWT.BORDER);
		projectNamePatternText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group manifestGroup = new Group(shell, SWT.NONE);
		manifestGroup.setLayout(new GridLayout(2, false));
		manifestGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		manifestGroup.setText("Manifest");
		
		bundleVersionCheckbox = new Button(manifestGroup, SWT.CHECK);
		bundleVersionCheckbox.setText("Bundle-Version");
		
		bundleVersionText = new Text(manifestGroup, SWT.BORDER);
		bundleVersionText.setEnabled(false);
		bundleVersionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		bundleVendorCheckbox = new Button(manifestGroup, SWT.CHECK);
		bundleVendorCheckbox.setText("Bundle-Vendor");
		
		bundleVendorText = new Text(manifestGroup, SWT.BORDER);
		bundleVendorText.setEnabled(false);
		bundleVendorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		bundleRequiredExecutionEnvironmentCheckbox = new Button(manifestGroup, SWT.CHECK);
		bundleRequiredExecutionEnvironmentCheckbox.setText("Bundle-RequiredExecutionEnvironment");
		
		bundleRequiredExecutionEnvironmentText = new Text(manifestGroup, SWT.BORDER);
		bundleRequiredExecutionEnvironmentText.setEnabled(false);
		bundleRequiredExecutionEnvironmentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		DefaultControlsFactory.createDummyComposite(shell, true, true);
		
		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, true));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		
		okButton = DefaultControlsFactory.createDefaultButton(buttonComposite, "OK");
		cancelButton = DefaultControlsFactory.createDefaultButton(buttonComposite, "Cancel");
	}
	
	private void registerListeners() {
		bundleVersionCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isSelected = bundleVersionCheckbox.getSelection();
				bundleVersionText.setEnabled(isSelected);
			}
		});
		
		bundleVendorCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isSelected = bundleVendorCheckbox.getSelection();
				bundleVendorText.setEnabled(isSelected);
			}
		});
		
		bundleRequiredExecutionEnvironmentCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isSelected = bundleRequiredExecutionEnvironmentCheckbox.getSelection();
				bundleRequiredExecutionEnvironmentText.setEnabled(isSelected);
			}
		});
		
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createResult();
				
				//TODO: Check result
				//project name pattern plausible
				//at least one checked
				
				shell.close();
			}
		});
		
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
	
	private void createResult() {
		result = new BatchEditManifestsInformation();
		
		String projectNamePattern = projectNamePatternText.getText();
		result.setProjectNamePattern(projectNamePattern);
		
		if (bundleVersionCheckbox.getSelection()) {
			String bundleVersion = bundleVersionText.getText();
			result.setBundleVersion(bundleVersion);
		}
		
		if (bundleVendorCheckbox.getSelection()) {
			String bundleVendor = bundleVendorText.getText();
			result.setBundleVendor(bundleVendor);
		}
		
		if (bundleRequiredExecutionEnvironmentCheckbox.getSelection()) {
			String bundleRequiredExecutionEnvironment = bundleRequiredExecutionEnvironmentText.getText();
			result.setBundleRequiredExecutionEnvironment(bundleRequiredExecutionEnvironment);
		}
	}
	
	public void center() {
		SWTUtil.centerOverParent(shell);
	}

	public BatchEditManifestsInformation getResult() {
		return result;
	}
}
