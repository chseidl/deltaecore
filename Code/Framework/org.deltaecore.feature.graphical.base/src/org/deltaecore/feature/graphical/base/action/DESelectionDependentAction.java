package org.deltaecore.feature.graphical.base.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.deltaecore.feature.graphical.base.editparts.DEFeatureModelEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;

public abstract class DESelectionDependentAction extends DEAction {
	
	private boolean allowMultipleSelection;
	
	public DESelectionDependentAction(DEGraphicalEditor editor) {
		this(editor, false);
	}
	
	public DESelectionDependentAction(DEGraphicalEditor editor, boolean allowMultipleSelection) {
		super(editor);
		this.allowMultipleSelection = allowMultipleSelection;
		
		updateEnabledState();
	}
	
	@Override
	public void run() {
		List<Object> acceptedModels = getAcceptedModels();
		
		for (Object acceptedModel : acceptedModels) {
			execute(acceptedModel);
			
			if (!allowMultipleSelection) {
				//Only perform once
				return;
			}
		}
	}
	
	protected abstract void execute(Object acceptedModel);
	
	protected List<?> getSelectedEditParts() {
		DEGraphicalEditor editor = getEditor();
		DEGraphicalViewer viewer = editor.getViewer();
		List<?> selectedEditParts = viewer.getSelectedEditParts();
		
		if (selectedEditParts != null && !selectedEditParts.isEmpty()) {
			return selectedEditParts;
		}

		DEFeatureModelEditPart featureModelEditPart = findFeatureModelEditPart();
		
		if (featureModelEditPart != null) {
			return Collections.singletonList(featureModelEditPart);
		}
		
		return null;
	}
	
	private DEFeatureModelEditPart findFeatureModelEditPart() {
		DEGraphicalEditor editor = getEditor();
		DEGraphicalViewer viewer = editor.getViewer();
		RootEditPart rootEditPart = viewer.getRootEditPart();
		List<?> children = rootEditPart.getChildren();
		
		for (Object child : children) {
			if (child instanceof DEFeatureModelEditPart) {
				return (DEFeatureModelEditPart) child;
			}
		}
		
		return null;
	}
	
	private List<Object> getAcceptedModels() {
		List<Object> acceptedModels = new ArrayList<Object>();
		List<?> editParts = getSelectedEditParts();
		
		for (Object rawEditPart : editParts) {
			if (rawEditPart instanceof EditPart) {
				EditPart editPart = (EditPart) rawEditPart;
				Object selectedModel = editPart.getModel();
				
				if (acceptsSelectedModel(selectedModel)) {
					acceptedModels.add(selectedModel);
				}
			}
		}
		
		return acceptedModels;
	}
	
	protected abstract boolean acceptsSelectedModel(Object selectedModel);

	@Override
	public void updateEnabledState() {
		boolean enabled = determineEnabledState();
		setEnabled(enabled);
	}
	
	protected boolean determineEnabledState() {
		List<?> editParts = getSelectedEditParts();
		
		if (editParts == null || editParts.isEmpty()) {
			return false;
		}
		
		if (!allowMultipleSelection && editParts.size() != 1) {
			return false;
		}
		
		List<Object> acceptedModels = getAcceptedModels();
		
		if (acceptedModels.isEmpty()) {
			return false;
		}
		
		return true;
	}
}
