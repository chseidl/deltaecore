package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.views.properties.IPropertySource;


public abstract class PropertySourceEObjectEditPart extends EObjectEditPart {
	private IPropertySource propertySource;
	
	public PropertySourceEObjectEditPart(EObject object) {
		super(object);
		propertySource = null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class) {
			return getPropertySource();
		}
		
		return super.getAdapter(key);
	}
	
	protected IPropertySource getPropertySource() {
		if (propertySource == null) {
			propertySource = createPropertySource();
		}
		
		return propertySource;
	}
	
	protected abstract IPropertySource createPropertySource();
}
