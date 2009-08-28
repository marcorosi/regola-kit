package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class FormManagedBeanGenerator implements Generator
{
	private static final String LIST_TEMPLATE = "formMBean.ftl";
	private static final String FACES_CONTEXT_TEMPLATE = "formMBeanFacesContextBean.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "formMBeanSpringBean.ftl";
	private static final String VALIDATION_TEMPLATE = "formMBeanValidation.ftl";
        
	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(LIST_TEMPLATE);	  
		env.writeJavaSource((String) pb.getParameters().get("mbean_package")
				, (String) pb.getParameters().get("mbean_form_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = ParameterBuilder.camelNotation((String)pb.getParameters().get("mbean_form_name"));
		env.writeXmlSource(env.getSpringServiceFileName(), beanId, template, pb.getParameters());
		
                
        template = env.getTemplate(VALIDATION_TEMPLATE);
		env.writeResource( pb.getParameters().get("mbean_form_name") + "Amendments.xml", template,  pb.getParameters());
		
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "form-handler";
	}

	public String getDisplayName() {

		return "jsf managed bean";
	}
}
