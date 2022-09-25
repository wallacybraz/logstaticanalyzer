package logseer.utils;

import java.util.List;

import javax.management.RuntimeErrorException;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;

public class SpoonUtils {
	public static int getNumberOfLines(CtStatement stm) {
		int numberOfLines = 0;
		if(stm instanceof CtBlock) {
			CtBlock blockStm = (CtBlock) stm;
			List<CtStatement> blockStmChildren = blockStm.getStatements();
			
			for(CtStatement stmChild : blockStmChildren) {
				numberOfLines += getNumberOfLines(stmChild);
				
			}
			
			return numberOfLines;
		}
		
		if(stm instanceof CtCase) {
			CtCase caseStm = (CtCase) stm;
			List<CtStatement> caseStmChildren = caseStm.getStatements();
			for(CtStatement stmChild : caseStmChildren) {
				numberOfLines += getNumberOfLines(stmChild);
			}
			
			return numberOfLines;
		}
		
		if(stm instanceof CtComment) {
			return 0;
		}
		
		
		SourcePosition sp = stm.getPosition();
		int startingLine = sp.getLine();
		int endingLine = sp.getEndLine();
		numberOfLines += (endingLine - startingLine) + 1;
		
		return numberOfLines;
	}
	
	public static CtClass<?> getDeclaringOuterClass(CtElement ctElement) {
		CtClass<?> ctElementParentClass = ctElement.getParent(CtClass.class);
		
		if(ctElementParentClass == null) {
			if(ctElement instanceof CtClass) {
				return (CtClass) ctElement;
			} else {
				return null;
			}
		} else {
			// class parent found, must call it again
			return getDeclaringOuterClass(ctElementParentClass);
		}
	}

	public static CtInterface<?> getDeclaringOuterInterface(CtElement ctElement) {
		CtInterface<?> ctElementParentInterface = ctElement.getParent(CtInterface.class);
		
		if(ctElementParentInterface == null) {
			if(ctElement instanceof CtInterface) {
				return (CtInterface) ctElement;
			} else {
				return null;
			}
		} else {
			// class parent found, must call it again
			return getDeclaringOuterInterface(ctElementParentInterface);
		}
	}
}
