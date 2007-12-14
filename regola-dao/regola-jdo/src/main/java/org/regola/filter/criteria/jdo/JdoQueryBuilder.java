package org.regola.filter.criteria.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.regola.filter.criteria.impl.BaseQueryBuilder;

public class JdoQueryBuilder extends BaseQueryBuilder {

	private Query jdoQuery;
	private Map<Entity, List<String>> filtersByEntity = new HashMap<Entity, List<String>>();

	public JdoQueryBuilder(Class<?> entityClass, PersistenceManager pm) {
		super(entityClass);
		this.jdoQuery = pm.newQuery(entityClass);
	}

	/**
	 * To execute with parametersMap:
	 * 
	 * List results = (List) c.getJdoQuery().execute(c.getParameters());
	 * 
	 * @return
	 */
	public Query getJdoQuery() {
		StringBuilder jdoFilters = new StringBuilder();
		// boolean first = true;
		// if (filtersByEntity.containsKey(getRootEntity())) {
		// if (!first) {
		// joinedEntities.append(getAndOperator()).append(" ");
		// }
		// first = false;
		// for (String filter : filtersByEntity.get(getRootEntity())) {
		// joinedEntities.append(filter).append(" ");
		// }
		// }
		int entityCount = getEntities().size();
		for (int i = 0; i < entityCount; i++) {
			if (jdoFilters.length() > 0) {
				jdoFilters.append(getAndOperator()).append(" ");
			}

			StringBuilder joins = new StringBuilder();
			Entity e = getEntities().get(i);
			if (log.isDebugEnabled()) {
				log.debug("JDOQL: processing entity " + e);
			}
			if (e.getJoinedBy() != null) {
				String alias = e.getJoinedBy().getEntity().getAlias();
				if (alias.length() > 0) {
					joins.append(alias).append(".");
				}
				joins.append(e.getJoinedBy().getName());
				joins.append(".contains(");
				joins.append(e.getAlias());
				joins.append(") ");
			}

			StringBuilder filters = new StringBuilder();
			if (filtersByEntity.containsKey(e)) {
				if (log.isDebugEnabled()) {
					log.debug("JDOQL: filters by entity "
							+ filtersByEntity.get(e));
				}
				for (String filter : filtersByEntity.get(e)) {
					filters.append(filter).append(" ");
				}
			}

			if (joins.length() > 0 || filters.length() > 0) {
				jdoFilters.append("(");
				jdoFilters.append(joins);
				if (joins.length() > 0 && filters.length() > 0) {
					jdoFilters.append(getAndOperator()).append(" ");
				}
				jdoFilters.append(filters);
				jdoFilters.append(") ");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("JDO filters: " + jdoFilters);
		}

		// String filters = joinFilters();
		// if (log.isDebugEnabled()) {
		// log.debug("Filters: " + filters);
		// }
		// if (joinedEntities.length() > 0 && filters.length() > 0) {
		// joinedEntities.append(" ").append(getAndOperator()).append(" ");
		// }
		// jdoQuery.setFilter(joinedEntities + filters);
		jdoQuery.setFilter(jdoFilters.toString());

		StringBuffer declaredParams = new StringBuffer();
		boolean first = true;
		for (String paramName : getParameters().keySet()) {
			if (!first) {
				declaredParams.append(",");
			}
			first = false;
			// java type
			declaredParams.append(parameterClass(paramName)).append(" ")
					.append(paramName);
		}
		if (log.isDebugEnabled()) {
			log.debug("Declared parameters: " + declaredParams);
		}
		jdoQuery.declareParameters(declaredParams.toString());

		jdoQuery.setOrdering(joinOrderBy());

		long from = 0L;
		if (hasFirstResult()) {
			from = getFirstResult();
		}
		long to = Long.MAX_VALUE;
		if (hasMaxResults()) {
			to = from + getMaxResults();
		}
		jdoQuery.setRange(from, to);

		if (isRowCount()) {
			jdoQuery.setUnique(true);
			jdoQuery.setResult("count(this)");
		}

		jdoQuery.compile();
		return jdoQuery;
	}

	protected String parameterClass(String paramName) {
		Class<?> clazz = getParameters().get(paramName).getClass();
		if (Collection.class.isAssignableFrom(clazz)) {
			clazz = Collection.class;
		}
		return clazz.getName();
	}

	@Override
	protected void addRootEntity() {
		getEntities().add(new Entity(getEntityClass().getName(), ""));
	}

	@Override
	protected String getAndOperator() {
		return "&&";
	}

	@Override
	protected String getEqualsOperator() {
		return "==";
	}

	@Override
	protected String getNotEqualsOperator() {
		return "!=";
	}

	@Override
	protected String parameterReference(String parameter) {
		return parameter;
	}

	@Override
	public void addLike(String propertyPath, String value) {
		Property property = getProperty(propertyPath);
		addFilter(propertyReference(property)
				+ ".matches("
				+ parameterReference(newParameter(likePattern(value, ".", ".*")))
				+ ")");
		addFilterByEntity(property);
	}

	protected void addFilterByEntity(String propertyPath) {
		addFilterByEntity(getProperty(propertyPath));
	}

	protected void addFilterByEntity(Property property) {
		Entity entity = property.getEntity();
		if (!filtersByEntity.containsKey(entity)) {
			filtersByEntity.put(entity, new ArrayList<String>());
		}
		filtersByEntity.get(entity).add(
				getFilters().get(getFilters().size() - 1));
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		addFilter(propertyReference(getProperty(propertyPath))
				+ ".matches("
				+ parameterReference(newParameter(likePattern("(?i)" + value,
						".", ".*"))) + ")");
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addIn(String propertyPath, Collection<?> value) {
		addFilter(parameterReference(newParameter(value)) + ".contains("
				+ propertyReference(getProperty(propertyPath)) + ")");
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addEquals(String propertyPath, Object value) {
		super.addEquals(propertyPath, value);
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addNotEquals(String propertyPath, Object value) {
		super.addNotEquals(propertyPath, value);
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addGreaterThan(String propertyPath, Object value) {
		super.addGreaterThan(propertyPath, value);
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addGreaterEquals(String propertyPath, Object value) {
		super.addGreaterEquals(propertyPath, value);
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addLessThan(String propertyPath, Object value) {
		super.addLessThan(propertyPath, value);
		addFilterByEntity(propertyPath);
	}

	@Override
	public void addLessEquals(String propertyPath, Object value) {
		super.addLessEquals(propertyPath, value);
		addFilterByEntity(propertyPath);
	}
}
