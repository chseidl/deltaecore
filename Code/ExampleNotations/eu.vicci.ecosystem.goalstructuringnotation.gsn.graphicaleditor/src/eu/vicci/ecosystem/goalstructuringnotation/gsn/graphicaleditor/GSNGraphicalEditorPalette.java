package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor;

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

import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNConnectionType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.GSNElementType;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory.GSNConnectionCreationFactory;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.creationfactory.GSNElementCreationFactory;
import eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.tools.CreationAndDirectEditingTool;

public class GSNGraphicalEditorPalette extends PaletteRoot {

	private PaletteGroup controlsGroup;
	private PaletteGroup elementCreationGroup;
	private PaletteGroup connectionCreationGroup;

	public GSNGraphicalEditorPalette() {
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
		addElementCreateTool("Goal", "Create a new goal", "GoalCreateTool.gif", GSNElementType.GOAL);
		addElementCreateTool("Strategy", "Create a new strategy", "StrategyCreateTool.gif", GSNElementType.STRATEGY);
		addElementCreateTool("Assumption", "Create a new assumption", "AssumptionCreateTool.gif", GSNElementType.ASSUMPTION);
		addElementCreateTool("Justification", "Create a new justification", "JustificationCreateTool.gif", GSNElementType.JUSTIFICATION);
		addElementCreateTool("Solution", "Create a new solution", "SolutionCreateTool.gif", GSNElementType.SOLUTION);
		addElementCreateTool("Context", "Create a new context", "ContextCreateTool.gif", GSNElementType.CONTEXT);
	}
	
	private void addConnectionCreateTools() {
		addConnectionCreateTool("Solved-by Connection", "Create a new solved-by connection", "SolvedByConnectionCreateTool.gif", GSNConnectionType.SOLVED_BY);
		addConnectionCreateTool("In-context-of Connection", "Create a new in-context-of connection", "InContextOfConnectionCreateTool.gif", GSNConnectionType.IN_CONTEXT_OF);
	}
	
	private void addElementCreateTool(String toolName, String toolDescription, String iconFilename, GSNElementType type) {
		CreationFactory creationFactory = new GSNElementCreationFactory(type);
		CreationToolEntry entry = new CreationToolEntry(toolName, toolDescription, creationFactory, null, null);
		
		ImageDescriptor smallIconImageDescriptor = loadIconImageDescriptor(iconFilename);
		entry.setSmallIcon(smallIconImageDescriptor);
		
		entry.setToolClass(CreationAndDirectEditingTool.class);
		elementCreationGroup.add(entry);
	}

	private void addConnectionCreateTool(String toolName, String toolDescription, String iconFilename, GSNConnectionType type) {
		CreationFactory creationFactory = new GSNConnectionCreationFactory(type);
		ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(toolName, toolDescription, creationFactory, null, null);
		
		ImageDescriptor smallIconImageDescriptor = loadIconImageDescriptor(iconFilename);
		entry.setSmallIcon(smallIconImageDescriptor);
		
	    connectionCreationGroup.add(entry);
	}
	
	private static ImageDescriptor loadIconImageDescriptor(String file) {
		Bundle bundle = FrameworkUtil.getBundle(GSNGraphicalEditorPalette.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		return ImageDescriptor.createFromURL(url);
	}
}
