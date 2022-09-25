package logseer.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class UnloggedContextVariables implements LoggingContextValidator {
	private static final String UNLOGGED_CONTEXT_VARIABLES_MESSAGE = "There are unlogged contextual parameters: ";
	private static final String COMMA = ",";
	private static final String SPACE = " ";
	private static final String MAIN_METHOD = "main";
	

	@Override
	public void validateLoggingContext(LoggingContext loggingContext, AnalysisReport report) {
		if(loggingContext.isInsideTryCatch() && loggingContext.getSeverity() == LoggingSeverity.ERROR) {
			// Buscar variáveis de entrada do método
			CtMethod<?> m = loggingContext.getContainerMethod();
			List<CtParameter<?>> parameterList;
			if(m != null && m.getParameters() != null && !m.getSimpleName().equalsIgnoreCase(MAIN_METHOD)) {
				parameterList = m.getParameters();
			} else {
				parameterList = new ArrayList<CtParameter<?>>();
			}
			
			List<CtExpression<?>> loggingInvocationArguments = loggingContext.getLoggingInvocation().getArguments();
			List<CtParameter<?>> unloggedVariables = new ArrayList<CtParameter<?>>();
			
			if(!parameterList.isEmpty()) {
				for(CtParameter<?> containerMethodParameter : parameterList) {
					String parameterName = containerMethodParameter.getSimpleName();
					CtTypeReference<?> parameterType = containerMethodParameter.getType();
					boolean variableIsLogged = false;
					
					for(CtExpression<?> ctExpression : loggingInvocationArguments) {
						if(ctExpression instanceof CtVariableRead) {
							CtVariableRead<?> variableRead = (CtVariableRead) ctExpression;
							String variableName = variableRead.getVariable().getSimpleName();
							CtTypeReference<?> variableType = variableRead.getType();
							
							if(parameterName.equalsIgnoreCase(variableName)) {
								variableIsLogged = true;
								break;
							}
						}
						
						if(ctExpression instanceof CtFieldRead) {
							CtFieldRead<?> fieldRead = (CtFieldRead) ctExpression;
							CtFieldReference<?> fieldReference = fieldRead.getVariable();
							if(fieldReference.getSimpleName().equals(parameterName)) {
								variableIsLogged = true;
								break;
							}
						}
					}
					
					if(variableIsLogged == false) {
						unloggedVariables.add(containerMethodParameter.clone());
					}
				}
				
				if(!unloggedVariables.isEmpty()) {
					String unloggedContextVariables = getUnloggedVariablesMessage(unloggedVariables);
					
					ReportingUtils.report(UNLOGGED_CONTEXT_VARIABLES_MESSAGE.concat(unloggedContextVariables), loggingContext);
					// Set context as having a logging issue
					LoggingIssue.createLoggingIssue(loggingContext, report, getRuleEnum(), getScopeEnum(), unloggedContextVariables);
				}
			}
		}
	}
	
	String getUnloggedVariablesMessage(List<CtParameter<?>> unloggedParameters) {
		if(!unloggedParameters.isEmpty()) {
			return unloggedParameters
							.stream()
							.map(p -> p.getSimpleName())
							.collect(Collectors.joining(COMMA + SPACE));
		}
		
		return null;
	}

	@Override
	public LoggingRuleEnum getRuleEnum() {
		return LoggingRuleEnum.INCOMPLETE_ERROR_CONTEXT;
	}
	
	@Override
	public IssueScopeEnum getScopeEnum() {
		return IssueScopeEnum.METHOD;
	}
}
