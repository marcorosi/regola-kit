package org.regola.service.impl;

import java.io.Serializable;
import java.util.Collection;

import org.regola.dao.MemoryGenericDao;
import org.regola.service.MemoryGenericManager;

public class MemoryGenericManagerImpl<T, PK extends Serializable> extends GenericManagerImpl<T, PK> 
				implements MemoryGenericManager<T, PK>
{

	public MemoryGenericManagerImpl(MemoryGenericDao<T, PK> memGenericDao) {
		super(memGenericDao);
	}
	
	public void setTarget(Collection<T> target) {
		((MemoryGenericDao<T, PK>)genericDao).setTarget(target);
	}
	
}
