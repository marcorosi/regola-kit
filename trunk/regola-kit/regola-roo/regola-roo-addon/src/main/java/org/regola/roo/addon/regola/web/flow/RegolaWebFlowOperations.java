package org.regola.roo.addon.regola.web.flow;

import org.springframework.roo.model.JavaPackage;

public interface RegolaWebFlowOperations {

	/**
	 * See {@link WebFlowOperations#installWebFlow(String)}.
	 * @param javaPackage 
	 */
	public abstract void installWebFlow(String flowName, JavaPackage javaPackage);

	public abstract boolean isWebFlowInstallationPossible();

}