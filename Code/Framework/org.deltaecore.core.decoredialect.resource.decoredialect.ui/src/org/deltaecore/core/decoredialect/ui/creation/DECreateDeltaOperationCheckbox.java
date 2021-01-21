package org.deltaecore.core.decoredialect.ui.creation;

import java.util.List;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEStandardDeltaOperationDefinition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DECreateDeltaOperationCheckbox extends Button {
	private DEDeltaDialect deltaDialect;
	private DEStandardDeltaOperationDefinition deltaOperationDefinition;
	private EClass target;
	
	public DECreateDeltaOperationCheckbox(Composite parent, DEDeltaDialect deltaDialect, DEStandardDeltaOperationDefinition deltaOperationDefinition, EClass target) {
		super(parent, SWT.CHECK | SWT.LEFT);
		
		this.deltaDialect = deltaDialect;
		this.deltaOperationDefinition = deltaOperationDefinition;
		this.target = target;
		
		GridData checkboxLayoutData = new GridData();
		checkboxLayoutData.horizontalAlignment = GridData.CENTER;
		setLayoutData(checkboxLayoutData);
	}

	@Override
	protected void checkSubclass() {
	}
	
	private void onSelect() {
		boolean selected = getSelection();
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		
		if (selected) {
			if (!deltaOperationDefinitions.contains(deltaOperationDefinition)) {
				deltaOperationDefinitions.add(deltaOperationDefinition);
			}
		} else {
			deltaOperationDefinitions.remove(deltaOperationDefinition);
		}
	}
	
	public EClass getTarget() {
		return target;
	}

	public DEStandardDeltaOperationDefinition getDeltaOperationDefinition() {
		return deltaOperationDefinition;
	}
	
	@Override
	public void setSelection(boolean selected) {
		super.setSelection(selected);
		onSelect();
	}
}
