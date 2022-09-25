package logseer.report.metrics;

import logseer.report.Metric;

public class LinesOfCode extends Metric<Integer> {
	public LinesOfCode() {
		this.setMetricEnum(MetricEnum.LOC);
		this.setValue(0);
		this.setOrder(0);
	}
	
	public void addLinesOfCode(Integer value) {
		this.setValue(this.getValue() + value);
	}
}
