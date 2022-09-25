package logseer.libraries.slf4j;

import logseer.core.AbstractLoggingCharacterizer;
import logseer.core.SeverityResolver;

public class Slf4jLoggingCharacterizer extends AbstractLoggingCharacterizer {
	@Override
	protected SeverityResolver getSeverityResolver() {
		return new Sl4jSeverityResolver();
	}
}
