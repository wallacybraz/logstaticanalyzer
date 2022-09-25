package logseer.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logseer.core.File;
import logseer.report.metrics.MetricEnum;

public class FileAnalysis {
	public FileAnalysis(File file) {
		this.file = file;
	}
	
	List<LoggingIssue> issues = new ArrayList<LoggingIssue>();
	
	Map<MetricEnum, Metric<?>> metrics = new HashMap<MetricEnum, Metric<?>>();
	
	private File file;
	
	public List<LoggingIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<LoggingIssue> issues) {
		this.issues = issues;
	}

	public Map<MetricEnum, Metric<?>> getMetrics() {
		return metrics;
	}

	public void setMetrics(Map<MetricEnum, Metric<?>> metrics) {
		this.metrics = metrics;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}	
}
