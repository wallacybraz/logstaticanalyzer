package logseer.report;

import logseer.core.LoggingContext;

public interface UpdatableMetric {
	public void update(LoggingContext loggingContext);
}
