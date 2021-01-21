package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class BooleanPropertyDescriptor extends ComboBoxPropertyDescriptor {

	public BooleanPropertyDescriptor(Object id, String displayName) {
		super(id, displayName, new String[] {"false", "true"});
	}

}
