package org.deltaecore.feature.graphical.configurator.editor;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalViewer;

public class DEConfiguratorGraphicalViewer extends DEGraphicalViewer {
	public DEConfiguratorGraphicalViewer(DEConfiguratorGraphicalEditor editor) {
		super(editor);
	}
	
	@Override
	public DEConfiguratorGraphicalEditor getGraphicalEditor() {
		return (DEConfiguratorGraphicalEditor) super.getGraphicalEditor();
	}
}
