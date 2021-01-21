package org.deltaecore.installer;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.mwe2.runtime.Mandatory;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.ui.CreateResourcePluginsJob;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsResource;
import org.emftext.sdk.concretesyntax.resource.cs.util.CsEclipseProxy;
import org.emftext.sdk.ui.jobs.UICreateResourcePluginsJob;
import org.emftext.sdk.ui.jobs.UIGenerationContext;
import org.emftext.sdk.ui.jobs.WorkspaceConnector;
import org.emftext.sdk.ui.jobs.WorkspaceMarker;

import de.christophseidl.util.eclipse.ResourceUtil;

public class EMFTextGenerator implements IWorkflowComponent {
	private static Logger log = Logger.getLogger(EMFTextGenerator.class);
	
	private String concreteSyntax;
	
	public EMFTextGenerator() {
		concreteSyntax = null;
	}
	
	@Override
	public void invoke(IWorkflowContext context1) {
		log.info("Generating EMFText code for " + concreteSyntax);

		//TODO: EMFText code generation
		
//		ResourcesPlugin.getWorkspace().getRoot().getF
		
		//TODO: Have to get file from URI
		IFile concreteSyntaxFile = ResourceUtil.getLocalFile(concreteSyntax);
		
		
		final CsResource csResource = new CsEclipseProxy().getResource(concreteSyntaxFile);
		final ConcreteSyntax concreteSyntax = (ConcreteSyntax) csResource.getContents().get(0);
		GenerationContext context = new UIGenerationContext(new WorkspaceConnector(), null, concreteSyntax);
		
		try {
			CreateResourcePluginsJob.Result result = new UICreateResourcePluginsJob().run(context, new WorkspaceMarker(), new NullProgressMonitor());
			
			if (result != CreateResourcePluginsJob.Result.SUCCESS) {
				System.out.println("Error");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void postInvoke() {
	}

	@Override
	public void preInvoke() {
	}

	public String getConcreteSyntax() {
		return concreteSyntax;
	}

	@Mandatory
	public void setConcreteSyntax(String concreteSyntax) {
		this.concreteSyntax = concreteSyntax;
	}
	

}