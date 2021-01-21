package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;

public class CFDIntermediateEventFigure extends CFDEventFigure {

	@Override
	protected Class<? extends CFDElement> getElementType() {
		return CFDIntermediateEvent.class;
	}

}
