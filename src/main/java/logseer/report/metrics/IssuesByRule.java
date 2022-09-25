package logseer.report.metrics;

import java.util.HashMap;
import java.util.Map;

import logseer.core.LoggingRuleEnum;
import logseer.report.Metric;

public class IssuesByRule extends Metric<Map<LoggingRuleEnum, Integer>> {
	private LoggingRuleEnum loggingRule;
	
	public IssuesByRule() {
		setValue(new HashMap<LoggingRuleEnum, Integer>());
		this.initIssuesMap();
	}

	private void initIssuesMap() {
		Map<LoggingRuleEnum, Integer> issuesMap = this.getValue();
		for(LoggingRuleEnum loggingRuleEnum: LoggingRuleEnum.values()) {
			issuesMap.put(loggingRuleEnum, 0);
		}
	}

	public LoggingRuleEnum getLoggingRule() {
		return loggingRule;
	}

	public void setLoggingRule(LoggingRuleEnum loggingRule) {
		this.loggingRule = loggingRule;
	}
	
	public void incrementIssue(LoggingRuleEnum loggingRuleEnum) {
		Integer actualValue = this.getValue().get(loggingRuleEnum);
		this.getValue().put(loggingRuleEnum, actualValue + 1);
	}
}
