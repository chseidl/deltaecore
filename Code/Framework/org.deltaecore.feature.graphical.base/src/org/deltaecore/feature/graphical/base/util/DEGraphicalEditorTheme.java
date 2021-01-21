package org.deltaecore.feature.graphical.base.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Class that provides all customizable settings used by the editor!
 * 
 * @author Florian, Christoph
 * 
 */
public class DEGraphicalEditorTheme {
	protected static final Color WHITE = new Color(null, 255, 255, 255);
	protected static final Color LIGHTGRAY = new Color(null, 192, 192, 192);
	protected static final Color GRAY = new Color(null, 128, 128, 128);
	protected static final Color DARKGRAY = new Color(null, 64, 64, 64);
	protected static final Color BLACK = new Color(null, 0, 0, 0);
	
	
	//Colors
	private Color selectionSecondaryColor = new Color(null, 244, 116, 10);
	private Color selectionPrimaryColor = new Color(null, 251, 185, 119);
	private Color selectionLineColor = new Color(null, 182, 109, 49);

	private Color previousSelectionSecondaryColor = new Color(null, 122, 199, 223);
	private Color previousSelectionPrimaryColor = new Color(null, 169, 220, 234);
	private Color previousSelectionLineColor = new Color(null, 86, 145, 163);
	
	private Color errorLineColor = new Color(null, 255, 58, 45);
	private Color warningLineColor = new Color(null, 242, 144, 0);
	
	private Color groupAlternativePrimaryColor = WHITE;
	private Color groupAlternativeSecondaryColor = LIGHTGRAY;
	
	private Color lineColor = BLACK;
	
	private Color featureNameAreaPrimaryColor = GRAY;
	private Color featureNameAreaSecondaryColor = BLACK;

	private Color featureVersionAreaPrimaryColor = WHITE;
	private Color featureVersionAreaSecondaryColor = new Color(null, 240, 240, 240);
	
	private Color featureFontColor = WHITE;

	private Color featureMandatoryPrimaryColor = GRAY;
	private Color featureMandatorySecondaryColor = DARKGRAY;
	private Color featureOptionalPrimaryColor = WHITE;
	private Color featureOptionalSecondaryColor = LIGHTGRAY;
	
	
	private Color groupOrPrimaryColor = GRAY;
	private Color groupOrSecondaryColor = BLACK;
	
	
	private Color versionFontColor = BLACK;
	
	private Color versionTrianglePrimaryColor = new Color(null, 138, 184, 0);
	private Color versionTriangleSecondaryColor = new Color(null, 138, 184, 0);
	
	private Color versionArrowColor = BLACK;
	
	
	//Font
	private Font featureFont = new Font(Display.getCurrent(), "Tahoma", 9, SWT.BOLD);
	private Font versionFont = new Font(Display.getCurrent(), "Tahoma", 8, SWT.NORMAL);
	
	

	private int lineWidth = 2;

	// A feature consists of three parts:
	// - some space for the circle representing the variation type
	// - the name area
	// - the version area 
	private int featureVariationTypeExtent = 17;
	private int featureNameAreaHeight = 25;
	private int featureVersionAreaDefaultHeight = 33;
	
	private int featureMinimumWidth = 90;//50;//130; //Temporary override

	//TODO: rename
	// These values represent the minimum distance between features in x and y
	private int featureExtentX = 10; //25; //Temporary override
	private int featureExtentY = 30; //80; //Temporary override
	
	
	private int groupSymbolRadius = 25;
	
	private int primaryMargin = 10;
	private int secondaryMargin = 4;
	
	private int versionTriangleEdgeLength = 15;
	
	
	//TODO: rename
	// These values are necessary in order to avoid that the arrow overlaps
	// with any other figures
	private int versionExtentX = 30;
	private int versionExtentY = 10;
	

	private int versionLineWidth = 1;
	private int arrowHeadEdgeLength = 3;


	
	public DEGraphicalEditorTheme() {
		
	}
	
	
	
	
	//====================================================================================== Colors =
	
	public Color getSelectionPrimaryColor() {
		return selectionPrimaryColor;
	}
	
	public Color getSelectionSecondaryColor() {
		return selectionSecondaryColor;
	}
	
