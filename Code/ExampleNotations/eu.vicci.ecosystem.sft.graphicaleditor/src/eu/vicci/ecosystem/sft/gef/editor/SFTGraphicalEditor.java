package eu.vicci.ecosystem.sft.gef.editor;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import eu.vicci.ecosystem.sft.SFTPackage;
import eu.vicci.ecosystem.sft.SFTSoftwareFaultTree;
import eu.vicci.ecosystem.sft.gef.factories.SFTEditPartFactory;

public class SFTGraphicalEditor /*extends GraphicalEditorWithFlyoutPalette */ extends GraphicalEditor{

	Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	private Resource sftResource;
	private SFTSoftwareFaultTree sft;

	public SFTGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void initializeGraphicalViewer() {
		logger.info("Initialize Graphical Viewer");
		getGraphicalViewer().setContents(sft);
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();	
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
		viewer.setEditPartFactory(new SFTEditPartFactory());
	}



	@Override
	public void doSave(IProgressMonitor monitor) {
		if (sftResource == null) {
			return;
		}

		try {
			sftResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			sftResource = null;
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		logger.trace("SFTGraphicalEditor init()");

		super.init(site, input);
		loadInput(input);

	}

	private void loadInput(IEditorInput input) {
		SFTPackage.eINSTANCE.eClass(); // This initializes the SFTPackage singleton implementation.
		ResourceSet resourceSet = new ResourceSetImpl();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			IFile file = fileInput.getFile();
			sftResource = resourceSet.createResource(URI.createURI(file.getLocationURI().toString()));
			try {
				sftResource.load(null);
				sft = (SFTSoftwareFaultTree) sftResource.getContents().get(0);
			} catch (IOException e) {
				e.printStackTrace();
				sftResource = null;
			}
		}
	}

}
