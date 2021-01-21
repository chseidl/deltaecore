package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDInPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDOutPort;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports.CFDInPortEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.ports.CFDOutPortEditPart;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editpolicies.CFDElementComponentEditPolicy;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.CFDElementFigure;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.figures.ports.CFDPortContainerFigure;

public abstract class CFDElementEditPart extends EObjectEditPart {
	private CFDElementFigure elementFigure;
	
	public CFDElementEditPart(CFDElement element) {
		super(element);
		
		elementFigure = null;
	}
	
	@Override
	protected CFDElement getElement() {
		return (CFDElement) super.getElement();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CFDElementComponentEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		CFDElement element = getElement();
		elementFigure = createElementFigure();
		elementFigure.update(element);
		
		return elementFigure;
	}

	protected abstract CFDElementFigure createElementFigure();
	
	protected CFDElementFigure getElementFigure() {
		return (CFDElementFigure) elementFigure;
	}
	
	@Override
	protected List<EObject> getModelChildren() {
		List<EObject> children = new ArrayList<EObject>();
		
		List<CFDOutPort> outPorts = getOutPorts();
		List<CFDInPort> inPorts = getInPorts();
		
		if (outPorts != null) {
			children.addAll(outPorts);
		}
		
		if (inPorts != null) {
			children.addAll(inPorts);
		}
		
		return children;
	}
	
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (childEditPart instanceof CFDOutPortEditPart) {
			CFDOutPortEditPart outPortEditPart = (CFDOutPortEditPart) childEditPart;
//			outPortEditPart.getP
			
			IFigure childFigure = outPortEditPart.getFigure();
			
			CFDElementFigure figure = getElementFigure();
			CFDPortContainerFigure outPortCompartment = figure.getOutPortCompartment();
			
			outPortCompartment.add(childFigure);
			
			return;
		}
		
		if (childEditPart instanceof CFDInPortEditPart) {
			CFDInPortEditPart inPortEditPart = (CFDInPortEditPart) childEditPart;
			IFigure childFigure = inPortEditPart.getFigure();
			
			CFDElementFigure figure = getElementFigure();
			CFDPortContainerFigure inPortCompartment = figure.getInPortCompartment();
			
			inPortCompartment.add(childFigure);
			
			return;
		}
		
		//Index seems to be off as not all model children are added to this container.
		//Correct it to at least have a plausible value (still not necessarily the proper one).
		IFigure contentPane = getContentPane();
		
		int n = contentPane.getChildren().size();
		int correctedIndex = Math.max(n - 1, 0);
		
		super.addChildVisual(childEditPart, correctedIndex);
	}
	
	@Override
	protected void refreshVisuals() {
		CFDElementFigure figure = getElementFigure();
		CFDElement element = getElement();
		
		GraphicalEditPart parentEditPart = (GraphicalEditPart) getParent();
		
		Rectangle constraints = element.getConstraints();
		
		//Ensure that elements are not invisible due to missing bounds.
		if (constraints == null) {
			constraints = new Rectangle(50, 50, 50, 50);
		}
		
		parentEditPart.setLayoutConstraint(this, figure, constraints);
			  
		elementFigure.update(element);
		
		super.refreshVisuals();
	}
	
	@Override
	protected void refreshChildren() {
		CFDElementFigure figure = getElementFigure();
		
		List<CFDOutPort> outPorts = getOutPorts();
		CFDPortContainerFigure outPortCompartment = figure.getOutPortCompartment();
		outPortCompartment.update(outPorts);
		
		List<CFDInPort> inPorts = getInPorts();
		CFDPortContainerFigure inPortCompartment = figure.getInPortCompartment();
		inPortCompartment.update(inPorts);
		
		super.refreshChildren();
	}
	
	protected abstract List<CFDOutPort> getOutPorts();
	protected abstract List<CFDInPort> getInPorts();
}
