package org.regola.dao.ibatis;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Id;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.ibatis.IbatisCriteria;
import org.regola.filter.impl.DefaultModelPatternParser;
import org.regola.model.ModelPattern;
import org.regola.util.AnnotationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;

public class IbatisGenericDao<T, ID extends Serializable> extends
		SqlMapClientDaoSupport implements GenericDao<T, ID> {

	private Class<T> persistentClass;

	private ModelPatternParser patternParser = new DefaultModelPatternParser();

	private Probe probe;

	private String idProperty;

	public IbatisGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
		probe = ProbeFactory.getProbe(persistentClass);
		idProperty = idProperty(persistentClass);
	}

	protected String idProperty(Class<T> persistentClass) {
		Field[] fields = AnnotationUtils.findFieldsByAnnotation(
				persistentClass, Id.class);
		if (fields.length > 1) {
			throw new IllegalArgumentException("La classe " + persistentClass
					+ " contiene più campi marcati con l'annotazione \"Id\":"
					+ " chiavi primarie composte non sono supportate");
		}
		if (fields.length == 1) {
			return fields[0].getName();
		}
		Method[] methods = AnnotationUtils.findMethodsByAnnotation(
				persistentClass, Id.class);
		if (methods.length > 1) {
			throw new IllegalArgumentException("La classe " + persistentClass
					+ " contiene più metodi marcati con l'annotazione \"Id\":"
					+ " chiavi primarie composte non sono supportate");
		}
		if (methods.length == 1) {
			if (methods[0].getName().startsWith("get")
					|| methods[0].getName().startsWith("set")) {
				return methods[0].getName().substring(3);
			} else {
				throw new IllegalArgumentException("La classe "
						+ persistentClass
						+ " contiene un metodo con nome invalido marcato come"
						+ " chiave primaria con l'annotatione \"Id\"");
			}
		}
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(
				persistentClass, "id");
		if (pd != null) {
			return pd.getName();
		}
		throw new IllegalArgumentException(
				"Impossibile individuare la chiave primaria della classe "
						+ persistentClass);
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
		// if (idValue(entity) != null) {
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

	protected String queryName(String query) {
		return persistentClass.getSimpleName() + "." + query;
	}

	protected Object idValue(T entity) {
		return probe.getObject(entity, idProperty);
	}
}
