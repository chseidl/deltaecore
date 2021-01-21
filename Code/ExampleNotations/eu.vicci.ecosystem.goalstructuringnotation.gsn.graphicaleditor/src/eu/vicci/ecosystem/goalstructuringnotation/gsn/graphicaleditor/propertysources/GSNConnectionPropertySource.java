package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.propertysources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnection;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNPackage;

public class GSNConnectionPropertySource implements IPropertySource {
	private GSNConnection connection;
	private String[] connectionTypeNames;
	
	public GSNConnectionPropertySource(GSNConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
		
		connectionTypeNames = createConnectionTypeNames();
		
		properties.add(new ComboBoxPropertyDescriptor(GSNPackage.GSN_CONNECTION__TYPE, "Type", connectionTypeNames));

		return properties.toArray(new IPropertyDescriptor[0]);
	}

	private String[] createConnectionTypeNames() {
		GSNConnectionType[] types = GSNConnectionType.values();
		String[] typeNames = new String[types.length];
		
		for (int i = 0; i < types.length; i++) {
			GSNConnectionType type = types[i];
			
			//TODO: Use display names.
			typeNames[i] = type.toString();
		}
		
		return typeNames;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(GSNPackage.GSN_CONNECTION__TYPE)) {
			return connection.getType().getValue();
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

		if (id.equals(GSNPackage.GSN_CONNECTION__TYPE)) {
			try {
				int index = Integer.parseInt(value.toString());
				GSNConnectionType[] values = GSNConnectionType.values();
				
				if (index < 0 || index >= values.length) {
					System.err.println("Index " + index + " out of bounds for GSNConnectionType values list.");
					return;
				}
				
				GSNConnectionType type = values[index];
				connection.setType(type);
			} catch(NumberFormatException e) {
				System.err.println("Failed to convert \"" + value + "\" to index for GSNConnectionType values list.");
			}
			
			return; 
		}
	}
}
