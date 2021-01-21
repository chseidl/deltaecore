/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decore.resource.decore.ui;

import org.deltaecore.core.decore.DEModelElementIdentifier;
import org.eclipse.emf.ecore.EObject;

public class DecoreHoverTextProvider implements org.deltaecore.core.decore.resource.decore.IDecoreHoverTextProvider {
	
	private org.deltaecore.core.decore.resource.decore.ui.DecoreDefaultHoverTextProvider defaultProvider = new org.deltaecore.core.decore.resource.decore.ui.DecoreDefaultHoverTextProvider();
	
	public String getHoverText(EObject container, EObject referencedObject) {
		// Set option overrideHoverTextProvider to false and customize this method to
		// obtain custom hover texts.
		return defaultProvider.getHoverText(referencedObject);
	}
	
	public String getHoverText(EObject object) {
		if (object instanceof DEModelElementIdentifier) {
			DEModelElementIdentifier modelElementIdentifier = (DEModelElementIdentifier) object;
			
			String rawIdentifier = modelElementIdentifier.getRawIdentifier();
			Object value = modelElementIdentifier.getValue();
			
			String hoverText = "";
			
			hoverText += rawIdentifier + "</br>";
			hoverText += "<b>";
			
			if (value == null) {
				hoverText += "<unresolved>";
//			} else if (value instanceof EObject) {
//				EObject eObject = (EObject) value;
//				
//				//TODO: Make a more serious attempt at formatting this by using possibly existing edit projects.
//				hoverText += eObject.eClass().toString();
			} else {
				hoverText += value.toString();
			}
			
			hoverText += "</b>";
			
			return hoverText;
		}
		// Set option overrideHoverTextProvider to false and customize this method to
		// obtain custom hover texts.
		return defaultProvider.getHoverText(object);
	}
	
}
