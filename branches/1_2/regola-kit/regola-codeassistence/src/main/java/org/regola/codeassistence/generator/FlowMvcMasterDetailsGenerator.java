package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;
import org.regola.codeassistence.Utils;


public class FlowMvcMasterDetailsGenerator extends AbstractGenerator {
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		String flowName = pb.getStringValue("flow_name");
        
		 
        env.addFlowsResource(flowName, "list.xml", env.getTemplate("flowmvc-list_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "list.jsp", env.getTemplate("flowmvc-list_jsp.ftl"),pb.getParameters(), null);
        
        env.addFlowsResource(flowName, "form.xml", env.getTemplate("flowmvc-form_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "form.jsp", env.getTemplate("flowmvc-form_jsp.ftl"),pb.getParameters(), null);
        
        env.addFlowsResource(flowName, "beans.xml", env.getTemplate("flow-beans_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "tiles-defs.xml", env.getTemplate("flowmvc-tiles-defs_xml.ftl"),pb.getParameters(), null);
        
        env.addFlowDefinition("/WEB-INF/flows/" + flowName + "/list.xml", env.getTemplate("flow-webflow-config_xml.ftl"), pb.getParameters());
        env.addTilesFlowDefinition("/WEB-INF/flows/" + flowName + "/tiles-defs.xml", env.getTemplate("flowmvc-spring-mvc-servlet_xml.ftl"), pb.getParameters());
        //bisogna aggiungere il flusso alla lista dei flussi
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  FlowMvcMasterDetailsGenerator() {
		name =  "flowMvcMasterDetails";
	}

	public String getDisplayName() {
		return "Webflow MVC Master/Details";
	}
	
	public String getDescription() {
		return "Produces a flow to manage master/details pages with Spring MVC.";

	}
}
