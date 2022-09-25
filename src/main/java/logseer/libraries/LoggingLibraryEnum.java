package logseer.libraries;

import java.security.InvalidParameterException;

public enum LoggingLibraryEnum {
	SLFJ("slf4j"),
	JCL("jcl");
	
	private final String name;
	
	LoggingLibraryEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static LoggingLibraryEnum getValueFromName(String name) {
		if(name.equalsIgnoreCase(SLFJ.getName())) {
			return SLFJ;
		} else {
			throw new InvalidParameterException(String.format("Library with name %s not supported", name));
		}
	}
}
