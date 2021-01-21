package org.deltaecore.feature.graphical.base.action;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import de.christophseidl.util.eclipse.ui.JFaceUtil;

public abstract class DEAction extends Action {
	private DEGraphicalEditor editor;
	
	public DEAction(DEGraphicalEditor editor) {
		this.editor = editor;
		
		initialize();
		registerListeners();
	}
	
	private void initialize() {
		setId(createID());
		setText(createText());
		
		String iconPath = createIconPath();
		
		if (iconPath != null) {
			ImageDescriptor imageDescriptor = JFaceUtil.getImageDescriptorFromClassBundle(iconPath, getClass());
			setImageDescriptor(imageDescriptor);
		}
	}
	
	private void registerListeners() {
		DEGraphicalViewer viewer = editor.getViewer();
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateEnabledState();
			}
		});
	}
	
	protected abstract String createText();
	protected abstract String createID();
	
	protected String createIconPath() {
		return null;
	}
	
	public void updateEnabledState() {
	}
	
	@Override
	public abstract void run();
	
	protected DEGraphicalEditor getEditor() {
		return editor;
	}
}
