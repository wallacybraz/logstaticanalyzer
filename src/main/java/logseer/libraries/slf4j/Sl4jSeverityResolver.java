package logseer.libraries.slf4j;


import java.security.InvalidParameterException;

import logseer.core.LoggingSeverity;
import logseer.core.SeverityResolver;

public class Sl4jSeverityResolver implements SeverityResolver {
	private final String warn = "warn";
	private final String error = "error";
	private final String info = "info";
	private final String debug = "debug";
	
	
	@Override
	public LoggingSeverity resolveSeverity(String name) {
		if(name != null) {
			switch (name) {
			case warn:
				return LoggingSeverity.WARNING;
			case error: 
				return LoggingSeverity.ERROR;
			case info:
				return LoggingSeverity.INFO;
			case debug:
				return LoggingSeverity.DEBUG;
			default:
				throw new InvalidParameterException("Invalid verbosity level");
			}
		}
		
		throw new NullPointerException();
	}
}
