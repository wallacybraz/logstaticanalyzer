package logseer.rules;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtVariableAccess;

public class ExceptionNotLogged  implements LoggingContextValidator {
	public static final String ERROR_MESSAGE_EXCEPTION_NOT_LOGGED = "Error Logging inside catch block must log caught exception";
	public static Logger LOGGER = LoggerFactory.getLogger(ExceptionNotLogged.class);
	
	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(loggingContext.isInsideTryCatch() && 
				(loggingContext.getSeverity().equals(LoggingSeverity.DEBUG) || loggingContext.getSeverity().equals(LoggingSeverity.ERROR))
		) {
			CtCatchVariable<?> caughtException = loggingContext.getCatchBlock().getParameter();
			
			Boolean hasIssue = false;
			if(loggingContext.getLoggingArgs() != null && loggingContext.getLoggingArgs().size() > 0) {
				CtExpression<?> lastLoggingArg = loggingContext.getLoggingArgs().get(loggingContext.getLoggingArgs().size() - 1);
				
				if(lastLoggingArg instanceof CtVariableAccess) {
					CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) lastLoggingArg;
					
					if(!caughtException.getSimpleName().equalsIgnoreCase(variableAccess.getVariable().getSimpleName())) {
						hasIssue = true;
					}
				}
				
				else {
					hasIssue = true;
				}
			} else {
				hasIssue = true;
			}
			
			if(hasIssue) {
				ReportingUtils.report(ERROR_MESSAGE_EXCEPTION_NOT_LOGGED, loggingContext);
				LoggingIssue.createLoggingIssue(loggingContext, report, getRuleEnum(), getScopeEnum(), null);
			}
		}
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		return LoggingRuleEnum.EXCEPTION_NOT_BEING_LOGGED;
	}
	
	@Override
	public IssueScopeEnum getScopeEnum() {
		return IssueScopeEnum.CATCH;
	}
}
