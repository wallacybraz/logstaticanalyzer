package logseer.core;

public enum LoggingRuleEnum {
	// General rules
	NO_RETHROW_AFTER_LOGGING("No rethrow after logging"),
	EXCEPTION_NOT_BEING_LOGGED("Exception not being logged on catch block"),
	NO_ERROR_OUTSIDE_TRY_CATCH("No error logging outside catch block"), 
	INCOMPLETE_ERROR_CONTEXT("Unlogged context variables"), 
	NO_EMPTY_CATCH_BLOCK("No empty catch block.");
	
	private String ruleDescription;
	
	private LoggingRuleEnum(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	
}
