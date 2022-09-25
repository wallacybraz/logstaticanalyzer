package logseer.report;

import logseer.core.File;
import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingRuleEnum;
import logseer.report.metrics.IssuesByRule;
import logseer.report.metrics.MetricEnum;

public class LoggingIssue {
	LoggingContext loggingContext;
	
	LoggingRuleEnum loggingRuleEnum;
	
	IssueScopeEnum issueScopeEnum;
	
	String aditionalInfo;

	public LoggingContext getLoggingContext() {
		return loggingContext;
	}

	public void setLoggingContext(LoggingContext loggingContext) {
		this.loggingContext = loggingContext;
	}

	public LoggingRuleEnum getLoggingRuleEnum() {
		return loggingRuleEnum;
	}

	public void setLoggingRuleEnum(LoggingRuleEnum loggigEnum) {
		this.loggingRuleEnum = loggigEnum;
	}

	public IssueScopeEnum getIssueScopeEnum() {
		return issueScopeEnum;
	}

	public void setIssueScopeEnum(IssueScopeEnum issueScopeEnum) {
		this.issueScopeEnum = issueScopeEnum;
	}
	
	static public void createLoggingIssue(
			LoggingContext loggingContext, 
			AnalysisReport report, 
			LoggingRuleEnum ruleEnum, 
			IssueScopeEnum scope,
			String aditionalInfo
		) 
	{
		// Set context as having a logging issue
		LoggingIssue loggingIssue = new LoggingIssue();
		loggingIssue.setLoggingContext(loggingContext);
		loggingIssue.setLoggingRuleEnum(ruleEnum);
		loggingIssue.setIssueScopeEnum(scope);
		loggingIssue.setAditionalInfo(aditionalInfo);
		
		// Add issue to file issues mapping
		File issueFile = loggingContext.getFile();
		report.createFileAnalasysIfNotExists(issueFile);
		report.getFilesAnalysis().get(issueFile).getIssues().add(loggingIssue);
		
		// Update report issues by rule
		IssuesByRule issuesMyRuleMetric = (IssuesByRule) report.getGeneralMetrics().get(MetricEnum.IBR);
		issuesMyRuleMetric.incrementIssue(ruleEnum);
	}

	public String getAditionalInfo() {
		return aditionalInfo;
	}

	public void setAditionalInfo(String aditionalInfo) {
		this.aditionalInfo = aditionalInfo;
	}
}
