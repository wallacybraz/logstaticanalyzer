package logseer.core;

import java.util.List;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;

import logseer.libraries.LoggingLibraryEnum;
import logseer.libraries.slf4j.Sl4jSeverityResolver;
import logseer.report.AnalysisReport;
import logseer.report.FileAnalysis;
import logseer.report.Metric;
import logseer.report.metrics.MetricEnum;
import logseer.utils.ReportingUtils;
import logseer.utils.SpoonUtils;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;

public abstract class AbstractLoggingCharacterizer implements LoggingContextCaracterizer {
	@Override
	public LoggingContext characterizeLoggingInvocation(CtInvocation<?> inv, AnalysisReport report) {
		if(inv == null) {
			throw new NullPointerException();
		}
		
		if(report == null) {
			throw new NullPointerException();
		}
		
		LoggingContext lc = new LoggingContext();
		// Set method invocation
		lc.setLoggingInvocation(inv);
		
		// Fill source code metadata
		SourcePosition sp = inv.getPosition();
		lc.setIndexS(sp.getSourceStart());
		lc.setIndexF(sp.getSourceEnd());
		lc.setLineNumber(sp.getLine());
		
		// get topmost class element
		CtClass<?> containerClass = null;
		CtInterface<?> containerInterface = null;
		ReportingUtils.debug("Searching for container class..." );
		containerClass = SpoonUtils.getDeclaringOuterClass(inv);
		if(containerClass != null) {
			lc.setContainerTypeEnum(ContainerTypeEnum.CLASS);
			lc.setContainerClass(containerClass);
			lc.setFile(new File().fillFileFromSourcePosition(containerClass.getPosition()));
		} else {
			containerInterface = SpoonUtils.getDeclaringOuterInterface(inv);
			if(containerInterface != null) {
				lc.setContainerTypeEnum(ContainerTypeEnum.INTERFACE);
				lc.setContainerInterface(containerInterface);
				lc.setFile(new File().fillFileFromSourcePosition(containerInterface.getPosition()));
			} else {
				return null;
			}
		}
		
		report.createFileAnalasysIfNotExists(lc.getFile());
		
		// Fill method invocation metadata
		lc.setLibraryEnum(LoggingLibraryEnum.SLFJ);
		List<CtExpression<?>> loggingInvArgs = inv.getArguments();
		lc.setLoggingArgs(loggingInvArgs);
		lc.setInvokedMethod(inv.getExecutable().getActualMethod());
		
		// Fill try/catch metadata
		CtCatch ctCatch = inv.getParent(CtCatch.class);
		if(ctCatch != null) {
			lc.setInsideTryCatch(true);
			lc.setCatchBlock(ctCatch);
			
			CtTry ctTry = ctCatch.getParent(CtTry.class);
			lc.setTryBlock(ctTry);
			
			CtCatchVariable<?> catchedVariables = ctCatch.getParameter();
			lc.setCatchedException(catchedVariables);
		} else {
			lc.setInsideTryCatch(false);
		}
		
		// Fill container method metadata
		CtMethod<?> methodSearcher = inv.getParent(CtMethod.class);
		if(methodSearcher != null) {
			lc.setContainerMethod(methodSearcher);
		}
		
		// Fill logging severity info
		SeverityResolver severityResolver = getSeverityResolver();
		lc.setSeverity(severityResolver.resolveSeverity(lc.getInvokedMethod().getName()));
		
		return lc;
	}
	
	abstract protected SeverityResolver getSeverityResolver();
}
