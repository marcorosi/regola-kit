package org.regola.service;

import java.io.Serializable;
import java.util.Collection;

public interface MemoryGenericManager<T, PK extends Serializable> extends GenericManager<T, PK> 
{
	void setTarget(Collection<T> target);
}
