package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDBasicEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDComponent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDConnection;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDElement;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDGate;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.CFDIntermediateEvent;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.creationfactories.CFDConnectionCreationFactory;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.creationfactories.CFDElementCreationFactory;
import eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.tools.CreationAndDirectEditingTool;

public class CFDGraphicalEditorPalette extends PaletteRoot {

	private PaletteGroup controlsGroup;
	private PaletteGroup elementCreationGroup;
	private PaletteGroup connectionCreationGroup;

	public CFDGraphicalEditorPalette() {
		addGroups();
		addSelectionTool();
		
		addElementCreateTools();
		addConnectionCreateTools();
	}

	private void addSelectionTool() {
		SelectionToolEntry entry = new SelectionToolEntry();
		controlsGroup.add(entry);
		setDefaultEntry(entry);
	}

	private void addGroups() {
		controlsGroup = new PaletteGroup("Controls");
		add(controlsGroup);
		
		elementCreationGroup = new PaletteGroup("Element Creation");
		add(elementCreationGroup);
		
		connectionCreationGroup = new PaletteGroup("Connection Creation");
		add(connectionCreationGroup);
	}

	private void addElementCreateTools() {
		addElementCreateTool("Create Component", "Create a new component", null, CFDComponent.class);
		addElementCreateTool("Create Basic Event", "Create a new basic event", null, CFDBasicEvent.class);
		addElementCreateTool("Create Intermediate Event", "Create a new intermediate event", null, CFDIntermediateEvent.class);
		addElementCreateTool("Create Gate", "Create a new gate", null, CFDGate.class, false);
		
	}
	
	private void addConnectionCreateTools() {
		addConnectionCreateTool("Create Causal Connection", "Create a new causal connection from an out port to an in port on the same level", null, CFDConnection.class);
		
		//TODO: Add populating connection
	}
	
	private void addElementCreateTool(String toolName, String toolDescription, String iconFilename, Class<? extends CFDElement> creationClass) {
		addElementCreateTool(toolName, toolDescription, iconFilename, creationClass, true);
	}
	
	private void addElementCreateTool(String toolName, String toolDescription, String iconFilename, Class<? extends CFDElement> creationClass, boolean performImmediateDirectEditing) {
		CreationFactory creationFactory = new CFDElementCreationFactory(creationClass);
		CreationToolEntry entry = new CreationToolEntry(toolName, toolDescription, creationFactory, null, null);
		
		if (iconFilename != null) {
			ImageDescriptor smallIconImageDescriptor = loadIconImageDescriptor(iconFilename);
			entry.setSmallIcon(smallIconImageDescriptor);
		}
		
		if (performImmediateDirectEditing) {
			entry.setToolClass(CreationAndDirectEditingTool.class);
		}
		
		elementCreationGroup.add(entry);
	}

	private void addConnectionCreateTool(String toolName, String toolDescription, String iconFilename, Class<? extends CFDConnection> creationClass) {
		CreationFactory creationFactory = new CFDConnectionCreationFactory(creationClass);
		ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(toolName, toolDescription, creationFactory, null, null);
		
		if (iconFilename != null) {
			ImageDescriptor smallIconImageDescriptor = loadIconImageDescriptor(iconFilename);
			entry.setSmallIcon(smallIconImageDescriptor);
		}
		
	    connectionCreationGroup.add(entry);
	}
	
	private static ImageDescriptor loadIconImageDescriptor(String file) {
		Bundle bundle = FrameworkUtil.getBundle(CFDGraphicalEditorPalette.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		return ImageDescriptor.createFromURL(url);
	}
}
