package de.christophseidl.batcheditmanifests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import eu.vicci.ecosystem.util.eclipse.resourcehandlers.ManifestHandler;

public class BatchEditManifestsJob extends Job {
	private BatchEditManifestsInformation information;
	private String report;
	
	public BatchEditManifestsJob(BatchEditManifestsInformation information) {
		super("Batch Edit Manifests");
		
		this.information = information;
		report = "";
		
		setPriority(SHORT);
	}
	
	private static String preprocessRawPattern(String rawPattern) {
		rawPattern = rawPattern.replaceAll("\\.", "\\\\.");
		rawPattern = rawPattern.replaceAll("\\*", "(.*)");
		rawPattern = rawPattern.replaceAll("\\?", ".");
		
		return rawPattern;
	}
	
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (information == null) {
			return new Status(IStatus.ERROR, BatchEditManifestsPlugin.PLUGIN_ID, "No batch edit manifest information provided.");
		}
		
		try {
			String rawProjectNamePattern = information.getProjectNamePattern();
			String preprocessedRawProjectNamePattern = preprocessRawPattern(rawProjectNamePattern);
			Pattern projectNamePattern = Pattern.compile(preprocessedRawProjectNamePattern);
			
			String bundleVersion = information.getBundleVersion();
			String bundleVendor = information.getBundleVendor();
			String bundleRequiredExecutionEnvironment = information.getBundleRequiredExecutionEnvironment();
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			
			IProject[] projects = workspaceRoot.getProjects();
			
			if (projects != null) {
				int numberOfProjects = projects.length;
				monitor.beginTask("Processing " + numberOfProjects + " project(s)", numberOfProjects);
				
				for (IProject project : projects) {
					
					try {
						String projectName = project.getName();
						monitor.setTaskName("Processing " + projectName);
						Matcher matcher = projectNamePattern.matcher(projectName);
						
						if (matcher.matches()) {
							report += "Project \"" + projectName + "\" matches." + System.lineSeparator();
							
							
							boolean projectWasOpen = true;
							
							if (!project.isOpen()) {
								projectWasOpen = false;
								project.open(null);
								report += "Opened project \"" + projectName + "\" for processing." + System.lineSeparator();
							}
							
							
							//TODO: Find constant
							if (project.hasNature("org.eclipse.pde.PluginNature")) {
								IFile manifestFile = project.getFile("META-INF/MANIFEST.MF");
								
								if (!manifestFile.exists()) {
									report += "ERROR: Project \"" + projectName + "\" has no manifest file." + System.lineSeparator();
									continue;
								}
								
								ManifestHandler manifestHandler = new ManifestHandler(manifestFile);
								
								if (bundleVersion != null) {
									manifestHandler.setBundleVersion(bundleVersion);
								}
								
								if (bundleVendor != null) {
									manifestHandler.setBundleVendor(bundleVendor);
								}
								
								if (bundleRequiredExecutionEnvironment != null) {
									manifestHandler.setRequiredExecutionEnvironment(bundleRequiredExecutionEnvironment);
								}
								
								boolean saveSuccess = manifestHandler.save();
								
								if (!saveSuccess) {
									report += "ERROR: Problem with saving manifest of project \"" + projectName + "\"." + System.lineSeparator();
								}
							}
							
							if (!projectWasOpen) {
								project.close(null);
								report += "Closed project \"" + projectName + "\"." + System.lineSeparator();
							}
						}
					} catch(Exception e) {
						report += "ERROR: " + e.getMessage();
						e.printStackTrace();
					}
					
					monitor.worked(1);
				}
			}
		
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}

	public String getReport() {
		return report;
	}
}
