/*
 * CodeAssistenceApp.java
 */

package org.regola.codeassistence.gui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CodeAssistenceApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new CodeAssistenceView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of CodeAssistenceApp
     */
    public static CodeAssistenceApp getApplication() {
        return Application.getInstance(CodeAssistenceApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
    	
		org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

		//options.addOption("h", false, "mostra questo aiuto");
		options.addOption("c", true, "la classe di modello da utilizzare");

		System.out.println("Regola kit graphical code assistence");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) 
		{
			usage(options);
		}
		
		if (cmd.hasOption("c"))
			setModelClass(cmd.getOptionValue("c"));
    	
        launch(CodeAssistenceApp.class, args);
    }
    
    private static String modelClass;
    
	private static void usage(org.apache.commons.cli.Options options) {
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar regola-codeassistence-1.1.jar", options);
		System.exit(0);
		
	}

	public static void setModelClass(String modelClass) {
		CodeAssistenceApp.modelClass = modelClass;
	}

	public static String getModelClass() {
		return modelClass;
	}


}
