package logseer;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logseer.core.CmdLineOptionsParser;
import logseer.core.LoggingContext;
import logseer.core.LoggingContextValidator;
import logseer.core.Options;
import logseer.libraries.LoggingLibrary;
import logseer.libraries.LoggingLibraryEnum;
import logseer.libraries.LoggingLibraryFactory;
import logseer.processors.EmptyCatchBlockProcessor;
import logseer.processors.LinesOfCodeProcessor;
import logseer.report.AnalysisReport;
import logseer.report.metrics.GeneralVerbosityDistribution;
import logseer.report.metrics.IssuesByRule;
import logseer.report.metrics.LinesOfCode;
import logseer.report.metrics.LoggingLinePercentage;
import logseer.report.metrics.MetricEnum;
import logseer.utils.JasperReportUtils;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.SourcePosition;

public class LogSeerApplication {

	static public final Logger LOGGER = LoggerFactory.getLogger(LogSeerApplication.class); 
	
	public static void main(String[] args) {
		Options opts;
		if(args.length > 0) {
			opts = CmdLineOptionsParser.parseCmdLineOptions(args);
		} else {
			throw new IllegalArgumentException("File/Directory not given");
		}
		
		System.out.println(String.format("Initing logging analysis at %s", opts.getPathDir()));
		Launcher launcher = new Launcher();
		
		// Set spoon default parameters
		launcher.addInputResource(opts.getPathDir());
		launcher.getEnvironment().setNoClasspath(true);
		launcher.getEnvironment().setIgnoreDuplicateDeclarations(true);
		launcher.getEnvironment().setComplianceLevel(8);
		launcher.getEnvironment().setCommentEnabled(false);
		launcher.getEnvironment().setIgnoreSyntaxErrors(true);

		// Build model
		System.out.println("Building model...");
		Long startingTime = System.currentTimeMillis();
		launcher.buildModel();
		Long endingTime = System.currentTimeMillis();
		System.out.println(String.format("Model succesfuly built. It took %d seconds", (endingTime - startingTime)/1000l));
		
		CtModel model = launcher.getModel();

		// Init analysis report
		AnalysisReport report = new AnalysisReport();
		
		
		// Init general processors and metrics
		LinesOfCode linesOfCode = new LinesOfCode();
		LinesOfCodeProcessor linesOfCodeProcessor = new LinesOfCodeProcessor(linesOfCode);
		EmptyCatchBlockProcessor emptyCatchBlockProcessor = new  EmptyCatchBlockProcessor(report);
		GeneralVerbosityDistribution generalVerbosityDistribution = new GeneralVerbosityDistribution();
		LoggingLinePercentage loggingLinePercentage = new LoggingLinePercentage();
		IssuesByRule issuesByRule = new IssuesByRule();
		
		// Add general metrics to report
		report.getGeneralMetrics().put(MetricEnum.LOC, linesOfCode);
		report.getGeneralMetrics().put(MetricEnum.GVD, generalVerbosityDistribution);
		report.getGeneralMetrics().put(MetricEnum.LLP, loggingLinePercentage);
		report.getGeneralMetrics().put(MetricEnum.IBR, issuesByRule);
		
		// Execute general processors
		System.out.println("Executing general processors...");
		launcher.addProcessor(emptyCatchBlockProcessor);
		launcher.addProcessor(linesOfCodeProcessor);
		launcher.process();
		
		// Now that we have LOC, update logging line percentage
		loggingLinePercentage.setFileLines(linesOfCode.getValue());

		
		// Build logging libraries cache
		System.out.println("Creating logging libraries cache...");
		List<LoggingLibrary> loggingLibraries = new ArrayList<LoggingLibrary>();
		for(LoggingLibraryEnum library: opts.getLibraries()) {
			try {
				loggingLibraries.add(LoggingLibraryFactory.getLibrary(library));
			} catch (InvalidParameterException e) {
				System.out.println(e.getMessage());
				System.exit(-1);
			}
		}
		
		// For each logging library
		List<LoggingContext> loggingContexts = new ArrayList<LoggingContext>();
		for(LoggingLibrary loggingLibrary: loggingLibraries) {
			System.out.println(String.format("Searching for %s logging invocations...", loggingLibrary.getLibrary().getName()));
			// Get library specific logging filter
			List<CtInvocation<?>> loggingInvocations = model.filterChildren(loggingLibrary.getLoggingFilter()).list();
			System.out.println(String.format("%d %s logging invocations were found", loggingInvocations.size(), loggingLibrary.getLibrary().getName()));
			
			// For each logging library invocation found
			for(CtInvocation<?> loggingInvocation : loggingInvocations) {
				// Create logging context
				LoggingContext loggingContext = loggingLibrary
						.getLoggingCaracterizer()
						.characterizeLoggingInvocation(loggingInvocation, report);
				
				if(loggingContext == null) {
					continue;
				}
				
				// Increment logging line
				SourcePosition loggingSp = loggingInvocation.getPosition();
				int loggingSpStartingLine = loggingSp.getLine();
				int loggingSpEndingLine = loggingSp.getEndLine();
				int loggingInvocationNumberOfLines = (loggingSpEndingLine - loggingSpStartingLine) + 1;
				
				loggingLinePercentage.increment(loggingInvocationNumberOfLines);
				
				// For each logging library rule
				for(LoggingContextValidator loggingRule: loggingLibrary.getLoggingRules()) {
					// Apply validator to given logging context and update report
					loggingRule.validateLoggingContext(loggingContext, report);
				}
				
				loggingContexts.add(loggingContext);
				generalVerbosityDistribution.update(loggingContext);
			}
		}
		
		// In the end, generate report calling jasper
		JasperReportUtils.exportJasperReport(report, opts.getOutputDir());
	}
}
