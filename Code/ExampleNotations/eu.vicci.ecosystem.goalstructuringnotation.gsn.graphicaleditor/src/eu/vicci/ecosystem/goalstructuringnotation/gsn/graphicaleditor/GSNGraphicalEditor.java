package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor;

import java.io.File;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNModel;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.util.GSNIOUtil;

public class GSNGraphicalEditor extends GraphicalEditorWithFlyoutPalette {

	private GSNModel model;
	
	public GSNGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
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
			
			//Load model from file.
			IPath path = file1.getFullPath();
			
			//TODO: Careful, this is a relative path - need absolute!
			File file = new File(path.toString());
			GSNModel model = GSNIOUtil.loadGSNModel(file);
			
			
			if (model != null) {
				this.model = model;
				GraphicalViewer graphicalViewer = getGraphicalViewer();
				
				if (graphicalViewer != null) {
					graphicalViewer.setContents(model);
				}
			}		
		}
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving...", 1);
		
		GSNIOUtil.saveGSNModel(model);
		getCommandStack().markSaveLocation();
		
		monitor.worked(1);
		monitor.done();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		graphicalViewer.setEditPartFactory(new GSNGraphicalEditPartFactory());
//		graphicalViewer.setRootEditPart(new ScalableFreeformRootEditPart());
		
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, Boolean.TRUE);
//		graphicalViewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, Boolean.TRUE);
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(10, 10));
		
		
//		ActionRegistry actionRegistry = getActionRegistry();
//		
//		actionRegistry.registerAction(new ToggleGridAction(graphicalViewer));
//		actionRegistry.registerAction(new ToggleSnapToGeometryAction(graphicalViewer));
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		if (model != null) {
			graphicalViewer.setContents(model);
		}
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return new GSNGraphicalEditorPalette();
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
        firePropertyChange(PROP_DIRTY);
        super.commandStackChanged(event);
    } 
}
