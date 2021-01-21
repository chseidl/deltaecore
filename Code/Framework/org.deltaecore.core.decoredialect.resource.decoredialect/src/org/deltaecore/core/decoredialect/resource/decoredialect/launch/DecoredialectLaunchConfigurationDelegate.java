/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.deltaecore.core.decoredialect.resource.decoredialect.launch;

import org.deltaecore.core.decoredialect.DEDeltaDialect;
import org.deltaecore.core.generation.DECodeGenerator;
import org.eclipse.emf.ecore.EObject;

/**
 * A class that handles launch configurations.
 */
public class DecoredialectLaunchConfigurationDelegate extends org.eclipse.debug.core.model.LaunchConfigurationDelegate {
	
	/**
	 * The URI of the resource that shall be launched.
	 */
	public final static String ATTR_RESOURCE_URI = "uri";
	
	public void launch(org.eclipse.debug.core.ILaunchConfiguration configuration, String mode, org.eclipse.debug.core.ILaunch launch, org.eclipse.core.runtime.IProgressMonitor monitor) throws org.eclipse.core.runtime.CoreException {
		DecoredialectLaunchConfigurationHelper helper = new DecoredialectLaunchConfigurationHelper();
		EObject root = helper.getModelRoot(configuration);
		
		if (root instanceof DEDeltaDialect) {
			DEDeltaDialect dialect = (DEDeltaDialect) root;
			DECodeGenerator generator = new DECodeGenerator(dialect);
			generator.generate();
			
//			Display display = Display.getCurrent();
//			Shell shell = display.getActiveShell()();
//			
//			MessageDialog.openInformation(shell, "Delta Ecore Dialect Creation Completed", "Delta Ecore dialect created successfully.");
		}
		
//		helper.launch(configuration, mode, launch, monitor);
	}

}
