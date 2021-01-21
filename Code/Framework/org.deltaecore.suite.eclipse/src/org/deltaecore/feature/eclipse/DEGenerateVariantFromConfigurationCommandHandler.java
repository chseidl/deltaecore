package org.deltaecore.feature.eclipse;

import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.util.DEConfigurationIOUtil;
import org.deltaecore.feature.eclipse.controls.DEVariantFolderDialog;
import org.deltaecore.feature.variant.DEConfigurationVariantGenerator;
import org.deltaecore.suite.variant.DEConfigurationDeltaModuleVariantGenerator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.christophseidl.util.eclipse.ui.JFaceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;
import de.christophseidl.util.ecore.EcoreResolverUtil;

public class DEGenerateVariantFromConfigurationCommandHandler extends AbstractHandler {
	private DEConfigurationVariantGenerator variantCreator;
	
	public DEGenerateVariantFromConfigurationCommandHandler() {
		variantCreator = new DEConfigurationDeltaModuleVariantGenerator();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile configurationModelFile = DEConfigurationIOUtil.getFirstActiveConfigurationFile();
		DEConfiguration configuration = EcoreIOUtil.loadModel(configurationModelFile);
		deriveVariantFromConfiguration(configuration);
		
		return null;
	}
	
	public void deriveVariantFromConfiguration(DEConfiguration configuration) {
		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();
		
		DEVariantFolderDialog dialog = new DEVariantFolderDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		IFile configurationModelFile = EcoreResolverUtil.resolveRelativeFileFromEObject(configuration);
		dialog.initialize(configurationModelFile);
		
		boolean success = dialog.open();
		
		if (success) {
			IFolder variantFolder = dialog.getVariantFolder();
			
			try {
				variantCreator.createAndSaveVariantFromConfiguration(configuration, variantFolder);
				JFaceUtil.alertInformation("Variant derivation completed successfully.");
			} catch (Exception e) {
				JFaceUtil.alertError("An error occurred: " + e.getMessage());
			}
		}
	}
}
