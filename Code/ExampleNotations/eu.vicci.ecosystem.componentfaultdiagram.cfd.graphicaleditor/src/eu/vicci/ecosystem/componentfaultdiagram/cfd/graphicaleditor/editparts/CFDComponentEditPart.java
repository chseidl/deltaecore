package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDComponentFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDNamedElementFigure;

public class CFDComponentEditPart extends CFDNamedElementEditPart {

	public CFDComponentEditPart(CFDComponent component) {
		super(component);
	}
	
	@Override
	protected Class<? extends CFDElement> getModelType() {
		return CFDComponent.class;
	}

	@Override
	protected CFDNamedElementFigure createElementFigure() {
		return new CFDComponentFigure();
	}

	@Override
	protected CFDComponentFigure getElementFigure() {
		return (CFDComponentFigure) super.getElementFigure();
	}
	
	@Override
	public IFigure getContentPane() {
		CFDComponentFigure figure = getElementFigure();
		
		return figure.getElementArea();
	}
	
	@Override
	protected CFDComponent getElement() {
		return (CFDComponent) super.getElement();
	}
	
	@Override
	protected List<EObject> getModelChildren() {
		List<EObject> children = super.getModelChildren();
		
		CFDComponent component = getElement();
		
		children.addAll(component.getElements());
		
		return children;
	}
	
	@Override
	protected List<CFDOutPort> getOutPorts() {
		CFDComponent component = getElement();
		List<CFDOutPort> outPorts = new ArrayList<CFDOutPort>();
		outPorts.addAll(component.getOutPorts());
		return outPorts;
	}
	
	@Override
	protected List<CFDInPort> getInPorts() {
		CFDComponent component = getElement();
		List<CFDInPort> inPorts = new ArrayList<CFDInPort>();
		inPorts.addAll(component.getInPorts());
		return inPorts;
	}
}
