package logseer.rules;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;

public class NoErrorOutstideTryCatch implements LoggingContextValidator {
	public static final String LOGGING_MUST_NOT_BE_ERROR_IF_OUTSIDE_CATCH_BLOCK = "Logging outside catch block must not be on error verbosity";
	
	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(!loggingContext.isInsideTryCatch() && 
				loggingContext.getSeverity().equals(LoggingSeverity.ERROR)
		) {
			ReportingUtils.report(LOGGING_MUST_NOT_BE_ERROR_IF_OUTSIDE_CATCH_BLOCK, loggingContext);
			LoggingIssue.createLoggingIssue(loggingContext, report, getRuleEnum(), getScopeEnum(), null);
		}
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		return LoggingRuleEnum.NO_ERROR_OUTSIDE_TRY_CATCH;
	}
	
	@Override
	public IssueScopeEnum getScopeEnum() {
		return IssueScopeEnum.INLINE;
	}
}
