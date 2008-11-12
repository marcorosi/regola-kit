package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;

import freemarker.template.Template;

public class CustomManagerGenerator extends AbstractGenerator
{
	private static final String IMPL_TEMPLATE = "customManagerImpl.ftl";
	private static final String INTERFACE_TEMPLATE = "customManagerInterface.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "customManagerSpringBean.ftl";

	public  CustomManagerGenerator() {
		name =  "manager";
	}

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(INTERFACE_TEMPLATE);	  
		env.writeJavaSource((String) pb.getParameters().get("service_package")
				, (String) pb.getParameters().get("service_interface_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(IMPL_TEMPLATE);
		env.writeJavaSource((String) pb.getParameters().get("service_impl_package")
				, (String) pb.getParameters().get("service_impl_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = (String)pb.getParameters().get("service_bean_name");
		env.writeXmlSource(env.getSpringServiceFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		
		return false;
	}

	public String getDisplayName() {
		return "Custom Manager";
	}
	
	public String getDescription() {
		return "Write interface and hibernate implementation for a Manager class.";

	}
}
