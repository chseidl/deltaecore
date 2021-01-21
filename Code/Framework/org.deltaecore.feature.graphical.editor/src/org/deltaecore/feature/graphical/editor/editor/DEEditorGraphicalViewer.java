package org.deltaecore.feature.graphical.editor.editor;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;

public class DEEditorGraphicalViewer extends DEGraphicalViewer {
	
	public DEEditorGraphicalViewer(DEEditorGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	public DEEditorGraphicalEditor getGraphicalEditor() {
		return (DEEditorGraphicalEditor) super.getGraphicalEditor();
	}

	@Override
	protected void registerListeners() {
		super.registerListeners();
		
//		addDragSourceListener(new AbstractTransferDragSourceListener(this) {
//			@Override
//			public void dragSetData(DragSourceEvent event) {
//				System.out.println("drag");
//			}
//		});
		
//		addDropTargetListener(new AbstractTransferDropTargetListener(this) {
//			@Override
//			protected void updateTargetRequest() {
//				System.out.println("drop");
//			}
//		});
	}
}
