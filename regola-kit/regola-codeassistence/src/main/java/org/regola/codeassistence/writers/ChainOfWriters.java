package org.regola.codeassistence.writers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;

public class ChainOfWriters implements ProjectWriter {

	private Map<String,String> simulationMap = new HashMap<String,String>();

	protected ProjectWriter fswriter = new FSWriter();
	protected ProjectWriter mapWriter = new MapWriter(simulationMap);
	protected ProjectWriter consoleWriter = new ConsoleWriter();
	
	
	public boolean isSimulation = false;
	public boolean logOnConsole = false;
	
	
	
	public ChainOfWriters() {
		super();
		
	}
	
	public boolean isSimulate() {
		return isSimulate();
	}

	public void setSimulate(boolean simulate) {
		this.isSimulation = simulate;
		if (simulate) 
		{	
			simulationMap = new HashMap<String,String>();
			mapWriter = new MapWriter(simulationMap);
		}
	}

		
	@Override
	public void writeToFile(File file, Template template, Object variables,
			boolean append) {
		
		if (isSimulation)
			mapWriter.writeToFile(file, template, variables, append);
		else 
			fswriter.writeToFile(file, template, variables, append);
		
		if (logOnConsole) consoleWriter.writeToFile(file, template, variables, append);

	}

	@Override
	public void makeDirectory(File file) {

		if (isSimulation)
			mapWriter.makeDirectory(file);
		else 
			fswriter.makeDirectory(file);
		
		if (logOnConsole) consoleWriter.makeDirectory(file);


	}

	@Override
	public void logNothingToDo(File file, String cause) {
		if (isSimulation)
			mapWriter.logNothingToDo(file, cause);
		else 
			fswriter.logNothingToDo(file, cause);
		
		if (logOnConsole) consoleWriter.logNothingToDo(file, cause);


	}

	@Override
	public void writeToFile(File file, String content, boolean append) {
		if (isSimulation)
			mapWriter.writeToFile(file, content, append);
		else 
			fswriter.writeToFile(file, content, append);
		
		if (logOnConsole) consoleWriter.writeToFile(file, content, append);
		
	}

	public Map<String, String> getSimulationMap() {
		// TODO Auto-generated method stub
		return simulationMap;
	}

}
