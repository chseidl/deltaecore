package org.deltaecore.core.decoredialect.ui.creation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.christophseidl.util.swt.SWTFactory;

public class DEDEcoreDialectGeneralSettingsComposite extends Composite {
	private Text containerText;
	private Button browseContainerButton;
	private Text fileNameText;
	
	private Text domainMetaModelNamespaceText;
	private Button loadMetaModelNamespaceButton;
	private Button importMetaModelNamespaceButton;
	
	public DEDEcoreDialectGeneralSettingsComposite(Composite parent, int style) {
		super(parent, style);
		createUI();
	}

	@Override
	protected void checkSubclass() {
	}
	
	private void createUI() {
		setLayout(new GridLayout(1, false));
		assembleFileSettingsGroup(this);
		assembleDomainMetaModelGroup(this);
	}
	
	private void assembleFileSettingsGroup(Composite parent) {
		Group fileSettingsGroup = new Group(parent, SWT.NULL);
		fileSettingsGroup.setText("File Settings");
		
		fileSettingsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		fileSettingsGroup.setLayout(new GridLayout(3, false));
		
		assembleContainerEntry(fileSettingsGroup);
		assembleFileEntry(fileSettingsGroup);
	}
	
	private void assembleContainerEntry(Composite parent) {
		Label containerLabel = new Label(parent, SWT.NULL);
		containerLabel.setText("&Container:");

		containerText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		containerText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		browseContainerButton = SWTFactory.createDefaultButton(parent, "Browse...");
	}

	private void assembleFileEntry(Composite parent) {
		Label fileNameLabel = new Label(parent, SWT.NULL);
		fileNameLabel.setText("&File Name:");

		fileNameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void assembleDomainMetaModelGroup(Composite parent) {
		Group domainMetaModelGroup = new Group(this, SWT.NONE);
		domainMetaModelGroup.setText("Domain Meta Model");

		domainMetaModelGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		domainMetaModelGroup.setLayout(new GridLayout(3, false));
		
		domainMetaModelNamespaceText = new Text(domainMetaModelGroup, SWT.BORDER);
		domainMetaModelNamespaceText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		loadMetaModelNamespaceButton = SWTFactory.createDefaultButton(domainMetaModelGroup, "Load", 85);
		
		importMetaModelNamespaceButton = SWTFactory.createDefaultButton(domainMetaModelGroup, "Import...", 85);
		//TEMP: enable once this works
		importMetaModelNamespaceButton.setEnabled(false);
	}
	
	public Text getContainerText() {
		return containerText;
	}

	public Button getBrowseContainerButton() {
		return browseContainerButton;
	}

	public Text getFileNameText() {
		return fileNameText;
	}

	public Text getDomainMetaModelNamespaceText() {
		return domainMetaModelNamespaceText;
	}

	public Button getLoadMetaModelNamespaceButton() {
		return loadMetaModelNamespaceButton;
	}

	public Button getImportMetaModelNamespaceButton() {
		return importMetaModelNamespaceButton;
	}
}
