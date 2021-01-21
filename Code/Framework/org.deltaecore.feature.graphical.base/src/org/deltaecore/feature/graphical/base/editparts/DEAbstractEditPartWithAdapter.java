package org.deltaecore.feature.graphical.base.editparts;

import java.util.List;

import org.deltaecore.feature.graphical.base.editor.DEGraphicalEditor;
import org.deltaecore.feature.graphical.base.util.DEAbstractAdapter;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;

public abstract class DEAbstractEditPartWithAdapter extends DEAbstractEditPart {
	private DEAbstractAdapter adapter;
	
	public DEAbstractEditPartWithAdapter(DEGraphicalEditor graphicalEditor) {
		super(graphicalEditor);
		
		adapter = createAdapter(this);
	}
	
	protected abstract DEAbstractAdapter createAdapter(DEAbstractEditPartWithAdapter editPart);
	
	@Override
	public void activate() {
		if (!isActive()) {
			EObject eObject = getModel();
			List<Adapter> adapters = eObject.eAdapters();
			adapters.add(adapter);
		}
		
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			EObject eObject = getModel();
			List<Adapter> adapters = eObject.eAdapters();
			adapters.remove(adapter);
		}

		super.deactivate();
	}
	
	@Override
	public EObject getModel() {
		return (EObject) super.getModel();
	}
}
