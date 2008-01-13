package org.regola.service.impl;

import org.regola.dao.MemoryGenericDao;
import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.service.ItemManager;

public class ItemMemoryManagerImpl extends MemoryGenericManagerImpl<Item,ItemId> implements ItemManager
{
	public ItemMemoryManagerImpl(MemoryGenericDao<Item,ItemId> memGenericDao) {
		super(memGenericDao);
		// TODO Auto-generated constructor stub
	}

}