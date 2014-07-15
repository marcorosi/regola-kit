package org.regola.roo.addon.regola.project;

public interface ProjectOperation {

	/** {@inheritDoc} */
	public abstract boolean isCommandAvailable();

	public abstract void setup(String projectName);

}