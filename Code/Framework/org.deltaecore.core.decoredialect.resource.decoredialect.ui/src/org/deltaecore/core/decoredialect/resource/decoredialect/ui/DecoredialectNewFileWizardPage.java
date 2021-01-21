/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.ui;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.ui.creation.DEDEcoreDialectGeneralSettingsComposite;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import de.christophseidl.util.eclipse.ui.SelectionUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;
import de.christophseidl.util.ecore.EcoreResolverUtil;

//TODO: Cannot get to next page if epackage = null but can finish

//NOTE: Need to maintain file name due to code generation
public class DecoredialectNewFileWizardPage extends WizardPage {
	private final String fileExtension;
	private ISelection selection;

	private DEDeltaDialect deltaDialect;
	
	private DEDEcoreDialectGeneralSettingsComposite composite;
	
	public DecoredialectNewFileWizardPage(ISelection selection, String fileExtension, DEDeltaDialect deltaDialect) {
		super("generalSettings");
		
		this.selection = selection;
		this.fileExtension = fileExtension;
		this.deltaDialect = deltaDialect;
		
		setTitle("Create new decoredialect file");
		setDescription("This wizard creates a new file with *." + fileExtension + " extension that can be opened with the EMFText editor.");
	}

	@Override
	public void createControl(Composite parent) {
		composite = new DEDEcoreDialectGeneralSettingsComposite(parent, SWT.NULL);
		
		registerListeners();
		initialize();
		dialogChanged();
		setControl(composite);
	}

	private void registerListeners() {
		composite.getContainerText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		composite.getBrowseContainerButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
		composite.getFileNameText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		composite.getDomainMetaModelNamespaceText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		composite.getLoadMetaModelNamespaceButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onLoadMetaModelNamespace();
			}
		});
	}
	
	private void onLoadMetaModelNamespace() {
		//Get package from registry.
		String domainMetaModelNamespace = composite.getDomainMetaModelNamespaceText().getText();
		EPackage domainMetaModelPackage = EcoreResolverUtil.resolveEPackageFromRegistry(domainMetaModelNamespace);
		setDomainMetaModelPackage(domainMetaModelPackage);
	}
	
	private void setDomainMetaModelPackage(EPackage domainMetaModelPackage) {
		//Operation is expensive. Only execute when necessary
		EPackage previousDomainMetaModelPackage = deltaDialect.getDomainPackage();
		
		if (previousDomainMetaModelPackage != domainMetaModelPackage) {
			deltaDialect.setDomainPackage(domainMetaModelPackage);
			
			String domainMetaModelNamespace = domainMetaModelPackage != null ? domainMetaModelPackage.getNsURI() : "";
			composite.getDomainMetaModelNamespaceText().setText(domainMetaModelNamespace);
			
			updateStatus(null);
		}
	}
	
	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initialize() {
		String name = "new_file";
		
		if (selection != null && !selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() > 1) {
				return;
			}
			
			Object object = structuredSelection.getFirstElement();
			
			// test for IAdaptable
			if (!(object instanceof IResource)) {
				if (object instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) object;
					object = adaptable.getAdapter(IResource.class);
				}
			}
			
			if (object instanceof IResource) {
				IContainer container;
				
				if (object instanceof IContainer) {
					container = (IContainer) object;
				} else {
					IResource resource = (IResource) object;
					container = resource.getParent();
					
					// we use the name of the currently selected file instead of 'new_file'.
					name = resource.getFullPath().removeFileExtension().lastSegment();
				}
				
				IPath fullPath = container.getFullPath();
				composite.getContainerText().setText(fullPath.toString());
			}
		}
		
		composite.getFileNameText().setText(name + "." + fileExtension);

		
		
		
		
		//If anything resembling an Ecore meta model is in the selection, use it as the domain.
		IFile ecoreFile = SelectionUtil.getFirstIFileFromSelectionWithExtension("ecore", selection);
		
		if (ecoreFile != null) {
			EPackage ePackage = EcoreIOUtil.loadModel(ecoreFile);
			setDomainMetaModelPackage(ePackage);
		}
		
		
		// TODO: Not working - fix
//		 fileText.setSelection(0, name.length());
//		 fileText.forceFocus();
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for the container field.
	 */
	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, "Select new file container");
		
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				IPath path = (IPath) result[0];
				composite.getContainerText().setText(path.toString());
			}
		}
	}

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new org.eclipse.core.runtime.Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		
		if (!fileName.endsWith("." + fileExtension)) {
			updateStatus("File extension must be \"" + fileExtension + "\"");
			return;
		}
		
		//TODO: Add stuff for namespace
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return composite.getContainerText().getText();
	}

	public String getFileName() {
		return composite.getFileNameText().getText();
	}

	@Override
	public boolean canFlipToNextPage() {
		if (super.canFlipToNextPage()) {
			return (deltaDialect.getDomainPackage() != null);
		}
		
		return false;
	}
}
