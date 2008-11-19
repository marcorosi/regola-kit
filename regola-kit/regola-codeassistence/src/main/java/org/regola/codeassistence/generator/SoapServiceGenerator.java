package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;

import freemarker.template.Template;

public class SoapServiceGenerator extends CustomManagerGenerator {

	public SoapServiceGenerator()
	{
		IMPL_TEMPLATE = "manager-soap_interface.ftl" ;
		INTERFACE_TEMPLATE= "manager-soap_impl.ftl";
		name =  "soapService";
	}
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		super.generate(env, pb);
		
		Template template = env.getTemplate("manager-soap_spring.ftl");
		String beanId = (String)pb.getParameters().get("service_bean_name");
		env.writeServiceEndpoint(env.getSpringServiceFileName(), beanId, template, pb.getParameters());
	
		template = env.getTemplate("manager-soap_wssecurity.ftl");
		env.writeJavaSource((String) pb.getParameters().get("service_package")
				, pb.getParameters().get("service_interface_name") + "PasswordCallBack"
				, template 
				, pb.getParameters());
	}
	
	public String getDisplayName() {
		return "SOAP Service";
	}
	
	public String getDescription() {
		return "Write interface and hibernate implementation for a Manager class exposed by SOAP Service.";

	}
}
