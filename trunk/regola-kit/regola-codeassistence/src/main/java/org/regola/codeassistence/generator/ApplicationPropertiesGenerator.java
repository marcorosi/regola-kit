package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class ApplicationPropertiesGenerator extends AbstractGenerator
{
	private static final String TEMPLATE = "application_properties.ftl";

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);
		//String fileName = pb.getParameters().get("model_name").toString().toLowerCase();
		//env.writeResource(fileName+".properties", template, pb.getParameters());
		env.writeApplicationProperties(template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}

	public ApplicationPropertiesGenerator() {
		name = "properties";
	}

	public String getDisplayName() {
		return "Application Localized Bundles";
	}
	
	public String getDescription() {
		return "Write application's resource boundles.";
	}
}
