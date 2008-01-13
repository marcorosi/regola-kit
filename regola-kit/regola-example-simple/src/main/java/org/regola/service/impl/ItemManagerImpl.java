package org.regola.service.impl;

import org.regola.dao.GenericDao;
import org.regola.service.impl.GenericManagerImpl;

import org.regola.model.Invoice;
import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.service.ItemManager;

public class ItemManagerImpl extends GenericManagerImpl<Item,ItemId> implements ItemManager
{
	public ItemManagerImpl(GenericDao<Item,ItemId> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
	}

}