package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;

import freemarker.template.Template;

public class MockGenerator extends AbstractGenerator {

	private static final String SPRING_BEAN_TEMPLATE = "mockSpring.ftl";


	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		
		String beanId = (String)pb.getParameters().get("mock_name");
		env.writeXmlSource(env.getSpringTestResourcesFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "springMock";
	}

	public String getDisplayName() {
		return "Spring Mock";
	}
	
	public String getDescription() {
		return "Generate a Spring Mock scaffold.";

	}
}
