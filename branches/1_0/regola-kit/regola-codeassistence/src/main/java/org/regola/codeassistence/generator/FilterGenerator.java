package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class FilterGenerator implements Generator
{
	private static final String TEMPLATE = "filter.ftl";

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);
		env.writeJavaSource((String) pb.getParameters().get("pattern_package")
				, (String) pb.getParameters().get("filter_name")
				, template
				, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "modelPattern";
	}

	public String getDisplayName() {
		return "model pattern class";
	}
}