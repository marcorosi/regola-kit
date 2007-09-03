package org.regola.dao.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.regola.dao.GenericDao;
import org.regola.filter.FilterBuilder;
import org.regola.filter.ModelFilter;
import org.regola.filter.builder.DefaultFilterBuilder;
import org.regola.filter.criteria.jpa.JpaCriteria;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class JpaGenericDao<T, ID extends Serializable> implements
		GenericDao<T, ID> {

	private EntityManager entityManager;

	private Class<T> persistentClass;

	private FilterBuilder filterBuilder = new DefaultFilterBuilder();

	private Method idGetter;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JpaGenericDao(Class<T> clazz) {
		this.persistentClass = clazz;
		// initId();
	}

	protected void initId() {
		for (Method method : persistentClass.getMethods()) {
			if (method.isAnnotationPresent(Id.class)
					|| method.isAnnotationPresent(EmbeddedId.class)) {
				idGetter = method;
				return;
			}
		}
		for (Field field : persistentClass.getFields()) {
			if (field.isAnnotationPresent(Id.class)
					|| field.isAnnotationPresent(EmbeddedId.class)) {
				try {
					idGetter = persistentClass
							.getMethod(getPropertyGetter(field.getName()));
				} catch (SecurityException e) {
					throw new InvalidDataAccessApiUsageException(
							"Could not find persistent Id accessor", e);
				} catch (NoSuchMethodException e) {
					throw new InvalidDataAccessApiUsageException(
							"Could not find persistent Id accessor", e);
				}
			}
		}
	}

	protected String getPropertyGetter(String versionProperty) {
		return "get" + Character.toUpperCase(versionProperty.charAt(0))
				+ versionProperty.substring(1);
	}

	@SuppressWarnings("unchecked")
	public ID create(T object) {
		entityManager.persist(object);
		entityManager.flush();
		// try {
		// return (ID) idGetter.invoke(object);
		// } catch (IllegalArgumentException e) {
		// throw new InvalidDataAccessApiUsageException(
		// "Error getting persistent Id", e);
		// } catch (IllegalAccessException e) {
		// throw new InvalidDataAccessApiUsageException(
		// "Error getting persistent Id", e);
		// } catch (InvocationTargetException e) {
		// throw new InvalidDataAccessApiUsageException(
		// "Error getting persistent Id", e);
		// }
		return (ID) new Integer(999);
	}

	public T read(ID id) {
		T object = entityManager.find(persistentClass, id);

		if (object == null) {
			throw new ObjectRetrievalFailureException(persistentClass, id);
		}

		return object;
	}

	public void update(T object) {
		entityManager.merge(object);
		entityManager.flush();
	}

	public void delete(T object) {
		entityManager.remove(object);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String finder, Object... args) {
		Query query = entityManager.createNamedQuery(getQueryName(finder));
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return (List<T>) query.getResultList();
	}

	protected String getQueryName(String query) {
		return persistentClass.getSimpleName() + "." + query;
	}

	public int count(ModelFilter filter) {
		JpaCriteria criteriaBuilder = new JpaCriteria(persistentClass,
				entityManager);
		getFilterBuilder().createCountFilter(criteriaBuilder, filter);
		return ((Number) criteriaBuilder.getQuery().getSingleResult())
				.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<T> find(ModelFilter filter) {
		JpaCriteria criteriaBuilder = new JpaCriteria(persistentClass,
				entityManager);
		getFilterBuilder().createFilter(criteriaBuilder, filter);
		return criteriaBuilder.getQuery().getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		JpaCriteria criteriaBuilder = new JpaCriteria(persistentClass,
				entityManager);
		getFilterBuilder().createFilter(criteriaBuilder, new ModelFilter() {
		});
		return criteriaBuilder.getQuery().getResultList();
	}

	public void save(T object) {
		entityManager.persist(object);
	}

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

}
