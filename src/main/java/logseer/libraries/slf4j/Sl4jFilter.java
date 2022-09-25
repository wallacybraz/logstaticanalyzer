package logseer.libraries.slf4j;

import java.util.HashSet;
import java.util.Set;


import spoon.reflect.code.CtInvocation;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;

public class Sl4jFilter extends AbstractFilter<CtInvocation<?>> {
	
	static public final String JAVA_UTIL_LOGGING_PACKAGE = "org.slf4j";
	static public final String JAVA_UTIL_LOGGER_OBJECT_NAME = "Logger";
	static public final String DOT_CHARACTER = ".";
	static public final Set<String> loggingInvocationNames = getSlf4jLoggingInvocationNames();
	
	@Override
	public boolean matches(CtInvocation<?> invocation) {
		CtExecutableReference<?> referenciaEx = invocation.getExecutable();
		CtTypeReference<?> tipoRef = referenciaEx.getDeclaringType();
		
		
		if(tipoRef != null) {
			return tipoRef.getQualifiedName().equals(JAVA_UTIL_LOGGING_PACKAGE + 
					DOT_CHARACTER + JAVA_UTIL_LOGGER_OBJECT_NAME) && 
					loggingInvocationNames.contains(invocation.getExecutable().getActualMethod().getName());
		} 
		
		return false;
	}


	private static Set<String> getSlf4jLoggingInvocationNames() {
		Set<String> names = new HashSet<String>();
		names.add("error");
		names.add("info");
		names.add("warn");
		names.add("debug");
		return names;
	}
	
}
