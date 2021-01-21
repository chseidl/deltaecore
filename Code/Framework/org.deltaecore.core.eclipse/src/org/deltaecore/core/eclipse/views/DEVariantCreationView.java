package org.deltaecore.core.eclipse.views;

import org.deltaecore.core.eclipse.controls.DEVariantCreationComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;


public class DEVariantCreationView extends ViewPart {
	public static final String ID = "org.deltaecore.eclipse.views.DEVariantCreationView";

	private DEVariantCreationComposite variantCreationComposite;
	
	public DEVariantCreationView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		variantCreationComposite = new DEVariantCreationComposite(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		variantCreationComposite.setFocus();
	}
}