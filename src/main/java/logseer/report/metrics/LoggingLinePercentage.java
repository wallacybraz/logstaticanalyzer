package logseer.report.metrics;

import logseer.report.Metric;

public class LoggingLinePercentage extends Metric<Double>  {
	private Integer fileLines;
	private Integer loggingLines;
	
	public LoggingLinePercentage() {
		this.setMetricEnum(MetricEnum.LLP);
		this.setLoggingLines(0);
	}

	public Integer getFileLines() {
		return fileLines;
	}

	public void setFileLines(Integer fileLines) {
		this.fileLines = fileLines;
	}

	public Integer getLoggingLines() {
		return loggingLines;
	}

	public void setLoggingLines(Integer loggingLines) {
		this.loggingLines = loggingLines;
	}
	
	@Override
	public Double getValue() {
		return this.calculatePercentage(this.fileLines, this.loggingLines);
	}
	
	@Override
	public String getValueFormatted() {
		return String.format("%.2f%%", this.getValue());
	}
	
	private Double calculatePercentage(Integer fileLines, Integer loggingLines) {
		if(fileLines != null && fileLines > 0) {
			return ((new Double(loggingLines)) / (new Double(fileLines))) * 100.00d;
		}
		
		return null;
	}
	
	public void increment(int numberOfLines) {
		this.loggingLines++;
	}
}
