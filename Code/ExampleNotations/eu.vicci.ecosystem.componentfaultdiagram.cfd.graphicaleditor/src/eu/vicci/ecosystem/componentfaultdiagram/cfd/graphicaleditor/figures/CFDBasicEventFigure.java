package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;

public class CFDBasicEventFigure extends CFDEventFigure {

	@Override
	protected Class<? extends CFDElement> getElementType() {
		return CFDBasicEvent.class;
	}

}
