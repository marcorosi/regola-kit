package org.regola.codeassistence.writers;

import java.io.File;

import freemarker.template.Template;

public interface ProjectWriter {

	public abstract void writeToFile(File file, Template template,	Object variables, boolean append);
	
	public abstract void writeToFile(File file, String content,	boolean append);

	public abstract void makeDirectory(File file);

	public abstract void logNothingToDo(File file, String cause);

}