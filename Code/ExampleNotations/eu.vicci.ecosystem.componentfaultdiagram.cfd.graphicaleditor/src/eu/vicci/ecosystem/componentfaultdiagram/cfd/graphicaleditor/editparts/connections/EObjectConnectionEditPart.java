package eu.vicci.ecosystem.componentfaultdiagram.cfd.graphicaleditor.editparts.connections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

//NOTE: duplicate of EObjectConnectionEdit part due to lack of multiple inheritance and different base class.
public abstract class EObjectConnectionEditPart extends AbstractConnectionEditPart {
	private Adapter listener;
	
	public EObjectConnectionEditPart(EObject object) {
		setModel(object);
		
		//Register listener for changes.
		listener = new Adapter() {
			@Override
			public void notifyChanged(Notification notification) {
				refresh();
			}

			@Override
			public Notifier getTarget() {
				return getElement();
			}

			@Override
			public void setTarget(Notifier newTarget) {
			}

			@Override
			public boolean isAdapterForType(Object type) {
				return type.equals(getModelType());
			}
		};
	}
	
	protected abstract Class<? extends EObject> getModelType();
	
	protected EObject getElement() {
		return (EObject) getModel();
	}
	
	@Override
	public void activate() {
		if (!isActive()) {
			EObject element = getElement();
			element.eAdapters().add(listener);
		}
		
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			EObject element = getElement();
			element.eAdapters().remove(listener);
		}

		super.deactivate();
	}
}
