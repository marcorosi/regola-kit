package org.regola.dao.jdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelFilter;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

/**
 * JDO implementation for {@link org.regola.dao.GenericDao}
 * 
 * Uses Spring {@link org.springframework.orm.jdo.JdoTemplate} facility.
 * 
 * @author davide.romanini
 * 
 */
public class JdoGenericDao<T, ID extends Serializable> extends JdoDaoSupport
		implements GenericDao<T, ID> {

	private Class persistentClass;

	public JdoGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public int count(ModelFilter filter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings(value = "unchecked")
	public ID create(T object) {
		// TODO: Check that object is in transient state, otherwise throw a
		// DataAccessException (of type??)
		getJdoTemplate().makePersistent(object);
		Object id = JDOHelper.getObjectId(object);
		if( id instanceof javax.jdo.identity.SingleFieldIdentity )
		{
			return (ID) ((javax.jdo.identity.SingleFieldIdentity)id).getKeyAsObject();
		}
		return (ID)id;
	}

	public void delete(T object) {
		getJdoTemplate().deletePersistent(object);
	}

	public List<T> find(ModelFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings(value = "unchecked")
	public List<T> getAll() {
		return new ArrayList<T>(getJdoTemplate().find(persistentClass));
	}

	@SuppressWarnings(value = "unchecked")
	public T read(ID id) {
		T instance = (T) getJdoTemplate().getObjectById(persistentClass, id);
		// TODO: check if it is useless (JdoTemplate throws ORFException or not)
		if (instance == null) {
			throw new ObjectRetrievalFailureException(persistentClass, id);
		}
		return instance;
	}

	public void save(T object) {
		getJdoTemplate().makePersistent(object);
	}

	public void update(T object) {
		getJdoTemplate().makePersistent(object);
	}

}
