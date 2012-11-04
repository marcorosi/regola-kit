package org.regola.roo.addon.regola.activerecord;

import org.springframework.roo.model.JavaType;

public interface ActiveRecordOperations {

	/** {@inheritDoc} */
	public abstract boolean isCommandAvailable();

	/** {@inheritDoc} */
	public abstract void annotateType(JavaType javaType);

	/** {@inheritDoc} */
	public abstract void annotateAll();

}