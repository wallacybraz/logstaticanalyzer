package logseer.core;

import spoon.Launcher;
import spoon.experimental.SpoonifierVisitor;

public class MainSpoon {

	public static void main(String[] args) {
		SpoonifierVisitor v = new SpoonifierVisitor(true);
		
		Launcher.parseClass("package resources.testingclasses;\n"
				+ "\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "public class DynamicMessageErrorLoggingClass {\n"
				+ "	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());\n"
				+ "	\n"
				+ "	public void doSomeErrorLogging() {\n"
				+ "		int a = 0;\n"
				+ "		int b = 4;\n"
				+ "		double result = doDivision(a, b);\n"
				+ "		LOGGER.info(\"result is: \" + result);\n"
				+ "	}\n"
				+ "	\n"
				+ "	private double doDivision(int a, int b) {\n"
				+ "		try {\n"
				+ "			return (double) a/b;\n"
				+ "		} catch (RuntimeException e) {\n"
				+ "			String message = e.getMessage();\n"
				+ "			LOGGER.error(\"Something went wrong: \" + message, e);\n"
				+ "			return 0d;\n"
				+ "		}\n"
				+ "	}\n"
				+ "}")
				.getMethodsByName("doSomeErrorLogging")
				.get(0)
				.accept(v);
		
		System.out.println(v.getResult());
	}

}
