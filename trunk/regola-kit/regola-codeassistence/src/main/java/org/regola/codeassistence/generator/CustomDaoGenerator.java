package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;

public class CustomDaoGenerator extends AbstractGenerator
{
	private static final String IMPL_TEMPLATE = "customDaoImpl.ftl";
	private static final String INTERFACE_TEMPLATE = "customDaoInterface.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "customDaoSpringBean.ftl";

	public String getName() {
		return "dao";
	}

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(INTERFACE_TEMPLATE);	  
		env.writeJavaSource((String) pb.getParameters().get("dao_package")
				, (String) pb.getParameters().get("dao_interface_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(IMPL_TEMPLATE);
		env.writeJavaSource((String) pb.getParameters().get("dao_impl_package")
				, (String) pb.getParameters().get("dao_impl_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = (String)pb.getParameters().get("dao_bean_name");
		env.writeXmlSource(env.getSpringDaoFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		
		return false;
	}

	public String getDisplayName() {
		return "Custom DAO";
	}
	
	public String getDescription() {
		return "Write interface and hibernate implementation for a DAO class.";
	}
}
