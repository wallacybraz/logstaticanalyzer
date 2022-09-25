package logseer.core;

import logseer.report.AnalysisReport;
import spoon.reflect.code.CtInvocation;

public interface LoggingContextCaracterizer {
	LoggingContext characterizeLoggingInvocation(CtInvocation<?> inv, AnalysisReport report);
}
