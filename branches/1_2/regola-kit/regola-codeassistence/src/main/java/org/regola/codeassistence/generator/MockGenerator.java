package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

import freemarker.template.Template;

public class MockGenerator extends AbstractGenerator {

	private static final String SPRING_BEAN_TEMPLATE = "mockSpring.ftl";


	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		
		String beanId = (String)pb.getParameters().get("mock_name");
		//env.addSpringBeansToTestApplicationContext( beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  MockGenerator() {
		name =  "springMock";
	}

	public String getDisplayName() {
		return "Spring Mock";
	}
	
	public String getDescription() {
		return "Generate a Spring Mock scaffold.";

	}
}
