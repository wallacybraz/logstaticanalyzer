package logseer.rules;

import java.util.List;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;
import spoon.reflect.code.CtThrow;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * This rule checks if an re-throw happens on a catch block that has an ERROR verbosity LI. 
 * This is a very well known logging anti-pattern.
 * @author wallacy
 *
 */ 
public class NoRethrowAfterLogging implements LoggingContextValidator {
	public static String NO_EXCEPTION_RETHROW_AND_LOGGING = "No rethrow after logging.";

	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(loggingContext.isInsideTryCatch()) {
			List<CtThrow> ctThrows = loggingContext.getCatchBlock().getElements(new TypeFilter<CtThrow>(CtThrow.class));
			
			if(ctThrows != null && ctThrows.size() > 0) {
				ReportingUtils.report(NO_EXCEPTION_RETHROW_AND_LOGGING, loggingContext);
				LoggingIssue.createLoggingIssue(loggingContext, report, getRuleEnum(), getScopeEnum(), null);
			}
		}
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		return LoggingRuleEnum.NO_RETHROW_AFTER_LOGGING;
	}
	
	@Override
	public IssueScopeEnum getScopeEnum() {
		return IssueScopeEnum.CATCH;
	}
}
