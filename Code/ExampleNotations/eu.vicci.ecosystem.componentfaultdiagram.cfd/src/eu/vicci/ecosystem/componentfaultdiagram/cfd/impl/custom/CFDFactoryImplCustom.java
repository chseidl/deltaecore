package eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.custom;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDDiagram;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.impl.CFDFactoryImpl;

public class CFDFactoryImplCustom extends CFDFactoryImpl {

	@Override
	public Rectangle createRectangleFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) {
			return null;
		}
		initialValue.replaceAll("\\s", "");
		String[] values = initialValue.split(",");
		if (values.length != 4) {
			return null;
		}

		Rectangle rect = new Rectangle();
		try {
			rect.setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
			rect.setSize(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
		} catch (NumberFormatException e) {
			EcorePlugin.INSTANCE.log(e);
			rect = null;
		}
		return rect;
	}

	@Override
	public String convertRectangleToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return null;
		}
		Rectangle rect = (Rectangle) instanceValue;
		return rect.x + "," + rect.y + "," + rect.width + "," + rect.height;
	}

	@Override
	public CFDDiagram createCFDDiagram() {
		return new CFDDiagramImplCustom();
	}

	@Override
	public CFDComponent createCFDComponent() {
		return new CFDComponentImplCustom();
	}

	@Override
	public CFDBasicEvent createCFDBasicEvent() {
		return new CFDBasicEventImplCustom();
	}

	@Override
	public CFDIntermediateEvent createCFDIntermediateEvent() {
		return new CFDIntermediateEventImplCustom();
	}

	@Override
	public CFDGate createCFDGate() {
		return new CFDGateImplCustom();
	}

	@Override
	public CFDInPort createCFDInPort() {
		return new CFDInPortImplCustom();
	}

	@Override
	public CFDOutPort createCFDOutPort() {
		return new CFDOutPortImplCustom();
	}

	@Override
	public CFDConnection createCFDConnection() {
		return new CFDConnectionImplCustom();
	}
}
