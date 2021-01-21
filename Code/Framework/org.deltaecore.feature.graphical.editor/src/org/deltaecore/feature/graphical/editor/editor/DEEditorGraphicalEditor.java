package org.deltaecore.feature.graphical.editor.editor;

import java.util.List;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.deltaecore.feature.graphical.base.factories.DEEditPartFactory;
import org.deltaecore.feature.graphical.editor.action.DEFeatureCreateAction;
import org.deltaecore.feature.graphical.editor.action.DEFeatureCreateInNewGroupAction;
import org.deltaecore.feature.graphical.editor.action.DEFeatureMakeMandatoryAction;
import org.deltaecore.feature.graphical.editor.action.DEFeatureMakeOptionalAction;
import org.deltaecore.feature.graphical.editor.action.DEGroupMakeAlternativeAction;
import org.deltaecore.feature.graphical.editor.action.DEGroupMakeAndAction;
import org.deltaecore.feature.graphical.editor.action.DEGroupMakeOrAction;
import org.deltaecore.feature.graphical.editor.action.DEVersionCreateAction;
import org.deltaecore.feature.graphical.editor.action.DEVersionCreatePredecessorAction;
import org.deltaecore.feature.graphical.editor.action.DEVersionCreateSuccessorAction;
import org.deltaecore.feature.graphical.editor.action.DEVersionCreateSuccessorOnNewBranchAction;
import org.deltaecore.feature.graphical.editor.factories.DEEditorEditPartFactory;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;

public class DEEditorGraphicalEditor extends DEGraphicalEditor {
	public DEEditorGraphicalEditor() {
	}
	
	@Override
	protected DEEditPartFactory createEditPartFactory() {
		return new DEEditorEditPartFactory(this);
	}
	
	@Override
	protected DEGraphicalViewer doCreateGraphicalViewer() {
		return new DEEditorGraphicalViewer(this);
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		
//		getGraphicalViewer().addDragSourceListener(new AbstractTransferDragSourceListener(getGraphicalViewer()) {
//			@Override
//			public void dragSetData(DragSourceEvent event) {
//				System.out.println("drag");
//			}
//		});
	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void registerActions(ActionRegistry actionRegistry) {
		super.registerActions(actionRegistry);
		
		//TODO: Look into getSelectionActions() and UpdateAction
		IAction directEditAction = new DirectEditAction(this);
		actionRegistry.registerAction(directEditAction);
		@SuppressWarnings("rawtypes")
		List selectionActions = getSelectionActions();
		selectionActions.add(directEditAction.getId());
		
		
		actionRegistry.registerAction(new DEFeatureCreateAction(this));
		actionRegistry.registerAction(new DEFeatureCreateInNewGroupAction(this));
		actionRegistry.registerAction(new DEVersionCreateAction(this));
		
		actionRegistry.registerAction(new DEFeatureMakeOptionalAction(this));
		actionRegistry.registerAction(new DEFeatureMakeMandatoryAction(this));
		
		actionRegistry.registerAction(new DEVersionCreatePredecessorAction(this));
		actionRegistry.registerAction(new DEVersionCreateSuccessorAction(this));
		actionRegistry.registerAction(new DEVersionCreateSuccessorOnNewBranchAction(this));
		
		actionRegistry.registerAction(new DEGroupMakeAndAction(this));
		actionRegistry.registerAction(new DEGroupMakeOrAction(this));
		actionRegistry.registerAction(new DEGroupMakeAlternativeAction(this));
	}
	
	@Override
	protected ContextMenuProvider createContextMenuProvider(GraphicalViewer viewer, ActionRegistry actionRegistry) {
		return new DEEditorContextMenuProvider(viewer, actionRegistry);
	}
	
	@Override
	protected void registerKeyboardShortcuts(KeyHandler keyHandler, ActionRegistry actionRegistry) {
		super.registerKeyboardShortcuts(keyHandler, actionRegistry);
		
		//TODO: This isn't working as expected as action is null
		IAction directEditAction = actionRegistry.getAction(GEFActionConstants.DIRECT_EDIT);
		keyHandler.put(KeyStroke.getPressed(SWT.F2, SWT.NONE), directEditAction);
	}
}
