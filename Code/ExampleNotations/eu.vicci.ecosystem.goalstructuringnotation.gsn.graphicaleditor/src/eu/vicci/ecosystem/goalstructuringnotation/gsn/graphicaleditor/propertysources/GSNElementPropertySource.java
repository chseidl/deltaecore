package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.propertysources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConcreteElement;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNPackage;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util.BooleanPropertyDescriptor;

public class GSNElementPropertySource implements IPropertySource {
	private GSNConcreteElement element;
	private String[] elementTypeNames;
	
	public GSNElementPropertySource(GSNConcreteElement element) {
		this.element = element;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		
		elementTypeNames = createElementTypeNames();
		
		properties.add(new TextPropertyDescriptor(GSNPackage.GSN_CONCRETE_ELEMENT__ID, "Id"));
		properties.add(new TextPropertyDescriptor(GSNPackage.GSN_CONCRETE_ELEMENT__DESCRIPTION, "Description"));
		properties.add(new ComboBoxPropertyDescriptor(GSNPackage.GSN_CONCRETE_ELEMENT__TYPE, "Type", elementTypeNames));
		properties.add(new BooleanPropertyDescriptor(GSNPackage.GSN_CONCRETE_ELEMENT__AWAY, "Away"));

		return properties.toArray(new IPropertyDescriptor[0]);
	}

	private String[] createElementTypeNames() {
		GSNElementType[] types = GSNElementType.values();
		String[] typeNames = new String[types.length];
		
		for (int i = 0; i < types.length; i++) {
			GSNElementType type = types[i];
			
			//TODO: Use display names.
			typeNames[i] = type.toString();
		}
		
		return typeNames;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__ID)) {
			return element.getId();
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__DESCRIPTION)) {
			String description = element.getDescription();
			
			if (description == null) {
				description = "";
			}
			
			return description;
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__TYPE)) {
			return element.getType().getValue();
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__AWAY)) {
			return element.isAway() ? 1 : 0;
		}
		
		return null;
	}
	
	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (value == null) {
			return;
		}

		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__ID)) {
			String elementId = value.toString();
			element.setId(elementId);
			return;
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__DESCRIPTION)) {
			String description = value.toString();
			element.setDescription(description);
			return;
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__TYPE)) {
			try {
				int index = Integer.parseInt(value.toString());
				GSNElementType[] values = GSNElementType.values();
				
				if (index < 0 || index >= values.length) {
					System.err.println("Index " + index + " out of bounds for GSNElementType values list.");
					return;
				}
				
				GSNElementType type = values[index];
				element.setType(type);
			} catch(NumberFormatException e) {
				System.err.println("Failed to convert \"" + value + "\" to index for GSNElementType values list.");
			}
			
			return; 
		}
		
		if (id.equals(GSNPackage.GSN_CONCRETE_ELEMENT__AWAY)) {
			try {
				int index = Integer.parseInt(value.toString());
				boolean away = (index == 1);
				element.setAway(away);
			} catch(NumberFormatException e) {
				System.err.println("Failed to convert \"" + value + "\" to index for boolean values list.");
			}
			
			return;
		}
	}
}
