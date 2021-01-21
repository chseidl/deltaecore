package org.deltaecore.feature.graphical.editor.editor;

import org.deltaecore.feature.graphical.base.action.DEAction;
import org.deltaecore.feature.graphical.base.action.DEExportImageAction;
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
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

public class DEEditorContextMenuProvider extends ContextMenuProvider {
	 
    private ActionRegistry actionRegistry;
 
    public DEEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry) {
        super(viewer);
        this.actionRegistry = actionRegistry;
    }
 
    @Override
    public void buildContextMenu(IMenuManager menuManager) {
        GEFActionConstants.addStandardActionGroups(menuManager);
 
        menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, actionRegistry.getAction(ActionFactory.UNDO.getId()));
        menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, actionRegistry.getAction(ActionFactory.REDO.getId()));
        
        
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEFeatureCreateAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEFeatureCreateInNewGroupAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEFeatureMakeOptionalAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEFeatureMakeMandatoryAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, new Separator());
        
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEVersionCreateAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEVersionCreatePredecessorAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEVersionCreateSuccessorAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEVersionCreateSuccessorOnNewBranchAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, new Separator());
        
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEGroupMakeAndAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEGroupMakeOrAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEGroupMakeAlternativeAction.ID));
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, new Separator());
        
        menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, actionRegistry.getAction(DEExportImageAction.ID));
        
        registerListeners(menuManager);
        updateMenuManagerEnabledState(menuManager);
    }
    
    private void registerListeners(IMenuManager menuManager) {
        menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager menuManager) {
				updateMenuManagerEnabledState(menuManager);
			}
		});
    }
    
    private void updateMenuManagerEnabledState(IMenuManager menuManager) {
		IContributionItem[] items = menuManager.getItems();
		
		if (items != null) {
			for (IContributionItem item : items) {
				if (item instanceof ActionContributionItem) {
					ActionContributionItem actionContributionItem = (ActionContributionItem) item;
					IAction rawAction = actionContributionItem.getAction();
					
					if (rawAction instanceof DEAction) {
						DEAction action = (DEAction) rawAction;
						action.updateEnabledState();
					}
					
				}
			}
		}
    }
}
