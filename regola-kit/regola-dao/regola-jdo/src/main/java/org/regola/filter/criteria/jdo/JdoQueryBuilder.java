package org.regola.filter.criteria.jdo;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.regola.filter.criteria.impl.BaseQueryBuilder;

public class JdoQueryBuilder extends BaseQueryBuilder {

	private Query jdoQuery;

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
		StringBuilder joinedEntities = new StringBuilder();
		int entityCount = getEntities().size();
		for (int i = 1; i < entityCount; i++) {
			Entity e = getEntities().get(i);
			String alias = e.getJoinedBy().getEntity().getAlias();
			if (alias.length() > 0) {
				joinedEntities.append(alias).append(".");
			}
			joinedEntities.append(e.getJoinedBy().getName());
			joinedEntities.append(".contains(");
			joinedEntities.append(e.getAlias());
			joinedEntities.append(") ");
			if (i < entityCount - 1) {
				joinedEntities.append(getAndOperator()).append(" ");
			}
		}
		jdoQuery.setFilter(joinedEntities + joinFilters());

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
		if (log.isDebugEnabled()) {
			log.debug("Generated JDOQL query: " + jdoQuery.toString());
		}
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
	public void addLike(String propertyPath, String value) {
		addFilter(propertyReference(getProperty(propertyPath))
				+ ".matches("
				+ parameterReference(newParameter(likePattern(value, ".", ".*")))
				+ ")");
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		addFilter(propertyReference(getProperty(propertyPath))
				+ ".matches("
				+ parameterReference(newParameter(likePattern("(?i)" + value,
						".", ".*"))) + ")");
	}

	@Override
	public void addIn(String propertyPath, Collection<?> value) {
		addFilter(parameterReference(newParameter(value)) + ".contains("
				+ propertyReference(getProperty(propertyPath)) + ")");
	}
}
