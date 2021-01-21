package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor;

import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import de.christophseidl.util.ecore.EcoreIOUtil;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;

public class CFDGraphicalEditor extends GraphicalEditorWithFlyoutPalette {

	private CFDDiagram diagram;
//	private PropertySheetPage propertySheetPage;
	
	public CFDGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
//		propertySheetPage = null;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		
		loadFromInput(input);
	}
	
	protected void loadFromInput(IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) input;
			
			IFile file1 = fileEditorInput.getFile();
			
//			//Load model from file.
//			IPath path = file1.getFullPath();
//			
//			//TODO: Careful, this is a relative path - need absolute!
//			File file = new File(path.toString());
			CFDDiagram diagram = EcoreIOUtil.loadModel(file1);
			
			if (diagram != null) {
				this.diagram = diagram;
				GraphicalViewer graphicalViewer = getGraphicalViewer();
				
				if (graphicalViewer != null) {
					graphicalViewer.setContents(diagram);
				}
			}		
		}
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving...", 1);
		
		EcoreIOUtil.saveModel(diagram);
		getCommandStack().markSaveLocation();
		
		monitor.worked(1);
		monitor.done();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		graphicalViewer.setEditPartFactory(new CFDGraphicalEditPartFactory());
//		graphicalViewer.setRootEditPart(new ScalableFreeformRootEditPart());
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		if (diagram != null) {
			graphicalViewer.setContents(diagram);
		}
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return new CFDGraphicalEditorPalette();
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
        firePropertyChange(PROP_DIRTY);
        super.commandStackChanged(event);
    }
	

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
//		if (type.equals(IPropertySheetPage.class)) {
//			if (propertySheetPage == null) {
//				propertySheetPage = createPropertySheetPage();
//			}
//			
//			return propertySheetPage;
//		}
		
		return super.getAdapter(type);
	}
	
//	private PropertySheetPage createPropertySheetPage() {
//		PropertySheetPage propertySheetPage = (UndoablePropertySheetPage) super.getAdapter(IPropertySheetPage.class);
//		
//		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
//		
//		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
//		adapterFactory.addAdapterFactory(new CFDItemProviderAdapterFactory());
//		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
//		
////		IPropertySourceProvider modelPropertySourceProvider = new AdapterFactoryContentProvider(new CFDItemProviderAdapterFactory());
//		final IPropertySourceProvider modelPropertySourceProvider = new AdapterFactoryContentProvider(adapterFactory);
//		
//		IPropertySourceProvider sourceProvider = new IPropertySourceProvider() {
//			@Override
//			public IPropertySource getPropertySource(Object object) {
//				Object model = null;
//				
//				if (object instanceof EditPart) {
//					EditPart editPart = (EditPart) object;
//					model = editPart.getModel();
//				} else {
//					model = object;
//				}
//				
//				if (model == null) {
//					return null;
//				}
//				
//				IPropertySource source = modelPropertySourceProvider.getPropertySource(model);
//				if (source == null) {
//					int haltHere = 0;
//				}
//				return new UnwrappingPropertySource(source);
//			}
//		};
//		
//		UndoablePropertySheetEntry root = new UndoablePropertySheetEntry(getCommandStack());
//		root.setPropertySourceProvider(sourceProvider);
//		propertySheetPage.setRootEntry(root);
//		
//		return propertySheetPage;
//	}
}