	public Color getSelectionLineColor() {
		return selectionLineColor;
	}
	
	
	public Color getPreviousSelectionPrimaryColor() {
		return previousSelectionPrimaryColor;
	}
	
	public Color getPreviousSelectionSecondaryColor() {
		return previousSelectionSecondaryColor;
	}
	
	public Color getPreviousSelectionLineColor() {
		return previousSelectionLineColor;
	}
	
	
	public Color getErrorLineColor() {
		return errorLineColor;
	}
	
	public Color getWarningLineColor() {
		return warningLineColor;
	}
	
	
	
	public Color getGroupAlternativePrimaryColor() {
		return groupAlternativePrimaryColor;
	}
	
	public Color getGroupAlternativeSecondaryColor() {
		return groupAlternativeSecondaryColor;
	}
	
	
	public Color getLineColor() {
		return lineColor;
	}
	
	
	public Color getFeatureNameAreaPrimaryColor() {
		return featureNameAreaPrimaryColor;
	}
	
	public Color getFeatureNameAreaSecondaryColor() {
		return featureNameAreaSecondaryColor;
	}
	
	
	public Color getFeatureVersionAreaPrimaryColor() {
		return featureVersionAreaPrimaryColor;
	}
	
	public Color getFeatureVersionAreaSecondaryColor() {
		return featureVersionAreaSecondaryColor;
	}
	
	public Color getFeatureFontColor() {
		return featureFontColor;
	}
	
	
	
	// Circle (feature type)
	public Color getFeatureMandatoryPrimaryColor() {
		return featureMandatoryPrimaryColor;
	}
	
	public Color getFeatureMandatorySecondaryColor() {
		return featureMandatorySecondaryColor;
	}
	
	public Color getFeatureOptionalPrimaryColor() {
		return featureOptionalPrimaryColor;
	}
	
	
	public Color getFeatureOptionalSecondaryColor() {
		return featureOptionalSecondaryColor;
	}
	
	
	public Color getGroupOrPrimaryColor() {
		return groupOrPrimaryColor;
	}
	
	public Color getGroupOrSecondaryColor() {
		return groupOrSecondaryColor;	
	}
	
	
	public Color getVersionFontColor() {
		return versionFontColor;
	}
	
	
	//TODO: Primary/secondary
	public Color getVersionTrianglePrimaryColor() {
		return versionTrianglePrimaryColor;
	}
	
	public Color getVersionTriangleSecondaryColor() {
		return versionTriangleSecondaryColor;
	}
	
	public Color getVersionArrowColor() {
		return versionArrowColor;
	}


	
//======================================================================================= Fonts =
	
	public Font getFeatureFont() {
		return featureFont;
	}
	
	public Font getVersionFont() {
		return versionFont;
	}
	


//==================================================================================== Geometry =

	//Geometry
	public int getLineWidth() {
		return lineWidth;
	}
	
	public int getFeatureVariationTypeExtent() {
		return featureVariationTypeExtent;
	}
	
	public int getFeatureNameAreaHeight() {
		return featureNameAreaHeight;
	}
	
	public int getFeatureVersionAreaDefaultHeight() {
		return featureVersionAreaDefaultHeight;
	}
	
	public int getFeatureMinimumWidth() {
		return featureMinimumWidth;
	}
	
	public int getFeatureExtentX() {
		return featureExtentX;
	}
	
	public int getFeatureExtentY() {
		return featureExtentY;
	}
	
	public int getGroupSymbolRadius() {
		return groupSymbolRadius;
	}
	
	public int getPrimaryMargin() {
		return primaryMargin;
	}
	
	public int getSecondaryMargin() {
		return secondaryMargin;
	}
	
	
	public int getVersionTriangleEdgeLength() {
		return versionTriangleEdgeLength;
	}
	
	//TODO: Check this
	public int getVersionExtentX() {
		return versionExtentX;
	}

	//TODO: Check this
	public int getVersionExtentY() {
		return versionExtentY;
	}
	
	public int getVersionLineWidth() {
		return versionLineWidth;
	}
	
	public int getArrowHeadEdgeLength() {
		return arrowHeadEdgeLength;
	}

}
