package de.christophseidl.batcheditmanifests;

public class BatchEditManifestsInformation {
	
	private String projectNamePattern;
	private String bundleVersion;
	private String bundleVendor;
	private String bundleRequiredExecutionEnvironment;
	
	public BatchEditManifestsInformation() {
	}

	public String getProjectNamePattern() {
		return projectNamePattern;
	}

	public void setProjectNamePattern(String projectNamePattern) {
		this.projectNamePattern = projectNamePattern;
	}
	
	public String getBundleVersion() {
		return bundleVersion;
	}

	public void setBundleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}

	public String getBundleVendor() {
		return bundleVendor;
	}

	public void setBundleVendor(String bundleVendor) {
		this.bundleVendor = bundleVendor;
	}

	public String getBundleRequiredExecutionEnvironment() {
		return bundleRequiredExecutionEnvironment;
	}

	public void setBundleRequiredExecutionEnvironment(String bundleRequiredExecutionEnvironment) {
		this.bundleRequiredExecutionEnvironment = bundleRequiredExecutionEnvironment;
	}
}
