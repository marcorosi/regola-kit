package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;
import org.regola.codeassistence.Utils;


public class FlowMasterDetailsGenerator extends AbstractGenerator {
	
	public void generate(Environment env, ParameterBuilder pb)
	{
		String flowName = pb.getStringValue("flow_name");
        
		 
        env.writeFlowsResource(flowName, "list.xml", env.getTemplate("flow-list_xml.ftl"),pb.getParameters(), null);
        env.writeFlowsResource(flowName, "list.xhtml", env.getTemplate("flow-list_xhtml.ftl"),pb.getParameters(), null);
        
        env.writeFlowsResource(flowName, "form.xml", env.getTemplate("flow-form_xml.ftl"),pb.getParameters(), null);
        env.writeFlowsResource(flowName, "form.xhtml", env.getTemplate("flow-form_xhtml.ftl"),pb.getParameters(), null);
        
        env.writeFlowsResource(flowName, "beans.xml", env.getTemplate("flow-beans_xml.ftl"),pb.getParameters(), null);
    	
        env.writeFlowConfig("/WEB-INF/flows/" + flowName + "/list.xml", env.getTemplate("flow-webflow-config_xml.ftl"), pb.getParameters());
        
        //bisogna aggiungere il flusso alla lista dei flussi
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "flowMasterDetails";
	}

	public String getDisplayName() {
		return "Webflow Master/Details";
	}
	
	public String getDescription() {
		return "Produces a flow to manage master/details pages.";

	}
}
