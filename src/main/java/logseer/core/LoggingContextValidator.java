package logseer.core;

import logseer.report.AnalysisReport;

public interface LoggingContextValidator {
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report);
	
	public LoggingRuleEnum getRuleEnum();
	
	public IssueScopeEnum getScopeEnum();
}
