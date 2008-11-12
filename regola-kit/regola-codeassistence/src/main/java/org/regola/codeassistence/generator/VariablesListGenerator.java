package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;
import org.regola.util.ValueReader;

import freemarker.template.Template;

public class VariablesListGenerator extends AbstractGenerator {

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate("variables.ftl");
		env.writeFile("","variable.txt",  template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
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
