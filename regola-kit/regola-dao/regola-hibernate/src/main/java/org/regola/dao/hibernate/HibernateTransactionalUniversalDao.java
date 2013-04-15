package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.regola.dao.UniversalDao;
import org.regola.dao.UniversalDao2;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateQueryBuilder;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementazione dello universal dao con metodi di scrittura marcati
 * come transazionali
 * 
 * @author marco
 *
 */
@SuppressWarnings("unchecked")
public class HibernateTransactionalUniversalDao extends HibernateUniversalDao implements UniversalDao, UniversalDao2  
{
	@Transactional(readOnly = false)
	public void remove(Class clazz, Serializable id) {
		super.remove(clazz, id);
	}

	@Transactional(readOnly = false)
	public void removeEntity(Object entity) {
		super.removeEntity(entity);
	}

	@Transactional(readOnly = false)
	public Object save(Object entity) {
		return super.save(entity);	
	}
}
