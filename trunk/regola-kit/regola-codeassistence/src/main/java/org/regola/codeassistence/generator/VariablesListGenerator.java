package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;
import org.regola.util.ValueReader;

import freemarker.template.Template;

public class VariablesListGenerator extends AbstractGenerator {

	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate("variables.ftl");
		env.writeFile("","variable.txt",  template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  VariablesListGenerator() {
		name =  "variablesList";
	}

	public String getDisplayName() {
		return "Variable List";
	}
	
	public String getDescription() {
		return "Produces a of variables used by code assistence template.";

	}
}
