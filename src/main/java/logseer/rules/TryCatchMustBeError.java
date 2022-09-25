package logseer.rules;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;

/**
 * If Logging call heppens inside try catch block it must be on error Verbosity.
 * In general, if 
 * @author wallacy
 *
 */
public class TryCatchMustBeError implements LoggingContextValidator {
	public static final String LOGGING_MUST_BE_ERROR_IF_INSIDE_CATCH_BLOCK = "Logging inside catch block must be on error verbosity";
	
	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(loggingContext.isInsideTryCatch() && 
				!loggingContext.getSeverity().equals(LoggingSeverity.ERROR)
		) {
			ReportingUtils.report(LOGGING_MUST_BE_ERROR_IF_INSIDE_CATCH_BLOCK, loggingContext);
			LoggingIssue.createLoggingIssue(loggingContext, report, getRuleEnum(), getScopeEnum(), null);
		}
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		return null;
	}
	
	@Override
	public IssueScopeEnum getScopeEnum() {
		// TODO Auto-generated method stub
		return null;
	}
}
