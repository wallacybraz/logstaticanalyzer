package logseer.report;

import org.apache.commons.lang3.NotImplementedException;

import logseer.core.LoggingContext;
import logseer.report.metrics.MetricEnum;

public class Metric<T> {
	private MetricEnum metricEnum;
	
	private int order;
	
	private T value;

	public MetricEnum getMetricEnum() {
		return metricEnum;
	}

	public void setMetricEnum(MetricEnum metricEnum) {
		this.metricEnum = metricEnum;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void updateMetric(LoggingContext lc) {
		throw new NotImplementedException();
	}

	public Object getValueFormatted() {
		return this.getValue().toString();
	}
}
