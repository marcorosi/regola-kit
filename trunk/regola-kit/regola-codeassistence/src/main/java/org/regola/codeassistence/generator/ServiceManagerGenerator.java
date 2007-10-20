package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class ServiceManagerGenerator implements Generator
{
	private static final String IMPL_TEMPLATE = "managerImpl.ftl";
	private static final String INTERFACE_TEMPLATE = "managerInterface.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "managerSpringBean.ftl";
	
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
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "service";
	}

	public String getDisplayName() {
		return "service class (business layer)";
	}
}
