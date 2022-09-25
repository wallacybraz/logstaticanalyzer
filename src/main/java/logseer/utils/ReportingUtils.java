package logseer.utils;


import logseer.core.GeneralContext;
import logseer.report.LoggingReportRow;

public class ReportingUtils {
	public static void report(String reportingMessage, GeneralContext lc) {
		System.out.println(String.format("[%s:%d] %s", lc.getFile().getSimpleName(), lc.getLineNumber(), reportingMessage));
	}
	
	public static void debug(String message) {
		boolean isDebug = getDebugFlagSomehow();
		
		if(isDebug) {
			System.out.println(String.format("[DEBUG] %s", message));
		}
	}

	private static boolean getDebugFlagSomehow() {
		return false;
	}
	
	public static int compareLoggingRows(LoggingReportRow r1, LoggingReportRow r2) {
		String name1 = r1.getFileName();
		String name2 = r2.getFileName();
		
		int nameCompare = name1.compareTo(name2);
		if(nameCompare == 0) {
			return r1.getLineNumber() - r2.getLineNumber();
		} else {
			return nameCompare;
		}
	}
}

