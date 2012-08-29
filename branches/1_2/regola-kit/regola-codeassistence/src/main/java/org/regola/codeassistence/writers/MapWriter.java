package org.regola.codeassistence.writers;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;



import freemarker.template.Template;

public class MapWriter implements ProjectWriter {
	
	
	
	public MapWriter(Map<String, String> simulationMap) {
		super();
		this.simulationMap = simulationMap;
	}

	public void setSimulationMap(Map<String,String> simulationMap) {
		this.simulationMap = simulationMap;
	}

	public Map<String,String> getSimulationMap() {
		return simulationMap;
	}
	


	
	
	
	private Map<String,String> simulationMap = new HashMap<String,String>();
	
	/* (non-Javadoc)
	 * @see org.regola.codeassistence.Writer#writeToFile(java.io.File, freemarker.template.Template, java.lang.Object, boolean)
	 */
	@Override
	public void writeToFile(File file, Template template, Object variables, boolean append)
	{
		try {
			
			StringWriter writer = new StringWriter();
			writer.append("// file " + file.getPath());
			
			writer.append("\n" ); 

			template.process(variables, writer);
			
			simulationMap.put(file.getName(), writer.toString());

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
	
	}
	
	/* (non-Javadoc)
	 * @see org.regola.codeassistence.Writer#logNothingToDo(java.io.File, java.lang.String)
	 */
	@Override
	public void logNothingToDo(File file, String cause)
	{
		String msg = "<!-- file: " + file.getPath() + " -->\n";
		msg += "<!-- Don't write anything beacuse the file doesn't exists -->";
		simulationMap.put(file.getName(), msg);
	}

	@Override
	public void writeToFile(File file, String content, boolean append) {
		
		try {
			
			StringWriter writer = new StringWriter();
			writer.append("// file " + file.getPath());
			
			writer.append("\n" ); 

			writer.append(content);
			
			simulationMap.put(file.getName(), writer.toString());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		
	}
	

}
