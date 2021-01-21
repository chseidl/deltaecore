package eu.vicci.ecosystem.goalstructuringnotation.gsn.graphicaleditor.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class EObjectEditPart extends AbstractGraphicalEditPart {
	private Adapter listener;
	
	public EObjectEditPart(EObject object) {
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
	
//	@SuppressWarnings("rawtypes")
//	@Override
//	public Object getAdapter(Class key) {
//		if (key == SnapToHelper.class) {
//			adaptToSnapToHelper();
//		}
//		
//		return super.getAdapter(key);
//	}
//	
//	private Object adaptToSnapToHelper() {
//		List<SnapToHelper> helpers = new ArrayList<SnapToHelper>();
//		
//		if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED))) {
//			helpers.add(new SnapToGrid(this));
//		}
//		
////		if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED))) {
////			helpers.add(new SnapToGeometry(this));
////		}
//		
//		if (helpers.size() == 0) {
//			return null;
//		} else {
//			return new CompoundSnapToHelper(helpers.toArray(new SnapToHelper[0]));
//		}
//	}
}
