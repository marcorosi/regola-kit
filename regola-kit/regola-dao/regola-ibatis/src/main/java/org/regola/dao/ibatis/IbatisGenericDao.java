package org.regola.dao.ibatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.ibatis.IbatisCriteria;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisGenericDao<T, ID extends Serializable> extends
		SqlMapClientDaoSupport implements GenericDao<T, ID>, FinderExecutor<T> {

	private Class<T> persistentClass;

	private ModelPatternParser patternParser = new DefaultPatternParser();

	public IbatisGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return (T) getSqlMapClientTemplate().queryForObject(queryName("get"),
				id);
	}

	public boolean exists(ID id) {
		Boolean exists = (Boolean) getSqlMapClientTemplate().queryForObject(
				queryName("exists"), id);
		return Boolean.TRUE.equals(exists);
	}

	public boolean existsEntity(T entity) {
		Boolean exists = (Boolean) getSqlMapClientTemplate().queryForObject(
				queryName("existsEntity"), entity);
		return Boolean.TRUE.equals(exists);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(ModelPattern pattern) {
		IbatisCriteria criteriaBuilder = new IbatisCriteria();
		patternParser.createQuery(criteriaBuilder, pattern);
		return getSqlMapClientTemplate().queryForList(queryName("find"),
				criteriaBuilder.getQueryFilter());
	}

	public int count(ModelPattern pattern) {
		IbatisCriteria criteriaBuilder = new IbatisCriteria();
		patternParser.createCountQuery(criteriaBuilder, pattern);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				queryName("count"), criteriaBuilder.getQueryFilter());
	}

	public void remove(ID id) {
		getSqlMapClientTemplate().delete(queryName("remove"), id);
	}

	public void removeEntity(T entity) {
		getSqlMapClientTemplate().delete(queryName("removeEntity"), entity);
	}

	public T save(T entity) {
		if (existsEntity(entity)) {
			update(entity);
		} else {
			insert(entity);
		}
		return entity;
	}

	protected void insert(T entity) {
		getSqlMapClientTemplate().insert(queryName("insert"), entity);
	}

	protected void update(T entity) {
		getSqlMapClientTemplate().update(queryName("update"), entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getSqlMapClientTemplate().queryForList(queryName("getAll"));
	}

	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String finder, Object... args) {
		Object param = null;
		if (args.length == 1) {
			param = args[0];
		} else if (args.length > 1) {
			param = parametersMap(args);
		}
		return getSqlMapClientTemplate().queryForList(
				queryName(finder), param);
	}

	protected Map<String, Object> parametersMap(Object... args) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		for (int i = 0; i < args.length; i++) {
			paramsMap.put(String.valueOf(i + 1), args[i]);
		}
		return paramsMap;
	}

	protected String queryName(String query) {
		return persistentClass.getSimpleName() + "." + query;
	}
}
