package org.deltaecore.core.decore.resource.decore.ui;

import java.util.List;

import org.deltaecore.core.decore.DEDelta;
import org.deltaecore.core.variant.DEVariantCreator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorInput;

/**
 * Custom Editor that creates a partial variant to properly resolve references to elements created/deleted/modified in required delta modules.
 * 
 * @author Christoph Seidl
 */
public class DecoreEditorCustom extends DecoreEditor {
	private DEVariantCreator variantCreator;
	private IResourceChangeListener resourceChangeListener;
	
	public DecoreEditorCustom() {
		variantCreator = new DEVariantCreator();
		
		//NOTE: This strategy is too demanding and not necessary.
//		addBackgroundParsingListener(new IDecoreBackgroundParsingListener() {
//			
//			@Override
//			public void parsingCompleted(Resource resource) {
//				updatePartialVariant();
//			}
//		});
		
		resourceChangeListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				IResourceDelta delta = event.getDelta();
				try {
					class ResourceDeltaVisitor implements IResourceDeltaVisitor {
						protected ResourceSet resourceSet = getResourceSet();
						
						public boolean visit(IResourceDelta delta) {
							if (delta.getResource().getType() != IResource.FILE) {
								return true;
							}
							int deltaKind = delta.getKind();
							if (deltaKind == IResourceDelta.CHANGED && delta.getFlags() != IResourceDelta.MARKERS) {
								URI platformURI = URI.createPlatformResourceURI(delta.getFullPath().toString(), true);
								Resource changedResource = resourceSet.getResource(platformURI, false);
								
								if (changedResource != null) {
									onResourceChanged(changedResource);
								}
							}
							
							return true;
						}
					}
					
					ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
					delta.accept(visitor);
				} catch (CoreException exception) {
					org.deltaecore.core.decore.resource.decore.ui.DecoreUIPlugin.logError("Unexpected Error: ", exception);
				}
			}
		};
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	protected void doSetInput(IEditorInput editorInput) throws CoreException {
		super.doSetInput(editorInput);
		
		updatePartialVariant();
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
		super.dispose();
	}

	//Update the partial variant if any of the related resources (required delta modules, modified models) changes.
	protected void onResourceChanged(Resource changedResource) {
//		Resource currentResource = getResource();

//		System.out.println("----> " + currentResource.getURI().lastSegment() + " detected change in " + changedResource.getURI().lastSegment());
		
		//NOTE: Also update the partial variant if this resource changes as the delta itself is interpreted to see if names can be resolved.
//		if (changedResource == currentResource) {
//			//This does not affect the partial variant.
//			return;
//		}
		
		//It seems that only connected resources trigger this event.
		updatePartialVariant();
	}
	
	protected void updatePartialVariant() {
		Resource resource = getResource();
		List<EObject> contents = resource.getContents();
		DEDelta delta = (DEDelta) contents.get(0);
		
		variantCreator = new DEVariantCreator();
		variantCreator.updatePartialVariantAsBasisForDelta(delta);
	}
}
