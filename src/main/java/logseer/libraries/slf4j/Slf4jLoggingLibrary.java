package logseer.libraries.slf4j;

import java.util.ArrayList;
import java.util.List;

import logseer.core.AbstractLoggingCharacterizer;
import logseer.core.LoggingContextCaracterizer;
import logseer.core.LoggingContextValidator;
import logseer.libraries.LoggingLibrary;
import logseer.libraries.LoggingLibraryEnum;
import logseer.rules.ExceptionNotLogged;
import logseer.rules.NoErrorOutstideTryCatch;
import logseer.rules.NoRethrowAfterLogging;
import logseer.rules.UnloggedContextVariables;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.visitor.filter.AbstractFilter;

public class Slf4jLoggingLibrary extends LoggingLibrary {
	private final LoggingContextCaracterizer characterizer = new Slf4jLoggingCharacterizer();
	
	public Slf4jLoggingLibrary() {
		this.setLibrary(LoggingLibraryEnum.SLFJ);
	}

	@Override
	public AbstractFilter<CtInvocation<?>> getLoggingFilter() {
		AbstractFilter<CtInvocation<?>> filter = new Sl4jFilter();
		return filter;
	} 

	@Override
	public LoggingContextCaracterizer getLoggingCaracterizer() {
		return this.characterizer;
	}

	@Override
	public List<LoggingContextValidator> getLoggingRules() {
		List<LoggingContextValidator> rules = new ArrayList<LoggingContextValidator>();
		rules.add(new ExceptionNotLogged());
		rules.add(new NoRethrowAfterLogging());
		rules.add(new UnloggedContextVariables());
		rules.add(new NoErrorOutstideTryCatch());
		
		return rules;
	}
}
