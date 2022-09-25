package logseer.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.data.general.DefaultPieDataset;

import logseer.core.ContainerTypeEnum;
import logseer.core.LoggingRuleEnum;
import logseer.core.LoggingSeverity;
import logseer.report.AnalysisReport;
import logseer.report.FileAnalysis;
import logseer.report.LoggingIssue;
import logseer.report.LoggingReportRow;
import logseer.report.Metric;
import logseer.report.PieChartRenderer;
import logseer.report.metrics.GeneralVerbosityDistribution;
import logseer.report.metrics.IssuesByRule;
import logseer.report.metrics.LinesOfCode;
import logseer.report.metrics.LoggingLinePercentage;
import logseer.report.metrics.MetricEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


public class JasperReportUtils {
	public static String JASPER_JRXML_FILE = "logseer.jrxml";
	public static String GVD_DATASET = "GeneralDistributionDataset";
	
	private static JasperReport compileLoggingReport() {
		JasperReport jasperReport = null;
		
		URL resource = JasperReportUtils.class.getClassLoader().getResource(JASPER_JRXML_FILE);
		
		try {
			jasperReport = JasperCompileManager.compileReport(resource.getPath());
		} catch (JRException e) {
			e.printStackTrace();
		}

		return jasperReport;
	}
	
	public static void exportJasperReport(AnalysisReport report, String outputDir) {
		// Generate general metric graphs
		Map<String, File> outputFiles = generateGraphImages(report.getGeneralMetrics()); 
		
		JasperReport jreport = compileLoggingReport();
		
		Object[] reportRows = getReportRowsFromReportDigest(report);
		JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(reportRows);
		
		Map<String, Object> parameters = getDefaultParameters(report, outputFiles);
		
		try {
			JasperPrint jp = JasperFillManager.fillReport(jreport, parameters, dataSource);
			JasperExportManager.exportReportToPdfFile(jp, outputDir.concat("/logseer_report.pdf"));
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private static Object[] getReportRowsFromReportDigest(AnalysisReport report) {
		List<LoggingReportRow> rows = new ArrayList<LoggingReportRow>();
		
		for(Entry<logseer.core.File, FileAnalysis> fileAnalysis:  report.getFilesAnalysis().entrySet()) {
			logseer.core.File file = fileAnalysis.getKey();
			List<LoggingIssue> fileIssues = fileAnalysis.getValue().getIssues();
			
			for(LoggingIssue issue: fileIssues) {
				LoggingReportRow row = new LoggingReportRow();
				
				try {
					switch(issue.getIssueScopeEnum()) {
						case INLINE: {
							row.setIssueCodeSnippet(issue.getLoggingContext().getLoggingInvocation().prettyprint());
							break;
						}
						case METHOD: {
							row.setIssueCodeSnippet(issue.getLoggingContext().getContainerMethod().prettyprint());
							break;
						}
						case TRY: {
							row.setIssueCodeSnippet(issue.getLoggingContext().getTryBlock().prettyprint());
							break;
						}
						case CATCH: {
							row.setIssueCodeSnippet(issue.getLoggingContext().getCatchBlock().prettyprint());
							break;
						}
						case CLASS_INTERFACE: {
							row.setIssueCodeSnippet(issue.getLoggingContext().getContainerTypeEnum()
									.equals(ContainerTypeEnum.CLASS) ? 
									issue.getLoggingContext().getContainerClass().prettyprint() : 
									issue.getLoggingContext().getContainerInterface().prettyprint());
							break;
						}
					}
				} catch (RuntimeException e) {
					try {
						row.setIssueCodeSnippet(issue.getLoggingContext().getLoggingInvocation().prettyprint());
					} catch (Exception e2) {
						continue;
					}
				}
				
				row.setFileName(file.getSimpleName());
				if(issue.getAditionalInfo() != null) {
					row.setIssueDescription(issue.getLoggingRuleEnum().getRuleDescription().concat(": " + issue.getAditionalInfo()));
				} else {
					row.setIssueDescription(issue.getLoggingRuleEnum().getRuleDescription());
				}
				
				row.setLineNumber(issue.getLoggingContext().getLineNumber());
				
				rows.add(row);
			}
		}
		
		rows.sort((r1, r2) -> ReportingUtils.compareLoggingRows(r1, r2));
		
		return rows.toArray();
	}

	private static Map<String, File> generateGraphImages(Map<MetricEnum, Metric<?>> generalMetrics) {
		// General logging distribution
		GeneralVerbosityDistribution gvd = (GeneralVerbosityDistribution) generalMetrics.get(MetricEnum.GVD);
		DefaultPieDataset<String> gvdDataSet = new DefaultPieDataset<String>();
		for(Entry<LoggingSeverity, Integer> entry: gvd.getValue().entrySet()) {
			gvdDataSet.setValue(entry.getKey().name(), entry.getValue());
		}
		File gvdPieChartImage = PieChartRenderer.createAndExportPieChart(MetricEnum.GVD.description, "gvd", gvdDataSet);
		
		// Issues by rule
		IssuesByRule ibr = (IssuesByRule) generalMetrics.get(MetricEnum.IBR);
		DefaultPieDataset<String> ibrDataSet = new DefaultPieDataset<String>();
		for(Entry<LoggingRuleEnum, Integer> entry: ibr.getValue().entrySet()) {
			ibrDataSet.setValue(entry.getKey().getRuleDescription(), entry.getValue());
		}
		File ibrPieCharImage = PieChartRenderer.createAndExportPieChart(MetricEnum.IBR.description, "ibr", ibrDataSet);
		
		
		Map<String, File> files = new HashMap<String, File>();
		files.put("GVD", gvdPieChartImage);
		files.put("IBR", ibrPieCharImage);

		return files;
	}

	private static Map<String, Object> getDefaultParameters(AnalysisReport report, Map<String, File> outputFiles) {
		
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("GVD_GRAPH_URL", outputFiles.get("GVD").getAbsolutePath());
		paramMap.put("IBR_GRAPH_URL", outputFiles.get("IBR").getAbsolutePath());
		
		LinesOfCode linesOfCode = (LinesOfCode) report.getGeneralMetrics().get(MetricEnum.LOC);
		LoggingLinePercentage loggingLinePercentage = (LoggingLinePercentage) report.getGeneralMetrics().get(MetricEnum.LLP);
		
		paramMap.put("SLOC", linesOfCode.getValue());
		paramMap.put("LLP", loggingLinePercentage.getValue());
		
		return paramMap;
	}
}
	
	
