package logseer.processors;

import java.util.List;

import logseer.report.metrics.LinesOfCode;
import logseer.utils.SpoonUtils;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.visitor.Filter;

public class LinesOfCodeProcessor extends AbstractProcessor<CtClass<?>> {
	private LinesOfCode linesOfCode;
	
	public LinesOfCodeProcessor(LinesOfCode linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	@Override
	public void process(CtClass<?> clasz) {
		try {
			// Verify if clasz is not a inner class
			CtClass<?> parentClass = clasz.getParent(new TypeFilter<CtClass<?>>(CtClass.class));
			if(parentClass != null) {
				return;
			}
			
			int numberOfLines = SpoonUtils.getNumberOfLines(clasz);
					
			this.linesOfCode.addLinesOfCode(numberOfLines);
		} catch(RuntimeException e) {
			System.err.println("Could not get lines of code from file :" + clasz.getSimpleName());
		}
	}

	public LinesOfCode getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(LinesOfCode linesOfCode) {
		this.linesOfCode = linesOfCode;
	}
}
