package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

import freemarker.template.Template;

public class CustomManagerGenerator extends AbstractGenerator
{
	protected   String IMPL_TEMPLATE = "customManagerImpl.ftl";
	protected   String INTERFACE_TEMPLATE = "customManagerInterface.ftl";
	//protected   String SPRING_BEAN_TEMPLATE = "customManagerSpringBean.ftl";

	public  CustomManagerGenerator() {
		name =  "manager";
	}

	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(INTERFACE_TEMPLATE);	  
		env.addJavaSource((String) pb.getParameters().get("service_package")
				, (String) pb.getParameters().get("service_interface_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(IMPL_TEMPLATE);
		env.addJavaSource((String) pb.getParameters().get("service_impl_package")
				, (String) pb.getParameters().get("service_impl_name")
				, template
				, pb.getParameters());
		
		//Now we use Spring @Component annotations
//		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
//		String beanId = (String)pb.getParameters().get("service_bean_name");
//		env.writeXmlSource(env.getSpringServiceFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		
		return false;
	}

	public String getDisplayName() {
		return "Custom Manager";
	}
	
	public String getDescription() {
		return "Write interface and hibernate implementation for a Manager class.";

	}
}
