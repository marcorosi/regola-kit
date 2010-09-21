package org.regola.codeassistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.regola.codeassistence.generator.Generator;
import org.regola.codeassistence.gui.CodeAssistenceApp;
import org.regola.descriptor.IClassDescriptor;
import org.regola.descriptor.IPropertyDescriptor;

/**
 * Prima implementazione del generatore di codice, molto rudimentale.
 * 
 * @author nicola
 * 
 */
public class FullStack {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// legge configuarazione
		// create Options object
		org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();

		// add t option
		options.addOption("h", false, "mostra questo aiuto");
		options.addOption("x", false, "avvia l'interfaccia grafica swing");
		options.addOption("g", true, "specifica i generatori da utilizzare separati da virgola");
		options.addOption("c", true, "la classe di modello da utilizzare");
		options.addOption("d", true, "la directory dove generare i file java");
		options.addOption("f", true, "la directory dove generare i file flex");
		options.addOption("s", false, "non scrive su disco: simulazione");
		options.addOption("m", false, "master/detail, come: -g dao,modelPattern,properties,list-handler,list,form,form-handler");

		System.out.println("Regola: assistente alla scrittura di codice");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) 
		{
			usage(options);
		}
		
		if (cmd.hasOption("x"))
		{
			//avvia l'interfaccia grafica
			CodeAssistenceApp.main(args);
			return;
		}
		
		if (!( cmd.hasOption("g") || cmd.hasOption("m") ) || !cmd.hasOption("c") )
			usage(options);
		
		String masterDetail = "dao,modelPattern,manager,properties,list-handler,list,form,form-handler";
		
		String generatorsString = cmd.hasOption("m") ? masterDetail: cmd.getOptionValue("g");
		Options ourOpt = new Options(cmd.getOptionValue("c"));
		
		Environment env = new Environment();
		
		if (cmd.hasOption("s"))
		env.setSimulate(true);

		env.setProjectDir(".");
		
		if (!cmd.hasOption("d"))
		{
			env.setOutputDir(new File(".").getCanonicalPath());
		}
		else
		{
			env.setOutputDir(cmd.getOptionValue("d"));
		}
		
		if (!cmd.hasOption("f"))
		{
			env.setFlexOutputDir(cmd.getOptionValue("f"));
		}
		
		generate(env, ourOpt,generatorsString);
		
	}
	
	public static VariablesBuilder instanceParameterBuilder(Environment env, String modelName) 
	{
		//IClassDescriptor modelDescriptor = env.getDescriptorService()
		//		.getClassDescriptor(Class.forName(ourOpt.getModelClass()));
		IClassDescriptor modelDescriptor;
		try {
			modelDescriptor = env.getClassDescriptor(Class.forName(modelName));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}	
		
		IPropertyDescriptor idProperty = modelDescriptor.getIdentifierDescriptor(); 
		if (idProperty==null)
		{
			String msg = "error: the class "+ modelDescriptor.getType().getCanonicalName()  +" doesn't have a property named 'id' ";
			System.err.println(msg);
			throw new RuntimeException(msg);
		}
		
		IClassDescriptor idDescriptor = null;		
		if (idProperty.getPropertyType().getPackage().getName().startsWith("java.") )
		{
			//idDescriptor = env.getDescriptorService()
			//.getClassDescriptor(idProperty.getPropertyType());
			idDescriptor = env.getClassDescriptor(idProperty.getPropertyType());
		}
		else
		{
			//idDescriptor = env.getDescriptorService()
			//	.getClassDescriptor(Class.forName(ourOpt.getModelClass()+"Id"));
			try {
				idDescriptor = env.getClassDescriptor(Class.forName(modelName + "Id"));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		// env.setPackageName(getPackageName(descriptor));

		return new VariablesBuilder(modelDescriptor,	idDescriptor);


	}
	
	private static void generate(Environment env, Options ourOpt, String generatorsString) throws ClassNotFoundException
	{
		
		VariablesBuilder pb = instanceParameterBuilder(env, ourOpt.getModelClass());
		//env.setPackageName((String) pb.getParameters().get("package"));
		
		for (Generator generator : ourOpt.getGeneratorListByNames(generatorsString.split(","))) {
			System.out.println(String.format("process type %s with generator %s",
					ourOpt.getModelClass(), generator.getDisplayName()));

			generator.generate(env, pb);
		}
		
		System.out.println("generation completed.");

	}

	private static void usage(org.apache.commons.cli.Options options) {
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar regola.jar", options);
		System.exit(0);
		
	}

}
