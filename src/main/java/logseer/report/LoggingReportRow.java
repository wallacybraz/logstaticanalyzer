package logseer.report;

import logseer.utils.ReportingUtils;

public class LoggingReportRow implements Comparable<LoggingReportRow> {
	private String fileName;
	
	private Integer lineNumber;
	
	private String issueDescription;
	
	private String issueCodeSnippet;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public String getIssueCodeSnippet() {
		return issueCodeSnippet;
	}

	public void setIssueCodeSnippet(String issueCodeSnippet) {
		this.issueCodeSnippet = issueCodeSnippet;
	}

	@Override
	public int compareTo(LoggingReportRow o) {
		return ReportingUtils.compareLoggingRows(this, o);
	}
}
