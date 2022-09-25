package logseer.report.metrics;

public enum MetricEnum {
	LOC("Lines of Code"),
	LLPF("Logging Lines Percentage by File"),
	LLP("Logging Lines Percentage"),
	GVD("General Verbosity Distribution"),
	IBR("Issues by Rule");
	
	public String description;
	
	MetricEnum(String description) {
		this.description = description;
	}
}
