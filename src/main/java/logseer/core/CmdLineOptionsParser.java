package logseer.core;

import java.io.File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logseer.libraries.LoggingLibraryEnum;

public class CmdLineOptionsParser {
	private static String PATH_OPT_REGEX = "--path=(?<path>.*)";
	private static String LIBRARIES_LIST_REGEX="slf4j|logback|log4j";
	private static String OUTPUT_DIR_REGEX="--output=(?<output>.*)";
	private static String LIBRARIES_OPT_REGEX  = String.format("--libraries=(?<libraries>(%s)+(,%s)?)", LIBRARIES_LIST_REGEX, LIBRARIES_LIST_REGEX);
	private static String OPT_REGEX = String.format("(%s|%s|%s)", PATH_OPT_REGEX, LIBRARIES_OPT_REGEX, OUTPUT_DIR_REGEX);
	
	public static Options parseCmdLineOptions(String args[]) {
		Options opt = new Options();

		if(args.length > 0) {
			for(String cmdArg: args) {
				Pattern pattern = Pattern.compile(OPT_REGEX);
				Matcher matcher = pattern.matcher(cmdArg);
				matcher.find();
				String path = matcher.group("path");
				if(path != null) {
					opt.setPathDir(path);
					continue;
				}
				
				String libraries = matcher.group("libraries");
				if(libraries != null) {
					String[] librariesList = libraries.split(",");
					for(String library: librariesList) {
						opt.getLibraries().add(LoggingLibraryEnum.getValueFromName(library));
					}
					continue;
				} 
				
				String outputDir = matcher.group("output");
				if(outputDir != null) {
					File file = new File(outputDir);
					if(!file.exists()) {
						System.out.println(String.format("path %s doesn't exist", outputDir));
						System.exit(-1);
					}
					
					if(!file.isDirectory()) {
						System.out.println(String.format("path %s is not a directory", outputDir));
						System.exit(-1);
					}
					
					if(!file.canWrite()) {
						System.out.println(String.format("directory %s is not writable", outputDir));
						System.exit(-1);
					}
					
					opt.setOutputDir(outputDir);
				}
			}
		} else {
			System.out.println("Invalid cmd args");
			System.exit(-1);
		}
		
		
		if(opt.getLibraries().isEmpty()) {
			System.out.println("Invalid libraries parameter");
			System.exit(-1);
		}
		
		if(opt.getPathDir() == null || opt.getPathDir().isEmpty()) {
			System.out.println("Invalid libraries parameter");
			System.exit(-1);
		}
		
		if(opt.getOutputDir() == null) {
			System.out.println("Output path invalid");
			System.exit(-1);
		}
		
		return opt;
	}
}
