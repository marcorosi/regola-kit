package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class ListManagedBeanGenerator implements Generator
{
	private static final String LIST_TEMPLATE = "listMBean.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "listMBeanSpringBean.ftl";

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(LIST_TEMPLATE);	  
		env.writeJavaSource((String) pb.getParameters().get("mbean_package")
				, (String) pb.getParameters().get("mbean_list_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = ParameterBuilder.camelNotation((String)pb.getParameters().get("mbean_list_name"));
		env.writeXmlSource(env.getSpringServiceFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "list-handler";
	}

	public String getDisplayName() {

		return "jsf managed bean";
	}
}
