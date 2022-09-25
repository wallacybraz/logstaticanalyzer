package logseer.core;

import logseer.libraries.LoggingLibraryEnum;
import spoon.reflect.cu.SourcePosition;

public class GeneralContext {
	protected Integer indexS;
	
	protected Integer indexF;
	
	protected Integer lineNumber;
	
	protected LoggingLibraryEnum libraryEnum;
	
	protected File file;
	
	public Integer getIndexS() {
		return indexS;
	}

	public void setIndexS(Integer indexS) {
		this.indexS = indexS;
	}

	public Integer getIndexF() {
		return indexF;
	}

	public void setIndexF(Integer indexF) {
		this.indexF = indexF;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public LoggingLibraryEnum getLibraryEnum() {
		return libraryEnum;
	}

	public void setLibraryEnum(LoggingLibraryEnum libraryEnum) {
		this.libraryEnum = libraryEnum;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
