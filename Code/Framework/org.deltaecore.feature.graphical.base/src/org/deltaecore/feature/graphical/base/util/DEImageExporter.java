package org.deltaecore.feature.graphical.base.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.constraint.util.DEConstraintIOUtil;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import de.christophseidl.util.eclipse.ResourceUtil;

public class DEImageExporter {
	private DEGraphicalViewer viewer;
	
	public DEImageExporter(DEGraphicalViewer viewer) {
		this.viewer = viewer;
	}
	
	public IFile exportImage() {
		IFile imageFile = createImageFileFromEditorInput();
		exportImage(imageFile);
		return imageFile;
	}
	
	public IFile exportImage(IFile imageFile) {
		if (imageFile == null) {
			throw new InvalidParameterException("File must be specified");
		}
		
		Image image = drawImage();
		saveImage(image, imageFile);
		image.dispose();
		
		return imageFile;
	}
	
	private IFile createImageFileFromEditorInput() {
		DEGraphicalEditor graphicalEditor = viewer.getGraphicalEditor();
		
		IEditorInput input = graphicalEditor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			IFile featureModelFile = fileInput.getFile();
			IFile imageFile = ResourceUtil.deriveFile(featureModelFile, "png");
			return imageFile;
		}
		
		return null;
	}
	
	private Image drawImage() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Image hyperFeatureModelImge = drawHyperFeatureModel();
		Image constraintsImage = drawConstraints();
		
		Rectangle hyperFeatureModelBounds = hyperFeatureModelImge.getBounds();
		Rectangle constraintsImageBounds = (constraintsImage == null) ? new Rectangle(0, 0, 0, 0) : constraintsImage.getBounds();
		
		//TODO: If constraints would overlap, extend the image's width
		int imageWidth = hyperFeatureModelBounds.width;
		
		//TODO: Image height may actually be smaller if feature model does not fill entire viewport - calculate differently!
		int imageHeight = Math.max(hyperFeatureModelBounds.height, constraintsImageBounds.height);
		
		//NOTE: Temporary workaround until editor has a margin as well
		imageWidth += theme.getPrimaryMargin();
		imageHeight += theme.getPrimaryMargin();
		
		
		Image image = new Image(Display.getDefault(), imageWidth, imageHeight);
		GC gc = new GC(image);

		//Make sure there is a solid background
		gc.setForeground(ColorConstants.white);
		gc.fillRectangle(0, 0, imageWidth, imageHeight);
		
		//Compose image
		gc.drawImage(hyperFeatureModelImge, 0, 0);

		//Place constraints right aligned on the right side of the image
		if (constraintsImage != null) {
			int constraintsImageX = imageWidth - constraintsImageBounds.width - theme.getPrimaryMargin();
			int constraintsImageY = theme.getPrimaryMargin();
		
			gc.drawImage(constraintsImage, constraintsImageX, constraintsImageY);
		}
		
		
		//Print the filename
		gc.setForeground(ColorConstants.black);
		gc.setFont(theme.getFeatureFont());
		
		DEGraphicalEditor graphicalEditor = viewer.getGraphicalEditor();
		
		IEditorInput editorInput = graphicalEditor.getEditorInput();
		IFile featureModelFile = org.eclipse.ui.ide.ResourceUtil.getFile(editorInput);
		
		String filename = featureModelFile.getName();
		String extension = featureModelFile.getFileExtension();
		String name = filename.substring(0, filename.length() - extension.length() - 1);
		gc.drawText(name, theme.getPrimaryMargin(), theme.getPrimaryMargin());
		
		
		//Dispose resources.
		gc.dispose();
		
		if (constraintsImage != null) {
			constraintsImage.dispose();
		}
		
		hyperFeatureModelImge.dispose();
		
		return image;
	}
	
	private Image drawHyperFeatureModel() {
		LayerManager rootEditPart = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
		IFigure rootFigure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
		org.eclipse.draw2d.geometry.Rectangle rootFigureBounds = rootFigure.getBounds();
		
		int imageWidth = rootFigureBounds.width;
		int imageHeight = rootFigureBounds.height;
		
		Image image = new Image(Display.getDefault(), imageWidth, imageHeight);
		GC gc = new GC(image);
		
		FigureCanvas figureCanvas = viewer.getFigureCanvas();
		
		//Draw the figure to the graphical context
		//Temporarily resize figure to avoid scroll bars. TODO: Improve some time: Causes flickering of editor.
		Point oldSize = figureCanvas.getSize();
		figureCanvas.setSize(imageWidth, imageHeight);
		figureCanvas.print(gc); // This is Eclipse 3.4 only API
		figureCanvas.setSize(oldSize);
		
		gc.dispose();
		
		return image;
	}
	
	private Image drawConstraints() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		DEGraphicalEditor graphicalEditor = viewer.getGraphicalEditor();
		
		DEFeatureModel featureModel = graphicalEditor.getFeatureModel();
		IFile constraintFile = DEConstraintIOUtil.getAccompanyingConstraintFile(featureModel);
		
		if (constraintFile != null) {
			//Load constraints file as text file.
			List<String> lines = readFile(constraintFile.getLocation().toOSString());
			
			int maxLineWidth = 0;
			int totalLinesHeight = 0;
			List<Integer> lineHeights = new ArrayList<Integer>();
			
			Font font = theme.getFeatureFont();
			
			//Determine dimensions
			Iterator<String> iterator = lines.iterator();
			
			while (iterator.hasNext()) {
				String line = iterator.next();
				int lineWidth = DEGeometryUtil.getTextWidth(line, font);
				int lineHeight = DEGeometryUtil.getTextHeight(line, font);
				
				maxLineWidth = Math.max(maxLineWidth, lineWidth);
				lineHeights.add(lineHeight);
				
				totalLinesHeight += lineHeight + theme.getSecondaryMargin();
			}
			
			
			Image image = new Image(Display.getDefault(), maxLineWidth, totalLinesHeight);
			GC gc = new GC(image);
			
			gc.setForeground(ColorConstants.black);
			gc.setFont(font);

			int y = 0;
			
			for(int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				
				gc.drawText(line, 0, y);
				
				Integer lineHeight = lineHeights.get(i);
				y += lineHeight + theme.getSecondaryMargin(); 
			}
			
			
			gc.dispose();
			
			return image;
		}

		return null;
	}
	
	private static List<String> readFile(String fileName) {
		List<String> lines = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));
	    
	        String line = bufferedReader.readLine();

	        while (line != null) {
	        	lines.add(line);
	            line = bufferedReader.readLine();
	        }
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
	    	if (bufferedReader != null) {
	    		try {
	    			bufferedReader.close();
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
		
        return lines;
	}
	
//	private Image drawWatermark() {
//		//Add a watermark
//		String watermark = "Created with DeltaEcore Hyper Feature Model Editor (www.deltaecore.org)";
//		gc.setFont(DEFeatureModelEditorConstants.FEATURE_FONT);
//		gc.setForeground(DEFeatureModelEditorConstants.VERSION_FONT_COLOR);
//		gc.drawText(watermark, 10, 10);
//	}
	
	private void saveImage(Image image, IFile imageFile) {
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { image.getImageData() };
		IPath imageFilepath = imageFile.getLocation();
		String imageFilename = imageFilepath.toString();
		imageLoader.save(imageFilename, SWT.IMAGE_PNG);
		
		ResourceUtil.refreshResource(imageFile);
	}
	
	//TODO: Look into Batik for SVG export: http://xmlgraphics.apache.org/batik/using/svg-generator.html
	
	//Renderer seems to come from GMF
//	public void exportToSVG(String saveLocation, IFigure rootFigure, IProgressMonitor monitor) {
//		SubProgressMonitor subProgressMonitor = new SubProgressMonitor(monitor, 10);
//		subProgressMonitor.setTaskName("Creating SVG image...");
//		Rectangle rootFigureBounds = rootFigure.getBounds();
//		GraphicsSVG graphics = GraphicsSVG.getInstance(rootFigureBounds.getTranslated(rootF igureBounds.getLocation().negate()));
//		SVGIDGenerator generator = new SVGIDGenerator();
//
//		subProgressMonitor.worked(2);
//		graphics.getSVGGraphics2D().getGeneratorContext().setIDGener ator(generator);
//		graphics.translate(rootFigureBounds.getLocation().negate());
//		rootFigure.paint(graphics);
//		OutputStream outputStream = null;
//		try {
//		outputStream = new FileOutputStream (saveLocation);
//		graphics.getSVGGraphics2D().stream(new BufferedWriter(new OutputStreamWriter(outputStream)));
//		subProgressMonitor.worked(4);
//		subProgressMonitor.done();
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		subProgressMonitor.done();
//
//		}
}
