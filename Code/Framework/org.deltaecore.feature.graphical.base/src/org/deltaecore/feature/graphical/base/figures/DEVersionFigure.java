package org.deltaecore.feature.graphical.base.figures;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.shapes.DETriangleShape;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionLayouterManager;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionTreeLayouter;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class DEVersionFigure extends DEAbstractFigure {
	private DEVersion version;
	
	private DETriangleShape triangle;
	private Label label;

	public DEVersionFigure(DEVersion version, DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
		
		this.version = version;
		
		setLayoutManager(new XYLayout());
		
		createChildFigures();
		update();
	}

	private void createChildFigures() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		triangle = new DETriangleShape();
		triangle.setSize(theme.getVersionTriangleEdgeLength(), theme.getVersionTriangleEdgeLength());
		add(triangle);
		
		label = new Label();
		label.setLabelAlignment(PositionConstants.CENTER);
		label.setFont(theme.getVersionFont());
		label.setForegroundColor(theme.getVersionFontColor());
		add(label);
	}
	
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}
	
	@Override
	public void update() {
		super.update();
		
		updateContent();
		resizeToContent();
		updateLocation();
	}
	
	private void updateContent() {
		String number = version.getNumber();
		label.setText(number);
	}
	
	private void resizeToContent() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		int width = DEGeometryUtil.calculateVersionWidth(version);
		int height = DEGeometryUtil.calculateVersionHeight(version);
		setSize(width, height);

		
		//Calculate position for version shape
		int versionShapeX = (width - theme.getVersionTriangleEdgeLength()) / 2;
		int versionShapeY = 0;

		triangle.setLocation(new Point(versionShapeX, versionShapeY));

		
		//Calculate position for label
		Dimension preferredLabelDimension = label.getPreferredSize();
		label.setSize(preferredLabelDimension);
		
		int labelX = (width - preferredLabelDimension.width) / 2;
		int labelY = theme.getVersionTriangleEdgeLength() + theme.getSecondaryMargin();
		
		label.setLocation(new Point(labelX, labelY));
	}
	
	private void updateLocation() {
		DEFeature feature = version.getFeature();
		
		if (feature == null) {
			return;
		}
		
		DEVersionTreeLayouter versionTreeLayouter = DEVersionLayouterManager.getLayouter(feature);

		Point location = versionTreeLayouter.getCoordinates(version);
		setLocation(location);
	}

	public Label getLabel() {
		return label;
	}
}
