package org.regola.dao;

import java.io.Serializable;
import java.util.Collection;

public interface MemoryGenericDao<T, ID extends Serializable> extends GenericDao<T,ID> 
{
	void setTarget(Collection<T> target);
}
