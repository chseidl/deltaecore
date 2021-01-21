package org.deltaecore.feature.graphical.configurator.components;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class DERunningConfigurationComposite extends Group {
	private Text textArea;

	public DERunningConfigurationComposite(Composite parent) {
		super(parent, SWT.NONE);
		assemble();
		setText("Running Configuration");
	}

	@Override
	protected void checkSubclass() {
	}

	private void assemble() {
		setLayout(new GridLayout(1, false));

		textArea = new Text(this, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		textArea.setEditable(false);
		textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void setConfiguration(DEConfiguration configuration) {
		String text = "";

		if (configuration != null) {
			text = DEConfigurationUtil.printConfiguration(configuration);
		}

		textArea.setText(text);
	}
}
