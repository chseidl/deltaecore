package org.deltaecore.feature.graphical.configurator.components;

import java.util.List;

import org.deltaecore.feature.variant.DEVariantGenerator;
import org.deltaecore.feature.variant.extension.DEVariantGeneratorExtension;
import org.deltaecore.feature.variant.extension.DEVariantGeneratorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class DEVariantGeneratorsComposite extends Composite {
	private List<DEVariantGeneratorExtension> variantGeneratorExtensions;
	
	private Combo generatorsCombo;
	private DEVariantGenerator variantGenerator;
	
	public DEVariantGeneratorsComposite(Composite parent, int style) {
		super(parent, style);
		
		variantGeneratorExtensions = DEVariantGeneratorRegistry.getVariantGeneratorExtensions();
		
		initGUI();
		registerListeners();
	}
	
	private void initGUI() {
		setLayout(new GridLayout());
		
		Group generatorGroup = new Group(this, SWT.NONE);
		generatorGroup.setText("Generator");
		generatorGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		generatorGroup.setLayout(new GridLayout());
		
		generatorsCombo = new Combo(generatorGroup, SWT.READ_ONLY);
		
		int numberOfRegisteredGenerators = variantGeneratorExtensions.size();
		String[] comboEntries = new String[numberOfRegisteredGenerators];
		int i = 0;
		
		for (DEVariantGeneratorExtension variantGeneratorExtension : variantGeneratorExtensions) {
			String name = variantGeneratorExtension.getName();
			comboEntries[i] = name;
			i++;
		}
		
		generatorsCombo.setItems(comboEntries);
		generatorsCombo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		if (numberOfRegisteredGenerators > 0) {
			generatorsCombo.select(0);
			updateSelectedVariantGenerator();
		}
	}
	
	private void registerListeners() {
		generatorsCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateSelectedVariantGenerator();
			}
		});
	}
	
	private void updateSelectedVariantGenerator() {
		int selectionIndex = generatorsCombo.getSelectionIndex();
		DEVariantGeneratorExtension variantGeneratorExtension = variantGeneratorExtensions.get(selectionIndex);
		variantGenerator = variantGeneratorExtension.getGenerator();
	}

	public DEVariantGenerator getVariantGenerator() {
		return variantGenerator;
	}
}
