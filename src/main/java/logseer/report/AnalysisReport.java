package logseer.report;

import java.util.HashMap;
import java.util.Map;

import logseer.core.File;
import logseer.core.LoggingContext;
import logseer.report.metrics.MetricEnum;

public class AnalysisReport {
	private Map<MetricEnum, Metric<?>> generalMetrics = new HashMap<MetricEnum, Metric<?>>();

	private Map<File, FileAnalysis> filesAnalysis = new HashMap<File, FileAnalysis>();

	public Map<MetricEnum, Metric<?>> getGeneralMetrics() {
		return generalMetrics;
	}

	public void setGeneralMetrics(Map<MetricEnum, Metric<?>> generalMetrics) {
		this.generalMetrics = generalMetrics;
	}

	public Map<File, FileAnalysis> getFilesAnalysis() {
		return filesAnalysis;
	}

	public void setFilesAnalysis(Map<File, FileAnalysis> filesAnalysis) {
		this.filesAnalysis = filesAnalysis;
	}

	public void createFileAnalasysIfNotExists(File file) {
		if(getFilesAnalysis().get(file) == null) {
			FileAnalysis fileAnalysis = new FileAnalysis(file);
			this.filesAnalysis.put(file, fileAnalysis);
		}
	}
}
