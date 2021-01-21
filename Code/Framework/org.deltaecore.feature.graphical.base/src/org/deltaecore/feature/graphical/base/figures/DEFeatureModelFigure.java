package org.deltaecore.feature.graphical.base.figures;

import java.util.Iterator;
import java.util.List;

import org.deltaecore.feature.DEFeature;
import org.deltaecore.feature.DEFeatureModel;
import org.deltaecore.feature.DEGroup;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.layouter.feature.DEFeatureLayouterManager;
import org.deltaecore.feature.graphical.base.util.DEDrawingUtil;
import org.deltaecore.feature.graphical.base.util.DEGraphicalEditorTheme;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;

public class DEFeatureModelFigure extends FreeformLayer {
	private DEFeatureModel featureModel;
	private DEGraphicalEditor graphicalEditor;
	
	public DEFeatureModelFigure(DEFeatureModel featureModel, DEGraphicalEditor graphicalEditor) {
		this.featureModel = featureModel;
		this.graphicalEditor = graphicalEditor;
		
		setLayoutManager(new FreeformLayout());
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		
		paintFeatureAndGroupMarks(graphics);
	}
	
	private void paintFeatureAndGroupMarks(Graphics graphics) {
		Iterator<EObject> iterator = featureModel.eAllContents();
		
		while (iterator.hasNext()) {
			EObject element = iterator.next();
			
			if (element instanceof DEFeature) {
				DEFeature feature = (DEFeature) element;
				Rectangle featureMarkRectangle = getFeatureMarkRectangle(feature);
				
				drawFeatureMarks(feature, graphics);
				
				boolean featureHasError = graphicalEditor.hasError(feature);
				boolean featureHasWarning = graphicalEditor.hasWarning(feature);
				
				if (featureHasError) {
					DEDrawingUtil.drawProblem(graphics, featureMarkRectangle, this, true);
				} else if (featureHasWarning) {
					DEDrawingUtil.drawProblem(graphics, featureMarkRectangle, this, false);
				}
			}
			
			if (element instanceof DEGroup) {
				DEGroup group = (DEGroup) element;
				Rectangle groupMarkRectangle = getGroupMarkRectangle(group);
				
				//It might be that the figure never was visible
				//so there is no rectangle around it either.
				if (groupMarkRectangle != null) {
					boolean groupHasError = graphicalEditor.hasError(group);
					boolean groupHasWarning = graphicalEditor.hasWarning(group);
					
					if (groupHasError) {
						DEDrawingUtil.drawProblem(graphics, groupMarkRectangle, this, true);
					} else if (groupHasWarning) {
						DEDrawingUtil.drawProblem(graphics, groupMarkRectangle, this, false);
					}
				}
			}
		}
	}
	
	protected void drawFeatureMarks(DEFeature feature, Graphics graphics) {
		DEGraphicalEditor graphicalEditor = getGraphicalEditor();
		
		Rectangle featureMarkRectangle = getFeatureMarkRectangle(feature);
		
		boolean featureHasError = graphicalEditor.hasError(feature);
		boolean featureHasWarning = graphicalEditor.hasWarning(feature);
		
		if (featureHasError) {
			DEDrawingUtil.drawProblem(graphics, featureMarkRectangle, this, true);
		} else if (featureHasWarning) {
			DEDrawingUtil.drawProblem(graphics, featureMarkRectangle, this, false);
		}
	}
	
	protected Rectangle getFeatureMarkRectangle(DEFeature feature) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		
		DEFeatureLayouterManager layouterManager = graphicalEditor.getFeatureLayouterManager();
		Rectangle featureMarkRectangle = layouterManager.getBoundsOfFeature(feature);
		
		
		if (DEFeatureFigure.variationTypeCircleVisible(feature)) {
			int increment = (theme.getFeatureVariationTypeExtent() - 1);
			featureMarkRectangle.y += increment;
			featureMarkRectangle.height -= increment;
		}
		
		
		return featureMarkRectangle;
	}
	
	private Rectangle getGroupMarkRectangle(DEGroup searchedGroup) {
		DEGraphicalEditorTheme theme = DEGraphicalEditor.getTheme();
		List<?> children = getChildren();
		
		for (Object child : children) {
			if (child instanceof DEGroupFigure) {
				DEGroupFigure groupFigure = (DEGroupFigure) child;
				DEGroup group = groupFigure.getGroup();
				
				if (group == searchedGroup) {
					Rectangle groupMarkRectangle = groupFigure.getBounds().getCopy();
					groupMarkRectangle.expand(theme.getSecondaryMargin(), theme.getSecondaryMargin());
					return groupMarkRectangle;
				}
			}
		}
		
		return null;
	}

	protected DEFeatureModel getFeatureModel() {
		return featureModel;
	}

	protected DEGraphicalEditor getGraphicalEditor() {
		return graphicalEditor;
	}
}
