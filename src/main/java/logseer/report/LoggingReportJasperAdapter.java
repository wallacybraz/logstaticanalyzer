package logseer.report;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import logseer.core.File;
import logseer.core.LoggingContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class LoggingReportJasperAdapter implements JRDataSource {
	
	private Queue<LoggingIssue> fileAnalysesQueue = new ArrayDeque<LoggingIssue>();
	
	public LoggingReportJasperAdapter(AnalysisReport report) {
		Collection<FileAnalysis> fAnalysis = report.getFilesAnalysis().values();
		for(FileAnalysis fileAnalysis: fAnalysis) {
			fileAnalysesQueue.addAll(fileAnalysis.getIssues());
		}
	}

	@Override
	public boolean next() throws JRException {
		return !fileAnalysesQueue.isEmpty();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		LoggingIssue loggingIssue = fileAnalysesQueue.peek();
		LoggingContext loggingContext = loggingIssue.getLoggingContext();
		if(jrField.getName().equalsIgnoreCase("FILE_NAME")) {
			return loggingContext.getFile().getSimpleName();
		}
		
		if(jrField.getName().equalsIgnoreCase("ISSUE_LINE")) {
			return loggingContext.getLineNumber();
		}
		
		if(jrField.getName().equalsIgnoreCase("ISSUE_DESCRIPTION")) {
			fileAnalysesQueue.poll();
			return loggingIssue.loggingRuleEnum.getRuleDescription();
		}
		
		return null;
	}

	public Queue<LoggingIssue> getFileAnalysesQueue() {
		return fileAnalysesQueue;
	}

	public void setFileAnalysesQueue(Queue<LoggingIssue> fileAnalysesQueue) {
		this.fileAnalysesQueue = fileAnalysesQueue;
	}
}
