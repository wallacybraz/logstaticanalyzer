package logseer.libraries;

import java.util.List;

import logseer.core.LoggingContextCaracterizer;
import logseer.core.LoggingContextValidator;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.visitor.filter.AbstractFilter;

public abstract class LoggingLibrary {
	public LoggingLibraryEnum library;
	
	public abstract AbstractFilter<CtInvocation<?>> getLoggingFilter();
	
	public abstract LoggingContextCaracterizer getLoggingCaracterizer();
	
	public abstract List<LoggingContextValidator> getLoggingRules();

	public LoggingLibraryEnum getLibrary() {
		return library;
	}

	public void setLibrary(LoggingLibraryEnum library) {
		this.library = library;
	}
}
