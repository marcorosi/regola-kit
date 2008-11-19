package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;


public class FormPageGenerator extends AbstractGenerator
{
	private static final String TEMPLATE = "form_page.ftl";
	private static final String FACES_CONFIG_TEMPLATE = "formMBeanFacesContextBean.ftl";
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);	  
		env.writeWebSource(pb.getParameters().get("model_name")+"-form", template, pb.getParameters());

		template = env.getTemplate(FACES_CONFIG_TEMPLATE);
		String beanId = "/" + pb.getParameters().get("mbean_form_page");
		env.writeFacesConfig(env.getFacesConfigFileName(), beanId, template, pb.getParameters());
		
		
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  FormPageGenerator() {
		name =  "form";
	}

	public String getDisplayName() {
		return "JSF detail page";
	}
	
	public String getDescription() {
		return "Produces a facelets template for detail page.";
	}
}
