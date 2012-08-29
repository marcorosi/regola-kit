package org.regola.webapp.flow.mvc;

import org.springframework.webflow.mvc.portlet.AbstractFlowHandler;

/**
 * Seleziona il flowID per le portlet
 * 
 * @author nicola
 *
 */
public class ViewFlowHandler extends AbstractFlowHandler {

	private String flowId = "main";
	
    public String getFlowId() {
	return flowId;
    }

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

}
