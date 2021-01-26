package org.deltaecore.feature.graphical.configurator.editor;

import org.deltaecore.common.eclipse.controls.DEVariantComposite;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.configuration.DEConfiguration;
import org.deltaecore.feature.configuration.DEConfigurationFactory;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigration;
import org.deltaecore.feature.configuration.configurationmigration.DEConfigurationMigrationFactory;
import org.deltaecore.feature.configuration.migration.util.DEConfigurationMigrationCreator;
import org.deltaecore.feature.configuration.util.DEConfigurationChecker;
import org.deltaecore.feature.configuration.util.DEConfigurationUtil;
import org.deltaecore.feature.configure.DEVersionAutoConfigurer;
import org.deltaecore.feature.constraint.DEConstraintModel;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.deltaecore.feature.graphical.base.factories.DEEditPartFactory;
import org.deltaecore.feature.graphical.configurator.components.DEConfigurationMigrationComposite;
import org.deltaecore.feature.graphical.configurator.components.DEGenerateVariantDialog;
import org.deltaecore.feature.graphical.configurator.components.DERunningConfigurationComposite;
import org.deltaecore.feature.graphical.configurator.components.DESelectedConfigurationComposite;
import org.deltaecore.feature.graphical.configurator.components.DEVariantGeneratorsComposite;
import org.deltaecore.feature.graphical.configurator.factories.DEConfiguratorEditPartFactory;
import org.deltaecore.feature.variant.DEVariantGenerator;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DEConfiguratorGraphicalEditor extends DEGraphicalEditor {
	private DEConfiguration runningConfiguration;
	private DEConfiguration selectedConfiguration;
	private DEConfigurationMigration configurationMigration;
	
	private Button validateConfigurationButton;
	private Button numberOfPossibleConfigurationsButton;
	private DERunningConfigurationComposite runningConfigurationComposite;
	private DESelectedConfigurationComposite selectedConfigurationComposite;
	private DEConfigurationMigrationComposite configurationMigrationComposite;
	private Button exportImageButton;
	
	public DEConfiguratorGraphicalEditor() {
		// Keep these instances stable so that listeners on them keep working
		runningConfiguration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		selectedConfiguration = DEConfigurationFactory.eINSTANCE.createDEConfiguration();
		configurationMigration = DEConfigurationMigrationFactory.eINSTANCE.createDEConfigurationMigration();
	}

	@Override
	protected void setDataModel(DEFeatureModel featureModel) {
		super.setDataModel(featureModel);
		
		runningConfiguration.setFeatureModel(featureModel);
		selectedConfiguration.setFeatureModel(featureModel);
	}
	
	@Override
	protected DEEditPartFactory createEditPartFactory() {
		return new DEConfiguratorEditPartFactory(this);
	}
	
	@Override
	protected DEGraphicalViewer doCreateGraphicalViewer() {
		return new DEConfiguratorGraphicalViewer(this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		
		// Left sash
		// Create the regular editor composite
		super.createPartControl(sashForm);
		
		// Right sash
		createConfigurationPanel(sashForm);
		
		sashForm.setWeights(new int[] { 4, 1 });
		registerLocalListeners();
	}
	
	@Override
	protected ContextMenuProvider createContextMenuProvider(GraphicalViewer viewer, ActionRegistry actionRegistry) {
		return new ContextMenuProvider(viewer) {
			@Override
			public void buildContextMenu(IMenuManager menu) {
				//Don't need a context menu.
			}
		};
	}
	
	private Composite createConfigurationPanel(Composite parent) {
		Composite configurationPanel = new Composite(parent, SWT.NONE);
		configurationPanel.setLayout(new GridLayout(1, false));

		validateConfigurationButton = new Button(configurationPanel, SWT.PUSH);
		validateConfigurationButton.setText("Validate Configuration");
		validateConfigurationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		numberOfPossibleConfigurationsButton = new Button(configurationPanel, SWT.PUSH);
		numberOfPossibleConfigurationsButton.setText("Number of Possible Configurations");
		numberOfPossibleConfigurationsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		numberOfPossibleConfigurationsButton.setEnabled(false);
		
		
		runningConfigurationComposite = new DERunningConfigurationComposite(configurationPanel);
		runningConfigurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		selectedConfigurationComposite = new DESelectedConfigurationComposite(configurationPanel);
		selectedConfigurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		configurationMigrationComposite = new DEConfigurationMigrationComposite(configurationPanel);
		configurationMigrationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		exportImageButton = new Button(configurationPanel, SWT.PUSH);
		exportImageButton.setText("Export Image");
		exportImageButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		return configurationPanel;
	}
	
	@Override
	protected void registerListeners() {
		//See issue below why this is not used here!
	}
	
	//NOTE: This is a workaround for the following issue (as I don't have time for architectural changes right now):
	//Super class define _protected_ registerListeners() method, which is called after creation of super classes controls.
	//As this class wraps the super classes control (i.e., creates it before creating this class' controls), registering listeners
	//for local controls via overriding the protected method faces null pointer exceptions.
	private void registerLocalListeners() {
		runningConfiguration.eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				
				runningConfigurationComposite.setConfiguration(runningConfiguration);
				refreshVisuals();
			}
		});

		selectedConfiguration.eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				
				selectedConfigurationComposite.setConfiguration(selectedConfiguration);
				refreshVisuals();
			}
		});
		
		validateConfigurationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateConfiguration();
			}
		});
		
		numberOfPossibleConfigurationsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				calculateNumberOfPossibleConfigurations();
			}
		});
		
		selectedConfigurationComposite.getCompleteButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				autoCompleteVersions();
			}
		});
		
		selectedConfigurationComposite.getUseButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useSelectedConfigurationAsRunningConfiguration();
			}
		});

		selectedConfigurationComposite.getGenerateVariantButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateVariant();
			}
		});
		
		// TODO: apply ConfigurationMigration some time
		configurationMigrationComposite.getCalculateConfigurationMigrationButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DEConfigurationMigration configurationMigration = DEConfigurationMigrationCreator.determineConfigurationMigration(runningConfiguration, selectedConfiguration);
				configurationMigrationComposite.setConfigurationMigration(configurationMigration);
			}
		});
		

		exportImageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getViewer().exportImage();
			}
		});
	}
	
	private void autoCompleteVersions() {
		try {
			DEConstraintModel constraintModel = getConstraintModel();
			DEConfiguration completedConfiguration = DEVersionAutoConfigurer.autoCompleteVersionsInConfiguration(selectedConfiguration, constraintModel);
			
			DEConfigurationUtil.transferAndReplaceConfigurationArtifacts(completedConfiguration, selectedConfiguration);
		} catch(Exception e) {
//			MessageDialog.openError(parent, title, message);
			e.printStackTrace();
		}
	}
	
	private void generateVariant() {
		final String failureMessageTitle = "Variant Generation Failed!";
		
		Shell shell = getShell();
		
		DEConfiguration selectedConfiguration = getSelectedConfiguration();
		
		//Validate selected configuration
		ValidateConfigurationResult validateConfigurationResult = doValidateConfiguration();
		
		if (!validateConfigurationResult.getIsValid()) {
			MessageDialog.openError(shell, failureMessageTitle, "The configuration is invalid: " + validateConfigurationResult.getErrorMessage());
			return;
		}
		
		try {
			
			DEGenerateVariantDialog dialog = new DEGenerateVariantDialog(shell);
			int result = dialog.open();
			
			if (result == Dialog.OK) {
				DEVariantGeneratorsComposite generatorsComposite = dialog.getGeneratorsComposite();
				DEVariantGenerator variantGenerator = generatorsComposite.getVariantGenerator();
				
				if (variantGenerator == null) {
					MessageDialog.openError(shell, failureMessageTitle, "No variant generator selected!");
					return;
				}
				
				DEVariantComposite variantComposite = dialog.getVariantComposite();
				IFolder variantFolder = variantComposite.getFolder();
				
				if (variantFolder == null) {
					MessageDialog.openError(shell, failureMessageTitle, "No variant folder selected!");
					return;
				}
				
				variantGenerator.createAndSaveVariantFromConfiguration(selectedConfiguration, variantFolder);
				
				IPath variantFolderPath = variantFolder.getFullPath();
				MessageDialog.openInformation(shell, "Variant Generation Completed", "Variant successfully generated in \"" + variantFolderPath.toOSString() + "\".");
			}
			
		} catch(Exception e) {
			//Debug
			e.printStackTrace();
			
			String message = "Variant could not be generated:\n\n" + e.getMessage();
			MessageDialog.openError(shell, failureMessageTitle, message);
		}
	}
	
	private void validateConfiguration() {
		DEFeatureModel featureModel = getFeatureModel();
		Diagnostician.INSTANCE.validate(featureModel);
		
		//Validate selected configuration
		ValidateConfigurationResult validateConfigurationResult = doValidateConfiguration();
		
		Shell shell = getShell();
		String title = "Validate Configuration";
		
		if (validateConfigurationResult.getIsValid()) {
			MessageDialog.openInformation(shell, title, "The configuration is valid.");
		} else {
			MessageDialog.openError(shell, title, "The configuration is invalid: " + validateConfigurationResult.getErrorMessage());
		}
	}

	//TEMP
	private class ValidateConfigurationResult {
		private boolean isValid;
		private String errorMessage;
		
		public ValidateConfigurationResult() {
			isValid = true;
			errorMessage = null;
		}
		
		public ValidateConfigurationResult(String errorMessage) {
			isValid = false;
			this.errorMessage = errorMessage;
		}

		public boolean getIsValid() {
			return isValid;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}
	
	private ValidateConfigurationResult doValidateConfiguration() {
		DEConfigurationChecker configurationChecker = new DEConfigurationChecker();
		boolean isValid = configurationChecker.isConfigurationValid(selectedConfiguration);
		
		if (isValid) {
			return new ValidateConfigurationResult();
		} else {
			String errorMessage = configurationChecker.getErrorMessage();
			return new ValidateConfigurationResult(errorMessage);
		}
	}
	
	
	private void calculateNumberOfPossibleConfigurations() {
//		DEFeatureModel featureModel = editorState.getFeatureModel();
//		int numberOfPossibleConfigurations = DEFeatureUtil.calculateNumberOfPossibleConfigurations(featureModel);
//		
//		Shell shell = getControl().getShell();
//		String title = "Number of possible configurations";
//		String message = "Number of possible configurations: " + numberOfPossibleConfigurations + " (not considering constraints)";
//		MessageDialog.openInformation(shell, title, message);
		
		
		Shell shell = getShell();
		MessageDialog.openInformation(shell, "TODO", "Not implemented yet");
	}
	
	private Shell getShell() {
		return getViewer().getControl().getShell();
	}
	
	public DEConfiguration getRunningConfiguration() {
		return runningConfiguration;
	}

	public DEConfiguration getSelectedConfiguration() {
		return selectedConfiguration;
	}

	public DEConfigurationMigration getConfigurationMigration() {
		return configurationMigration;
	}

	public void useSelectedConfigurationAsRunningConfiguration() {
		// Keep the configuration instances stable so that listeners on them keep working
		DEConfigurationUtil.transferAndReplaceConfigurationArtifacts(selectedConfiguration, runningConfiguration);
	}
}
