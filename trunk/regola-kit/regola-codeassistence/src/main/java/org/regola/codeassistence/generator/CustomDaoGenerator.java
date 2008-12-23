package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

public class CustomDaoGenerator extends AbstractGenerator
{
	private static final String IMPL_TEMPLATE = "customDaoImpl.ftl";
	private static final String MOCK_TEMPLATE = "customDaoMock.ftl";
	private static final String INTERFACE_TEMPLATE = "customDaoInterface.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "customDaoSpringBean.ftl";

	
	public  CustomDaoGenerator() {
		name =  "dao";
	}

	public void generate(Environment env, VariablesBuilder pb)
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
		
		template = env.getTemplate(MOCK_TEMPLATE);
		env.writeJavaTestSource((String) pb.getParameters().get("dao_mock_package")
				, (String) pb.getParameters().get("dao_mock_name")
				, template
				, pb.getParameters());
		
		//Non è più necessario: usiamo le annotazioni @Component
//		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
//		String beanId = (String)pb.getParameters().get("dao_bean_name");
//		env.writeXmlSource(env.getSpringDaoFileName(), beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		
		return false;
	}

	public String getDisplayName() {
		return "Custom DAO";
	}
	
	public String getDescription() {
		return "Write interface and hibernate implementation for a DAO class.";
	}
	
	
}
