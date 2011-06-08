package org.regola.codeassistence.writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



import freemarker.template.Template;

public class FSWriter implements ProjectWriter {
	
	/* (non-Javadoc)
	 * @see org.regola.codeassistence.Writer#writeToFile(java.io.File, freemarker.template.Template, java.lang.Object, boolean)
	 */
	@Override
	public void writeToFile(File file, Template template, Object variables, boolean append)
	{
		try {
		FileWriter out = new FileWriter(file,append);
		template.process(variables, out);
		out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.regola.codeassistence.Writer#makeDirectory(java.io.File)
	 */
	@Override
	public void makeDirectory(File file)
	{
		File dir = file.getParentFile();
		
		if(!dir.exists())
			dir.mkdirs();
	}
	
	/* (non-Javadoc)
	 * @see org.regola.codeassistence.Writer#logNothingToDo(java.io.File, java.lang.String)
	 */
	@Override
	public void logNothingToDo(File file, String cause)
	{
		
	}

	@Override
	public void writeToFile(File file, String content, boolean append) {
		try {
			
			FileWriter out = new FileWriter(file, append);
			out.write(content);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	

}
