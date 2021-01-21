package eu.vicci.ecosystem.goalstructuringnotation.gsn;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.impl.GSNFactoryImpl;

public class GSNFactoryImplCustom extends GSNFactoryImpl {
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
			rect.setLocation(Integer.parseInt(values[0]),
					Integer.parseInt(values[1]));
			rect.setSize(Integer.parseInt(values[2]),
					Integer.parseInt(values[3]));
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
}
