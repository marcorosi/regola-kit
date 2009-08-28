package org.regola.dao.hibernate;

import org.regola.dao.hibernate.HibernateGenericDao;

import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.dao.ItemDao;

public class ItemDaoHibernate extends HibernateGenericDao<Item,ItemId> implements ItemDao
{
	public ItemDaoHibernate(Class<Item> persistentClass) 
	{
		super(persistentClass);
	}
}