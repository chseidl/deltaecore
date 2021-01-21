package org.deltaecore.feature.graphical.configurator.components;

import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigration;
import org.deltaecore.feature.configuration.migration.util.DEConfigurationMigrationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class DEConfigurationMigrationComposite extends Group {
	private Text testArea;
	private Button calculateConfigurationMigrationButton;

	public DEConfigurationMigrationComposite(Composite parent) {
		super(parent, SWT.NONE);
		assemble();
		setText("Configuration Migration");
	}

	@Override
	protected void checkSubclass() {
	}

	private void assemble() {
		setLayout(new GridLayout(1, false));

		testArea = new Text(this, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		testArea.setEditable(false);
		testArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		calculateConfigurationMigrationButton = new Button(this, SWT.PUSH);
		calculateConfigurationMigrationButton.setText("Calculate Configuration Migration");
		calculateConfigurationMigrationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	public void setConfigurationMigration(DEConfigurationMigration configurationMigration) {
		String text = "";

		if (configurationMigration != null) {
			text = DEConfigurationMigrationUtil.printConfigurationMigration(configurationMigration);
		}

		testArea.setText(text);
	}

	public Button getCalculateConfigurationMigrationButton() {
		return calculateConfigurationMigrationButton;
	}
}
