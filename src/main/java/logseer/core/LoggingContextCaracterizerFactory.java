package logseer.core;

import org.apache.commons.lang3.NotImplementedException;

import logseer.libraries.LoggingLibraryEnum;
import logseer.libraries.slf4j.Slf4jLoggingCharacterizer;

public class LoggingContextCaracterizerFactory {
	public static LoggingContextCaracterizer getCharacterizer(LoggingLibraryEnum library) {
		if(library.equals(LoggingLibraryEnum.SLFJ)) {
			return new Slf4jLoggingCharacterizer();
		}
		
		throw new NotImplementedException();
	}
}
