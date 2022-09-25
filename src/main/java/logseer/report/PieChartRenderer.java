package logseer.report;

import java.util.Date;
import java.util.Map;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.ExportUtils;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartRenderer {
	public static File createAndExportPieChart(String title, String chartName, DefaultPieDataset<String> dataSet) {
		JFreeChart objChart = ChartFactory.createPieChart (
			title,  
		    dataSet,    
		    false,      
		    true,        
		    false           
	    );
		
		PiePlot plotagem = (PiePlot) objChart.getPlot();
		plotagem.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({1} | {2})"));
		plotagem.setLabelBackgroundPaint(new Color(220, 220, 220));
		
		try {
			File outputFile = File.createTempFile("logseerchart-".concat(chartName), ".png");
			outputFile.deleteOnExit();
			ExportUtils.writeAsPNG(objChart, 500, 500, outputFile);
			
			return outputFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
