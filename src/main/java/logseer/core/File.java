package logseer.core;

import java.util.Objects;

import logseer.utils.SpoonUtils;
import spoon.reflect.cu.SourcePosition;

public class File {
	String fullPath;
	
	String simpleName;
	
	Integer numberOfLines;

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public Integer getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(Integer numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fullPath, simpleName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File other = (File) obj;
		return Objects.equals(fullPath, other.fullPath) && Objects.equals(simpleName, other.simpleName);
	}
	
	public File fillFileFromSourcePosition(SourcePosition sp) {
		if(sp.getFile() != null) {
			if(sp.getFile().getAbsoluteFile() != null) {
				this.setFullPath(sp.getFile().getAbsolutePath());
			}
			
			if(sp.getFile().getName() != null) {
				this.setSimpleName(sp.getFile().getName());
			}
			
			if(sp.getLine() != 0 && sp.getEndLine() != 0) {
				Integer classNumberOfLines = (sp.getEndLine() - sp.getLine()) + 1;
				this.setNumberOfLines(classNumberOfLines);
			}
		}
		
		return this;
	}
}
