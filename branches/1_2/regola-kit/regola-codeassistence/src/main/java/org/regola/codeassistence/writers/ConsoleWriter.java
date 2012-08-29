package org.regola.codeassistence.writers;

import java.io.File;
import java.io.StringWriter;

import freemarker.template.Template;

public class ConsoleWriter implements ProjectWriter {

	@Override
	public void writeToFile(File file, Template template, Object variables,
			boolean append) {
		try {

			StringWriter writer = new StringWriter();
			writer.append("// file " + file.getPath());

			writer.append("\n");

			template.process(variables, writer);

			System.out.print(writer.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void makeDirectory(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logNothingToDo(File file, String cause) {
		System.out.println("Nothing to do with file " + file.getPath()
				+ " beacause " + cause);

	}

	@Override
	public void writeToFile(File file, String content, boolean append) {
		StringWriter writer = new StringWriter();
		writer.append("// file " + file.getPath());

		writer.append("\n");

		writer.append(content);

		System.out.print(writer.toString());

		
	}

}
