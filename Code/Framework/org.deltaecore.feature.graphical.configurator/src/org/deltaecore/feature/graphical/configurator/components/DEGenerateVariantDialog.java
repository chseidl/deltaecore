package org.deltaecore.feature.graphical.configurator.components;

import org.deltaecore.common.eclipse.controls.DEVariantComposite;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.swt.SWTFactory;

public class DEGenerateVariantDialog extends Dialog {
	private DEVariantGeneratorsComposite generatorsComposite;
	private DEVariantComposite variantComposite;
	
	//TODO: Make resizeable
	
	public DEGenerateVariantDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = SWTFactory.createComposite(parent, 1);
		
		generatorsComposite = new DEVariantGeneratorsComposite(composite, SWT.NONE);
		generatorsComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		variantComposite = new DEVariantComposite(composite, SWT.NONE);
		variantComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		return composite;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Generate Variant");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	public DEVariantGeneratorsComposite getGeneratorsComposite() {
		return generatorsComposite;
	}

	public DEVariantComposite getVariantComposite() {
		return variantComposite;
	}
}
