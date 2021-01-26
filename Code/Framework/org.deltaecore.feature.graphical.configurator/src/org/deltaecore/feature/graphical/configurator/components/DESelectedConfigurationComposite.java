package org.deltaecore.feature.graphical.configurator.components;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class DESelectedConfigurationComposite extends Group {
	private Text textArea;
	private Button completeButton;
	private Button useButton;
	private Button saveConfigurationButton;
	private Button generateVariantButton;
	
	public DESelectedConfigurationComposite(Composite parent) {
		super(parent, SWT.NONE);
		assemble();
		setText("Selected Configuration");
	}

	@Override
	protected void checkSubclass() {
	}

	private void assemble() {
		setLayout(new GridLayout(1, false));

		textArea = new Text(this, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		textArea.setEditable(false);
		textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		completeButton = new Button(this, SWT.PUSH);
		completeButton.setText("Automatically Select Versions");
		completeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		useButton = new Button(this, SWT.PUSH);
		useButton.setText("Use as Running Configuration");
		useButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		saveConfigurationButton = new Button(this, SWT.PUSH);
		saveConfigurationButton.setText("Save Configuration");
		saveConfigurationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		generateVariantButton = new Button(this, SWT.PUSH);
		generateVariantButton.setText("Generate Variant");
		generateVariantButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	public void setConfiguration(DEConfiguration configuration) {
		String text = "";

		if (configuration != null) {
			text = DEConfigurationUtil.printConfiguration(configuration);
		}

		textArea.setText(text);
	}
	
	public Button getCompleteButton() {
		return completeButton;
	}

	public Button getUseButton() {
		return useButton;
	}

	public Button getSaveConfigurationButton() {
		return saveConfigurationButton;
	}
	
	public Button getGenerateVariantButton() {
		return generateVariantButton;
	}
}
