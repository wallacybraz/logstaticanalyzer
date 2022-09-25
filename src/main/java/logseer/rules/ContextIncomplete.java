package logseer.rules;

import java.util.Iterator;
import java.util.List;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTargetedExpression;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.visitor.filter.TypeFilter;

public class ContextIncomplete implements LoggingContextValidator {

	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(loggingContext.getSeverity().equals(LoggingSeverity.ERROR) && loggingContext.isInsideTryCatch()) {
			CtTry ctTry = loggingContext.getTryBlock();
			if(ctTry != null) {
				List<CtInvocation<?>> tryCatchInvocations = ctTry.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class)).list();
				for (CtInvocation<?> ctInvocation : tryCatchInvocations) {
					List<CtExpression<?>> arguments = ctInvocation.getArguments();
					
					Boolean isThisAcess = false;
					Boolean isFieldMethodCall = false;
					Boolean isAbstractMethodCall = false;
					
					if(ctInvocation instanceof CtTargetedExpression) {
						CtTargetedExpression<?, ?> targetExpression = (CtTargetedExpression<?, ?>) ctInvocation.getTarget();
						if(targetExpression instanceof CtThisAccess) {
							isThisAcess = true;
						}
						
						if(targetExpression instanceof CtFieldRead) {
							// Invocation is a method call on a field
							CtFieldRead<?> ctFielRead = (CtFieldRead<?>) targetExpression;
							String fieldName = ctFielRead.getVariable().getSimpleName();
							isFieldMethodCall = true;
						}
					}
					
					if(ctInvocation instanceof CtTypeAccess) {
						// Abstract invocation
						isAbstractMethodCall = true;
					}
				}
			}
		}
		
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IssueScopeEnum getScopeEnum() {
		// TODO Auto-generated method stub
		return null;
	}

}
