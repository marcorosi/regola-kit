package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;


public class ListPageGenerator extends AbstractGenerator
{
	private static final String TEMPLATE = "list_page.ftl";
	

	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);	  
		env.writeWebSource(pb.getParameters().get("model_name")+"-list", template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  ListPageGenerator() {
		name =  "list";
	}

	public String getDisplayName() {
		return "JSF list page";
	}
	
	public String getDescription() {
		return "Produces a facelets template for list page.";
	}
}
