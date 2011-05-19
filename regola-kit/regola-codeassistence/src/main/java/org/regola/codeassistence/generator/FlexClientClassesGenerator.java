package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

import freemarker.template.Template;

public class FlexClientClassesGenerator extends AbstractGenerator {
	
	public  FlexClientClassesGenerator() {
		name =  "flex-client";
	}
	
	private static final String MODEL_PATTERN = "flex-filter.ftl";
	private static final String MODEL = "flex-model.ftl";
	private static final String CONTROLLER = "flex-controller.ftl";
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(MODEL_PATTERN);
		env.writeFlexSource((String) pb.getParameters().get("pattern_package")
				, (String) pb.getParameters().get("filter_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(MODEL);
		env.writeFlexSource((String) pb.getParameters().get("model_package")
				, (String) pb.getParameters().get("model_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(CONTROLLER);	  
		env.writeFlexSource((String) pb.getParameters().get("controller_package")
				, (String) pb.getParameters().get("controller_name")
				, template
				, pb.getParameters());
	}
	
	
	public boolean existsArtifact(Environment env, VariablesBuilder pb) {

		return false;
	}

	public String getDisplayName() {
		return "Flex Client Classes";
	}

	public String getDescription() {
		return "Create Model, ModelPattern and Controller as3 classes.";

	}
}
