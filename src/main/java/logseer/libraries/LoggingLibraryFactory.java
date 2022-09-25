package logseer.libraries;

import java.security.InvalidParameterException;

import logseer.libraries.slf4j.Slf4jLoggingLibrary;

public class LoggingLibraryFactory {
	static public LoggingLibrary getLibrary(LoggingLibraryEnum library) {
		switch(library) {
			case SLFJ: {
				return new Slf4jLoggingLibrary();
			}
			
			default: {
				throw new InvalidParameterException(String.format("%s library not supported", library.getName()));
			}
		}
	}
}
