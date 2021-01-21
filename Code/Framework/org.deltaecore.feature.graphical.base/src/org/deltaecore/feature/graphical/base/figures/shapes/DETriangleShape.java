package org.deltaecore.feature.graphical.base.figures.shapes;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class DETriangleShape extends Shape {
	private Path trianglePath;

	public DETriangleShape() {
		updatePath();
	}

	private void updatePath() {
		Rectangle bounds = getBounds();
		
		int x = bounds.x;
		int y = bounds.y;
		int width = bounds.width - 1;
		int height = bounds.height - 1;
		
		Display display = Display.getCurrent();
		trianglePath = new Path(display);

		trianglePath.moveTo(x, y + height);
		trianglePath.lineTo(x + width, y + height);
		trianglePath.lineTo(x + width / 2, y);
		trianglePath.close();
	}
	
	@Override
	public void setBounds(Rectangle bounds) {
		super.setBounds(bounds);
		
		updatePath();
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		DEDrawingUtil.gradientFillPath(graphics, trianglePath, theme.getVersionTrianglePrimaryColor(), theme.getVersionTriangleSecondaryColor());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		//TODO: externalize?
		graphics.setForegroundColor(theme.getLineColor());
		graphics.setAntialias(SWT.ON);
		graphics.setLineWidth(theme.getVersionLineWidth());

		graphics.drawPath(trianglePath);
	}
}
