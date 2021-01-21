/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.decoredialect.DEDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEStandardDeltaOperationDefinition;
import org.deltaecore.core.decoredialect.DEcoreDialectFactory;
import org.deltaecore.core.decoredialect.resource.decoredialect.IDecoredialectMetaInformation;
import org.deltaecore.core.decoredialect.resource.decoredialect.mopp.DecoredialectMetaInformation;
import org.deltaecore.core.decoredialect.util.DEDEcoreDialectUtil;
import org.deltaecore.core.generation.dialect.DEDeltaDialectCreatorUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import de.christophseidl.util.ecore.EcoreIOUtil;

public class DecoredialectNewFileWizard extends Wizard implements INewWizard {
	private DecoredialectNewFileWizardPage generalSettingsPage;
	
//	private String categoryId = null;
	private ISelection selection;

	private DEDeltaDialect deltaDialect;
	
	public DecoredialectNewFileWizard() {
		setNeedsProgressMonitor(true);
		
		setWindowTitle("New Delta Ecore Dialect");
		
		deltaDialect = DEcoreDialectFactory.eINSTANCE.createDEDeltaDialect();
	}

//	public String getCategoryId() {
//		return categoryId;
//	}
//
//	public void setCategoryId(String id) {
//		categoryId = id;
//	}

	@Override
	public void addPages() {
		//Some here need UI knowledge that might not be present during constructor call.
		generalSettingsPage = new DecoredialectNewFileWizardPage(selection, getFileExtension(), deltaDialect);
		
		addPage(generalSettingsPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using the wizard as execution
	 * context.
	 */
	@Override
	public boolean performFinish() {
		final String containerName = generalSettingsPage.getContainerName();
		final String fileName = generalSettingsPage.getFileName();
		
		String newName = fileName;
		
		int seperatorIdx = newName.indexOf('.');
		if (seperatorIdx != -1) {
			newName = newName.substring(0, seperatorIdx);
		}
		final IFile file;
		
		try {
			file = getFile(fileName, containerName);
		} catch (CoreException e1) {
			DecoredialectUIPlugin.logError("Exception while initializing new file", e1);
			return false;
		}

		if (file.exists()) {
			// ask for confirmation
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			messageBox.setMessage("File \"" + fileName + "\" already exists. Do you want to override it?");
			messageBox.setText("File exists");
			
			int response = messageBox.open();
			
			if (response == SWT.NO) {
				return true;
			}
		}

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		
		try {
			getContainer().run(true, false, runnable);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			DecoredialectUIPlugin.logError("Exception while initializing new file", e);
			return false;
		}
		
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just replace its contents, and open the editor on the newly
	 * created file.
	 */
	private void doFinish(final String containerName, final String fileName, final IProgressMonitor monitor) throws CoreException {
		Shell shell = getShell();
		Display display = shell.getDisplay();
		
		//Monitor and async exec work against each other -> only 
		//update monitor outside of UI thread even if this provides (slightly)
		//more coarse-grained progress information.
		monitor.beginTask("Creating " + fileName, 1);
		
		//Certainly not the best practice to run all this on the UI thread but it takes relatively short
		//and the dialect creation needs access to UI controls to retrieve up-to-date data.
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				IFile file = createFileAndContents(containerName, fileName, monitor);
				
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		
		monitor.worked(1);
	}

	private void completeDeltaDialect() {
		//TODO: If delta dialect not loaded yet but there is text in the box, load it now!
		
		EPackage metaModel = deltaDialect.getDomainPackage();
		List<DEStandardDeltaOperationDefinition> generatedStandardDeltaOperationDefinitions = DEDeltaDialectCreatorUtil.createStandardDeltaOperationDefinitions(metaModel);
		List<DEDeltaOperationDefinition> deltaOperationDefinitions = deltaDialect.getDeltaOperationDefinitions();
		
		deltaOperationDefinitions.addAll(generatedStandardDeltaOperationDefinitions);
	}
	
	private IFile createFileAndContents(String containerName, String fileName, IProgressMonitor monitor) {
		try {
			IFile file = getFile(fileName, containerName);
			
			if (file.exists()) {
				file.delete(true,  monitor);
			}
			
			completeDeltaDialect();
			DEDEcoreDialectUtil.sortDeltaOperationDefinitions(deltaDialect);
			
			boolean success = EcoreIOUtil.saveModelAs(deltaDialect, file);
			
			if (!success) {
				return null;
			}
			
			return file;
		} catch (CoreException e) {
			throw new WrappedException(e);
		}
	}
	
	private IFile getFile(String fileName, String containerName) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		
		IContainer container = (IContainer) resource;

		return container.getFile(new Path(fileName));
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "NewFileContentPrinter", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public String getFileExtension() {
		return new DecoredialectMetaInformation().getSyntaxName();
	}

	public IDecoredialectMetaInformation getMetaInformation() {
		return new DecoredialectMetaInformation();
	}

}
