package logseer.core;

import java.lang.reflect.Method;
import java.util.List;

import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;

public class LoggingContext extends GeneralContext {
	private LoggingSeverity severity;
	
	private List<CtExpression<?>> loggingArgs;
	
	private ContainerTypeEnum containerTypeEnum;
	
	private boolean isInsideTryCatch;
	
	private CtMethod<?> containerMethod;
	
	private CtClass<?> containerClass;
	
	private CtInterface<?> containerInterface;	
	
	private Method invokedMethod;
	
	private CtInvocation<?> loggingInvocation;
	
	private CtTry tryBlock;
	
	private CtCatch catchBlock;
	
	private CtCatchVariable<?> catchedException;
	
	public LoggingSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(LoggingSeverity severity) {
		this.severity = severity;
	}

	public List<CtExpression<?>> getLoggingArgs() {
		return loggingArgs;
	}

	public void setLoggingArgs(List<CtExpression<?>> loggingArgs) {
		this.loggingArgs = loggingArgs;
	}

	public boolean isInsideTryCatch() {
		return isInsideTryCatch;
	}

	public void setInsideTryCatch(boolean isInsideTryCatch) {
		this.isInsideTryCatch = isInsideTryCatch;
	}

	public CtInvocation<?> getLoggingInvocation() {
		return loggingInvocation;
	}

	public void setLoggingInvocation(CtInvocation<?> loggingInvocation) {
		this.loggingInvocation = loggingInvocation;
	}

	public CtTry getTryBlock() {
		return tryBlock;
	}

	public void setTryBlock(CtTry tc) {
		this.tryBlock = tc;
	}

	public CtMethod<?> getContainerMethod() {
		return containerMethod;
	}
	
	public void setContainerMethod(CtMethod<?> containerMethod) {
		this.containerMethod = containerMethod;
	}
	
	
	public CtClass<?> getContainerClass() {
		return containerClass;
	}

	public void setContainerClass(CtClass<?> containerClass) {
		this.containerClass = containerClass;
	}
	
	

	public CtInterface<?> getContainerInterface() {
		return containerInterface;
	}

	public void setContainerInterface(CtInterface<?> containerInterface) {
		this.containerInterface = containerInterface;
	}

	public Method getInvokedMethod() {
		return invokedMethod;
	}

	public void setInvokedMethod(Method invokedMethod) {
		this.invokedMethod = invokedMethod;
	}
	
	public CtCatch getCatchBlock() {
		return catchBlock;
	}

	public void setCatchBlock(CtCatch catchBlock) {
		this.catchBlock = catchBlock;
	}
	
	public CtCatchVariable<?> getCatchedException() {
		return catchedException;
	}

	public void setCatchedException(CtCatchVariable<?> catchedException) {
		this.catchedException = catchedException;
	}

	public ContainerTypeEnum getContainerTypeEnum() {
		return containerTypeEnum;
	}

	public void setContainerTypeEnum(ContainerTypeEnum containerTypeEnum) {
		this.containerTypeEnum = containerTypeEnum;
	}
}
