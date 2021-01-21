package org.deltaecore.feature.graphical.base.editparts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;

public class DERootEditPart extends FreeformGraphicalRootEditPart {
	public static final String SELECTION_LAYER = "SELECTION_LAYER";

	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		
		//Add a special layer for selections that have to go behind elements.
		FreeformLayer selectionLayer = new FreeformLayer();
		selectionLayer.setLayoutManager(new FreeformLayout());
		
		layeredPane.addLayerBefore(selectionLayer, SELECTION_LAYER, LayerConstants.SCALABLE_LAYERS);
	}
}
