package logseer.processors;

import logseer.core.ContainerTypeEnum;
import logseer.core.File;
import logseer.core.IssueScopeEnum;
import logseer.core.LoggingContext;
import logseer.core.LoggingRuleEnum;
import logseer.report.AnalysisReport;
import logseer.report.LoggingIssue;
import logseer.utils.ReportingUtils;
import logseer.utils.SpoonUtils;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCatch;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;

public class EmptyCatchBlockProcessor extends AbstractProcessor<CtCatch> {
	private static final String NO_EMPTY_CATCH_CLAUSE = "Empty catch block. You should at least logging exception.";
	
	private AnalysisReport report;
	
	public EmptyCatchBlockProcessor(AnalysisReport report) {
		this.report = report;
	}

	public void process(CtCatch element) {
		// we get all statements and if there isn't statement, it means the block catch is empty!
		if (element.getBody().getStatements().size() == 0) {
			LoggingContext context = new LoggingContext();
			
			context.setCatchBlock(element);
			
			// Fill source code metadata
			SourcePosition sp = element.getPosition();
			context.setIndexS(sp.getSourceStart());
			context.setIndexF(sp.getSourceEnd());
			context.setLineNumber(sp.getLine());
			
			// get topmost class element
			CtClass<?> containerClass = null;
			CtInterface<?> containerInterface = null;
			ReportingUtils.debug("Searching for container class..." );
			containerClass = SpoonUtils.getDeclaringOuterClass(element);
			if(containerClass != null) {
				context.setContainerTypeEnum(ContainerTypeEnum.CLASS);
				context.setContainerClass(containerClass);
				context.setFile(new File().fillFileFromSourcePosition(containerClass.getPosition()));
			} else {
				containerInterface = SpoonUtils.getDeclaringOuterInterface(element);
				if(containerInterface != null) {
					context.setContainerTypeEnum(ContainerTypeEnum.INTERFACE);
					context.setContainerInterface(containerInterface);
					context.setFile(new File().fillFileFromSourcePosition(containerInterface.getPosition()));
				} else {
					return;
				}
			}
			
			report.createFileAnalasysIfNotExists(context.getFile());
			ReportingUtils.report(NO_EMPTY_CATCH_CLAUSE, context);
			
			// Create a logging context
			LoggingIssue.createLoggingIssue(context, this.report, LoggingRuleEnum.NO_EMPTY_CATCH_BLOCK, IssueScopeEnum.CATCH, null);
		}
	}
}

