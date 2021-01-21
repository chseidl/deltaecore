package org.deltaecore.feature.graphical.base.editor;

import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.deltaecore.feature.graphical.base.util.DEImageExporter;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

//Largely a copy of ScrollingGraphicalViewer thanks to the brilliant idea
//of making createControl() final.
public abstract class DEGraphicalViewer extends GraphicalViewerImpl {
	private DEGraphicalEditor editor;
	private FigureCanvas figureCanvas;
	
	private RectangleFigure dropTargetMarker;
	
	public DEGraphicalViewer(DEGraphicalEditor editor) {
		this.editor = editor;
		
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		dropTargetMarker = new RectangleFigure();
		dropTargetMarker.setForegroundColor(theme.getLineColor());
		dropTargetMarker.setLineWidth(theme.getLineWidth());
		setDropTargetMarkerBounds(new Rectangle());
		
//		addDropTargetListener(new AbstractTransferDropTargetListener(this) {
//			@Override
//			protected void updateTargetRequest() {
//			}
//
//			@Override
//			public void drop(DropTargetEvent event) {
//				super.drop(event);
//				System.out.println("now");
//				setDropTargetMarkerVisible(false);
//			}
//		});
	}
	
	public void setDropTargetMarkerBounds(Rectangle bounds) {
		dropTargetMarker.setBounds(bounds);
	}
	
	public void setDropTargetMarkerVisible(boolean visible) {
		@SuppressWarnings("deprecation")
		IFigure rootFigure = getRootFigure();
		
		if (visible) {
			rootFigure.add(dropTargetMarker);
		}
		
		if (!visible && dropTargetMarker.getParent() == rootFigure) {
			rootFigure.remove(dropTargetMarker);
		}
	}

	public DEGraphicalEditor getGraphicalEditor() {
		return editor;
	}

	public IFile exportImage() {
		DEImageExporter imageExporter = new DEImageExporter(this);
		return imageExporter.exportImage();
	}
	
	// Modified
	@Override
	public Control createControl(Composite parent) {
		createAndSetFigureCanvas(parent);
		
		setControl(figureCanvas);
		
		hookRootFigure();
		registerListeners();
		return getControl();
	}
	
	protected void registerListeners() {
//		figureCanvas.addDragDetectListener(new DragDetectListener() {
//			
//			@Override
//			public void dragDetected(DragDetectEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println(e);
//			}
//		});
	}
	
	/**
	 * Has to call {@link #createAndSetFigureCanvas(Composite) createAndSetFigureCanvas()}.
	 */
	protected Composite createMainComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		createAndSetFigureCanvas(composite);
		return composite;
	}
	
	protected FigureCanvas createAndSetFigureCanvas(Composite parent) {
		FigureCanvas figureCanvas = createFigureCanvas(parent);
		this.figureCanvas = figureCanvas;
		return figureCanvas;
	}
	
	protected FigureCanvas createFigureCanvas(Composite parent) {
		return new FigureCanvas(parent, getLightweightSystem());
	}
	
	// Modified
	public FigureCanvas getFigureCanvas() {
		return figureCanvas;
	}

	// Copied
	@Override
	public void reveal(EditPart part) {
		super.reveal(part);
		Viewport port = getFigureCanvas().getViewport();
		IFigure target = ((GraphicalEditPart) part).getFigure();
		Rectangle exposeRegion = target.getBounds().getCopy();
		target = target.getParent();
		while (target != null && target != port) {
			target.translateToParent(exposeRegion);
			target = target.getParent();
		}
		exposeRegion.expand(5, 5);

		Dimension viewportSize = port.getClientArea().getSize();

		Point topLeft = exposeRegion.getTopLeft();
		Point bottomRight = exposeRegion.getBottomRight().translate(viewportSize.getNegated());
		Point finalLocation = new Point();
		if (viewportSize.width < exposeRegion.width) {
			finalLocation.x = Math.min(bottomRight.x, Math.max(topLeft.x, port.getViewLocation().x));
		} else {
			finalLocation.x = Math.min(topLeft.x, Math.max(bottomRight.x, port.getViewLocation().x));
		}

		if (viewportSize.height < exposeRegion.height) {
			finalLocation.y = Math.min(bottomRight.y, Math.max(topLeft.y, port.getViewLocation().y));
		} else {
			finalLocation.y = Math.min(topLeft.y, Math.max(bottomRight.y, port.getViewLocation().y));
		}

		getFigureCanvas().scrollSmoothTo(finalLocation.x, finalLocation.y);
	}

	// Copied
	@Override
	protected void hookRootFigure() {
		@SuppressWarnings("deprecation")
		IFigure rootFigure = getRootFigure();

		if (getFigureCanvas() == null) {
			return;
		}
		if (rootFigure instanceof Viewport) {
			getFigureCanvas().setViewport((Viewport) rootFigure);
		} else {
			getFigureCanvas().setContents(rootFigure);
		}
	}
}
