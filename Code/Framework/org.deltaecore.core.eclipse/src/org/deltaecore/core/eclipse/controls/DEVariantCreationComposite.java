package org.deltaecore.core.eclipse.controls;

import java.util.List;

import org.deltaecore.common.eclipse.controls.DEVariantComposite;
import org.deltaecore.core.variant.DEVariantCreator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.java.StringUtil;
import de.christophseidl.util.swt.SWTFactory;

public class DEVariantCreationComposite extends Composite {
	private DEDeltasComposite deltasComposite;
	private DEVariantComposite variantComposite;
	
	private Composite buttonComposite;
	private Button createVariantButton;
	
	private DEVariantCreator variantCreator;
	
	public DEVariantCreationComposite(Composite parent, int style) {
		super(parent, style);
	
		variantCreator = new DEVariantCreator();
		
		initGUI();
		registerListeners();
	}
		
	
	private void initGUI() {
		setLayout(new GridLayout());
		
		{
			deltasComposite = new DEDeltasComposite(this, SWT.NONE);
			GridLayout deltasCompositeLayout = new GridLayout();
			deltasCompositeLayout.makeColumnsEqualWidth = true;
			GridData deltasCompositeLData = new GridData();
			deltasCompositeLData.horizontalAlignment = GridData.FILL;
			deltasCompositeLData.verticalAlignment = GridData.FILL;
			deltasCompositeLData.grabExcessHorizontalSpace = true;
			deltasCompositeLData.grabExcessVerticalSpace = true;
			deltasComposite.setLayoutData(deltasCompositeLData);
			deltasComposite.setLayout(deltasCompositeLayout);
		}
		{
			variantComposite = new DEVariantComposite(this, SWT.NONE);
			GridLayout variantsCompositeLayout = new GridLayout();
			variantsCompositeLayout.makeColumnsEqualWidth = true;
			GridData variantsCompositeLData = new GridData();
			variantsCompositeLData.horizontalAlignment = GridData.FILL;
			variantsCompositeLData.grabExcessHorizontalSpace = true;
			variantsCompositeLData.verticalAlignment = GridData.FILL;
			variantComposite.setLayoutData(variantsCompositeLData);
			variantComposite.setLayout(variantsCompositeLayout);
		}
		{
			buttonComposite = new Composite(this, SWT.NONE);
			GridLayout buttonCompositeLayout = new GridLayout();
			buttonCompositeLayout.makeColumnsEqualWidth = true;
			GridData buttonCompositeLData = new GridData();
			buttonCompositeLData.horizontalAlignment = GridData.FILL;
			buttonCompositeLData.grabExcessHorizontalSpace = true;
			buttonCompositeLData.verticalAlignment = GridData.FILL;
			buttonComposite.setLayoutData(buttonCompositeLData);
			buttonComposite.setLayout(buttonCompositeLayout);
			
			{
				createVariantButton = SWTFactory.createSpanningButton(buttonComposite, "Create Variant");
			}
		}
	}
	
	private void registerListeners() {
		createVariantButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCreateVariant();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	//TODO: This should be externalized - no real logic within UI please.
	protected void onCreateVariant() {
		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();
		
		try {
			List<IFile> deltaFiles = deltasComposite.getFiles();
			
			if (deltaFiles.isEmpty()) {
				MessageDialog.openError(shell, "Error", "No deltas specified");
				return;
			}
			
			IFolder variantFolder = getVariantFolder();
			IPath variantFolderPath = variantFolder.getFullPath();
			
			variantCreator.createAndSaveVariantFromDeltaFiles(deltaFiles, variantFolder);
			
			MessageDialog.openInformation(shell, "Variant Creation Completed", "Variant successfully created in \"" + variantFolderPath.toOSString() + "\".");
		} catch (Exception e) {
			//For debug. remove later as exceptions are handle appropriately here.
			e.printStackTrace();
			
			String message = e.getMessage();
			
			if (message == null || message.isEmpty()) {
				message = StringUtil.implode(e.getStackTrace(), "\n");
			}
			
			MessageDialog.openError(shell, "Error", e.getMessage());
		}
	}
	
	public List<IFile> getDeltas() {
		return deltasComposite.getFiles();
	}
	
	public IFolder getVariantFolder() {
		return variantComposite.getFolder();
	}

	@Override
	public boolean setFocus() {
		return deltasComposite.setFocus();
	}
}
