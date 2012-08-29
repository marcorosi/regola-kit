package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;
import org.regola.codeassistence.Utils;


public class PortletGenerator extends AbstractGenerator {
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		String flowName = pb.getStringValue("portlet_name");
        
		 
        env.addFlowsResource(flowName, "config.xml", env.getTemplate("portlet-config_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "main.jsp", env.getTemplate("portlet-main_jsp.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "main.xml", env.getTemplate("portlet-main_xml.ftl"),pb.getParameters(), null);
                
        env.addWebXmlConfig(flowName , env.getTemplate("portlet-web_xml.ftl"), pb.getParameters());
        env.addPortlet(flowName , env.getTemplate("portlet-portlet_xml.ftl"), pb.getParameters());
        //bisogna aggiungere il flusso alla lista dei flussi
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  PortletGenerator() {
		name =  "portlet";
	}

	public String getDisplayName() {
		return "Portlet";
	}
	
	public String getDescription() {
		return "Produces a portlet with Spring Webflow.";

	}
}
