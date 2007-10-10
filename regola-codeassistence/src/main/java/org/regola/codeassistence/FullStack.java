package org.regola.codeassistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.regola.codeassistence.generator.Generator;
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
		options.addOption("g", true, "specifica i generatori da utilizzare separati da virgola");
		options.addOption("c", true, "la classe di modello da utilizzare");
		options.addOption("d", true, "la directory dove generare i file");
		options.addOption("s", false, "non scrive su disco: simulazione");

		System.out.println("Regola: assistente alla scrittura di codice");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) 
		{
			usage(options);
		}
		
		if (!cmd.hasOption("g") || !cmd.hasOption("c") )
			usage(options);
		
		String generatorsString = cmd.getOptionValue("g");
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
		
		
		IClassDescriptor modelDescriptor = env.getDescriptorService()
				.getClassDescriptor(Class.forName(ourOpt.getModelClass()));
		
		
		
		IPropertyDescriptor idProperty = modelDescriptor.getIdentifierDescriptor(); 
		if (idProperty==null)
		{
			System.out.println("Errore: la classe "+ modelDescriptor.getType().getCanonicalName()  +" non presenta la proprieta' id ");
			System.exit(1);
		}
		
		
		
		IClassDescriptor idDescriptor = null;
		
		if (idProperty.getPropertyType().getPackage().getName().startsWith("java.") )
		{
			idDescriptor = env.getDescriptorService()
			.getClassDescriptor(idProperty.getPropertyType());
		}
		else
		{
			idDescriptor = env.getDescriptorService()
				.getClassDescriptor(Class.forName(ourOpt.getModelClass()+"Id"));
		}
		// env.setPackageName(getPackageName(descriptor));

		ParameterBuilder pb = new ParameterBuilder(modelDescriptor,	idDescriptor);
		//env.setPackageName((String) pb.getParameters().get("package"));
		
		for (Generator generator : ourOpt.getGeneratorListByNames(generatorsString.split(","))) {
			System.out.println(String.format("Processo %s con %s",
					modelDescriptor.getDisplayName(), generator.getDisplayName()));

			generator.generate(env, pb);
		}
		
		System.out.println("Generazione completata");
	}

	private static void usage(org.apache.commons.cli.Options options) {
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar regola.jar", options);
		System.exit(0);
		
	}

}
