package org.deltaecore.feature.graphical.base.action;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;

public class DEExportImageAction extends DEAction {

	public static final String ID = DEExportImageAction.class.getCanonicalName();
	
	public DEExportImageAction(DEGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	protected String createText() {
		return "Export Image";
	}

	@Override
	protected String createID() {
		return ID;
	}
	
	@Override
	protected String createIconPath() {
		return "icons/ActionExportImage.png";
	}
	
	@Override
	public void run() {
		DEGraphicalEditor editor = getEditor();
		DEGraphicalViewer viewer = editor.getViewer();
		IFile imageFile = viewer.exportImage();
		IPath imageFilePath = imageFile.getFullPath();
		
		IWorkbenchPartSite site = editor.getSite();
		Shell shell = site.getShell();
		
		String title = "Export Image";
		String message = "Exported image to \"" + imageFilePath + "\".";
				
		MessageDialog.openInformation(shell, title, message);
	}
}
