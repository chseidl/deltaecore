package org.deltaecore.feature.graphical.base.figures;

import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.DEVersion;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.figures.shapes.DELineWithArrowShape;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionLayouterManager;
import org.deltaecore.feature.graphical.base.layouter.version.DEVersionTreeLayouter;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.base.util.DEGeometryUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.deltaecore.feature.util.DEFeatureUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class DEFeatureFigure extends DEAbstractFigure {
	private DEFeature feature;

	private Label label;
	
	public DEFeatureFigure(DEFeature feature, DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
		
		this.feature = feature;

		setLayoutManager(new XYLayout());
		
		createChildFigures();
		update();
	}

	private void createChildFigures() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		label = new Label();
		label.setFont(theme.getFeatureFont());
		label.setForegroundColor(theme.getFeatureFontColor());
		add(label);
	}

	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}
	
	@Override
	protected Rectangle getEffectiveBounds() {
		Rectangle bounds = getBounds();
		Rectangle effectiveBounds = bounds.getCopy();
		
		if (variationTypeCircleVisible(feature)) {
			DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
			
			int offset = theme.getFeatureVariationTypeExtent();
			
			effectiveBounds.y += offset;
			effectiveBounds.height -= offset;
		}
		
		return effectiveBounds;
	}
	
	private Rectangle calculateVariationTypeCircleBounds() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle bounds = getBounds();
		
		int offset = theme.getFeatureVariationTypeExtent();
		
		int x = bounds.x + (bounds.width - offset) / 2;
		int y = bounds.y;
		int width = offset;
		int height = offset;
		
		return new Rectangle(x, y, width, height);
	}
	
	private int getEffectiveVariationTypeExtent() {
		if (variationTypeCircleVisible(feature)) {
			DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
			return theme.getFeatureVariationTypeExtent();
		}
		
		return 0;
	}
	
	private Rectangle calculateNameAreaBounds() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle bounds = getBounds();
		Rectangle nameAreaBounds = bounds.getCopy();
		
		nameAreaBounds.y += getEffectiveVariationTypeExtent();
		nameAreaBounds.height = theme.getFeatureNameAreaHeight();
		
		return nameAreaBounds;
	}
	
	private Rectangle calculateVersionAreaBounds() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle bounds = getBounds();
		Rectangle versionAreaBounds = bounds.getCopy();
		
		int variationTypeAndNameAreaHeight = getEffectiveVariationTypeExtent() + theme.getFeatureNameAreaHeight();
		
		versionAreaBounds.y += variationTypeAndNameAreaHeight;
		versionAreaBounds.height -= variationTypeAndNameAreaHeight;
		
		return versionAreaBounds;
	}
	
	@Override
	public void update() {
		super.update();
		
		updateContent();
		resizeToContent();
		updateLocation();

		//This is necessary to reflect changes in variation type.
		repaint();
	}
	
	private void updateContent() {
		String featureName = feature.getName();
		label.setText(featureName);
	}
	
	private void resizeToContent() {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		int width = DEGeometryUtil.calculateFeatureWidth(feature);
		int height = DEGeometryUtil.calculateFeatureHeight(feature);
		
		setSize(width, height);
		
		//Label size
		Rectangle nameAreaBounds = calculateNameAreaBounds();
		Dimension preferredLabelSize = label.getPreferredSize();
		
		int labelWidth = nameAreaBounds.width;
		int labelHeight = preferredLabelSize.height;
		int labelX = 0;
		int labelY = getEffectiveVariationTypeExtent() + (theme.getFeatureNameAreaHeight() - labelHeight) / 2;
		
		Rectangle labelBounds = new Rectangle(labelX, labelY, labelWidth, labelHeight);
		label.setBounds(labelBounds);
		setConstraint(label, labelBounds);
	}
	
	protected void updateLocation() {
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		DEFeatureLayouterManager layouterManager = graphicalEditor.getFeatureLayouterManager();
		Rectangle boundsOfFeature = layouterManager.getBoundsOfFeature(feature);
		Point location = new Point(boundsOfFeature.x, boundsOfFeature.y);
		setLocation(location);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		if (variationTypeCircleVisible(feature)) {
			paintVariationTypeCircle(graphics);
		}
		
		if (versionAreaIsVisible(feature)) {
			paintVersionAreaBackground(graphics);
			paintVersionConnections(graphics);
		}
		
		paintNameAreaBackground(graphics);
	}
	
	private void paintVariationTypeCircle(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		Color light = feature.isMandatory() ? theme.getFeatureMandatoryPrimaryColor() : theme.getFeatureOptionalPrimaryColor();
		Color dark = feature.isMandatory() ? theme.getFeatureMandatorySecondaryColor() : theme.getFeatureOptionalSecondaryColor();

		Rectangle variationTypeCircleBounds = calculateVariationTypeCircleBounds();

		//Compensate for line width
		int lineWidth = theme.getLineWidth();
		variationTypeCircleBounds.expand(-lineWidth, -lineWidth);
		
		DEDrawingUtil.gradientFillEllipsis(graphics, variationTypeCircleBounds, light, dark);
		DEDrawingUtil.outlineEllipsis(graphics, variationTypeCircleBounds, theme.getLineColor());
	}
	
	private void paintNameAreaBackground(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle nameAreaBounds = calculateNameAreaBounds();
		
		//Compensate for line width
		int lineWidth = theme.getLineWidth();
		int halfLineWidth = (int) Math.ceil(lineWidth / 2);
		
		nameAreaBounds.x += halfLineWidth;
		nameAreaBounds.width -= lineWidth;
		
		if (!variationTypeCircleVisible(feature)) {
			nameAreaBounds.y += halfLineWidth;
			nameAreaBounds.height -= lineWidth;
		} else {
			nameAreaBounds.height -= halfLineWidth;
		}
		
		DEDrawingUtil.gradientFillRectangle(graphics, nameAreaBounds, theme.getFeatureNameAreaPrimaryColor(), theme.getFeatureNameAreaSecondaryColor());
		DEDrawingUtil.outlineRectangle(graphics, nameAreaBounds, theme.getLineColor());
	}
	
	private void paintVersionAreaBackground(Graphics graphics) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		Rectangle versionAreaBounds = calculateVersionAreaBounds();
		
		//Compensate for line width
		int halfLineWidth = (int) Math.ceil(theme.getLineWidth() / 2);
		versionAreaBounds.expand(new Insets(0, -halfLineWidth, -halfLineWidth, -halfLineWidth));
		
		DEDrawingUtil.gradientFillRectangle(graphics, versionAreaBounds, theme.getFeatureVersionAreaPrimaryColor(), theme.getFeatureVersionAreaSecondaryColor());
		
		paintVersionMarks(graphics);
		
		DEDrawingUtil.outlineRectangle(graphics, versionAreaBounds, theme.getLineColor());
	}
	
	protected void paintVersionMarks(Graphics graphics) {
		DEFeature feature = getFeature();
		List<DEVersion> versions = feature.getVersions();

		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		
		for (DEVersion version : versions) {
			Rectangle versionMarkRectangle = getVersionMarkRectangle(version);
			
			boolean versionHasError = graphicalEditor.hasError(version);
			boolean versionHasWarning = graphicalEditor.hasWarning(version);
			
			if (versionHasError) {
				DEDrawingUtil.drawProblem(graphics, versionMarkRectangle, this, true);
			} else if (versionHasWarning) {
				DEDrawingUtil.drawProblem(graphics, versionMarkRectangle, this, false);
			}
		}
	}
	
	protected Rectangle getVersionMarkRectangle(DEVersion version) {
		DEFeature feature = getFeature();
		DEVersionTreeLayouter versionTree = DEVersionLayouterManager.getLayouter(feature);
		Rectangle versionBounds = versionTree.getBounds(version);
		
		return versionBounds;
	}
	
	private void paintVersionConnections(Graphics graphics) {
		List<DEVersion> versions = feature.getVersions();
		DELineWithArrowShape arrow = new DELineWithArrowShape();
		
		for (DEVersion version : versions) {
			//From the right of this version to the left of the superseding version
			Point versionPoint = calculateVersionConnectionAnchorRight(version);
			
			List<DEVersion> predecessors = version.getSupersedingVersions();
			
			for (DEVersion predecessor : predecessors) {
				Point predecessorPoint = calculateVersionConnectionAnchorLeft(predecessor);
				
				//Line with arrow head
				arrow.setStartPoint(versionPoint);
				arrow.setEndPoint(predecessorPoint);
				arrow.paint(graphics);
			}
		}
	}
	
	private Point calculateVersionConnectionAnchorLeft(DEVersion version) {
		return doCalculateVersionConnectionAnchorRight(version, false);
	}

	private Point calculateVersionConnectionAnchorRight(DEVersion version) {
		return doCalculateVersionConnectionAnchorRight(version, true);
	}
	
	private Point doCalculateVersionConnectionAnchorRight(DEVersion version, boolean right) {
		DEFeature feature = version.getFeature();
		
		if (feature == null) {
			//For robustness. Should not be called in regular operation.
			return new Point(10, 10);
		}
		
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		DEVersionTreeLayouter versionTree = DEVersionLayouterManager.getLayouter(feature);

		Rectangle versionBounds = versionTree.getBounds(version);
		
		int versionX = versionBounds.x;
		
		if (right) {
			versionX += theme.getSecondaryMargin() + versionBounds.width;
		} else {
			versionX -= theme.getSecondaryMargin();
		}
		
		int versionY = versionBounds.y + (theme.getVersionTriangleEdgeLength() / 2);
		
		
		//Offset with bounds as absolute coordinates are required.
		Rectangle bounds = getBounds();
		
		return new Point(bounds.x + versionX, bounds.y + versionY);
	}
	
	public static boolean variationTypeCircleVisible(DEFeature feature) {
		if (feature == null) {
			return false;
		}
		
		boolean isRoot = !DEFeatureUtil.isRootFeature(feature);
		boolean hasStandardVariabilityType = feature.isOptional() || feature.isMandatory();
		DEGroup group = feature.getParentOfFeature();
		boolean isPartOfPaintedStandardGroup = group == null ? false : (group.isAlternative() || group.isOr());
		boolean isOnlyFeatureInGroup = group == null ? false : group.getFeatures().size() == 1;
		boolean shouldBeHidden = isPartOfPaintedStandardGroup && !isOnlyFeatureInGroup && feature.isOptional();
		
		return (isRoot && hasStandardVariabilityType && !shouldBeHidden);
	}
	
	public static boolean versionAreaIsVisible(DEFeature feature) {
		List<DEVersion> versions = feature.getVersions();
		return !versions.isEmpty();
	}

	protected DEFeature getFeature() {
		return feature;
	}

	public Label getLabel() {
		return label;
	}
}
