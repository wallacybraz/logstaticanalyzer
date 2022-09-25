package logseer.report.metrics;

import java.util.HashMap;
import java.util.Map;

import logseer.core.LoggingContext;
import logseer.core.LoggingSeverity;
import logseer.report.Metric;

public class GeneralVerbosityDistribution extends Metric<Map<LoggingSeverity, Integer>> {
	public GeneralVerbosityDistribution() {
		this.setMetricEnum(MetricEnum.GVD);
		this.setValue(new HashMap<LoggingSeverity, Integer>());
		this.initDistribution();
	}
	
	private void initDistribution() {
		this.getValue().put(LoggingSeverity.INFO, 0);
		this.getValue().put(LoggingSeverity.WARNING, 0);
		this.getValue().put(LoggingSeverity.DEBUG, 0);
		this.getValue().put(LoggingSeverity.ERROR, 0);
		this.getValue().put(LoggingSeverity.FATAL, 0);
	}
	
	public void update(LoggingContext loggingContext) {
		if(loggingContext.getSeverity() != null) {
			Integer actualValue = this.getValue().get(loggingContext.getSeverity());
			this.getValue().put(loggingContext.getSeverity(), actualValue + 1);
		}
	}

	@Override
	public String toString() {
		return "VerbosityDistribution [getName()=" + getMetricEnum().description + ", getValue()=" + getValue() + "]";
	}
}
