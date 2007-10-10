package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class FormPageGenerator implements Generator
{
	private static final String TEMPLATE = "form_page.ftl";
	//private static final String MBEAN_TEMPLATE = "formMBean.ftl";
	
	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);	  
		env.writeWebSource(pb.getParameters().get("model_name")+"-form", template, pb.getParameters());

		/*
		template = env.getTemplate(MBEAN_TEMPLATE);	  
		env.writeJavaSource((String) pb.getParameters().get("mbean_package")
				, (String) pb.getParameters().get("mbean_form_name")
				, template
				, pb.getParameters());
		*/
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "form";
	}

	public String getDisplayName() {
		return "form page";
	}
}
