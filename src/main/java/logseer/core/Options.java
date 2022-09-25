package logseer.core;

import java.util.ArrayList;
import java.util.List;

import logseer.libraries.LoggingLibraryEnum;

public class Options {
	private String pathDir;
	private String outputDir;
	private List<LoggingLibraryEnum> libraries = new ArrayList<LoggingLibraryEnum>();
	
	public String getPathDir() {
		return pathDir;
	}
	public void setPathDir(String pathDir) {
		this.pathDir = pathDir;
	}
	public List<LoggingLibraryEnum> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<LoggingLibraryEnum> libraries) {
		this.libraries = libraries;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
}
