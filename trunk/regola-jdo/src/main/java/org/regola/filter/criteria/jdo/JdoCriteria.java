package org.regola.filter.criteria.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractCriteriaBuilder;

public class JdoCriteria extends AbstractCriteriaBuilder {

	protected static final Log log = LogFactory.getLog(JdoCriteria.class);

	protected static final String PARAM_PREFIX = "p_";

	private Query jdoQuery;

	private StringBuffer filter = new StringBuffer();

	private Map<String, Object> params = new HashMap<String, Object>();

	private int from = 0;
	private int to = Integer.MAX_VALUE;

	private List<String> orders = new ArrayList<String>();

	public JdoCriteria(Class<?> candidate, PersistenceManager pm) {
		this.jdoQuery = pm.newQuery(candidate);
	}

	private void and(String property, String op, Object param) {
		and(property, op, param, false);
	}

	private void and(String property, String op, Object param,
			boolean functionCall) {
		and(expr(property, op, param, functionCall));
	}

	private String expr(String property, String op, Object param,
			boolean functionCall) {
		StringBuilder expression = new StringBuilder();
		expression.append(property);
		if (functionCall) {
			expression.append("." + op + "(");
		} else {
			expression.append(op);
		}
		expression.append(addParameter(param));
		if (functionCall) {
			expression.append(")");
		}
		return expression.toString();
	}

	protected String addParameter(Object paramValue) {
		String paramName = PARAM_PREFIX + params.size();
		params.put(paramName, paramValue);
		return paramName;
	}

	private void and(String expression) {
		if (filter.length() > 0) {
			filter.append(" && ");
		}
		filter.append("(");
		filter.append(expression);
		filter.append(")");
	}

	public Map<String, Object> getParametersMap() {
		return params;
	}

	/**
	 * To execute with parametersMap:
	 * 
	 * List results = (List) c.getJdoQuery().execute(c.getParametersMap());
	 * 
	 * @return
	 */
	public Query getJdoQuery() {
		boolean first = true;
		StringBuffer declaredParams = new StringBuffer();
		StringBuffer ordering = new StringBuffer();
		for (String paramName : params.keySet()) {
			if (!first) {
				declaredParams.append(",");
			}
			first = false;
			// java type
			declaredParams.append(parameterClass(paramName)).append(" ")
					.append(paramName);
		}
		first = true;
		for (String order : orders) {
			if (!first) {
				ordering.append(",");
			}
			first = false;
			ordering.append(order);
		}

		jdoQuery.setFilter(filter.toString());
		jdoQuery.declareParameters(declaredParams.toString());
		jdoQuery.setRange(from, to);
		jdoQuery.setOrdering(ordering.toString());
		jdoQuery.compile();
		if (log.isDebugEnabled()) {
			log.debug("Generated JDOQL query: " + jdoQuery.toString());
		}
		return jdoQuery;
	}

	protected String parameterClass(String paramName) {
		Class<?> clazz = params.get(paramName).getClass();
		if (Collection.class.isAssignableFrom(clazz)) {
			clazz = Collection.class;
		}
		return clazz.getName();
	}

	@Override
	public void addEquals(String property, Object value) {
		and(property, "==", value);
	}

	@Override
	public void addGreaterEquals(String property, Object value) {
		and(property, ">=", value);
	}

	@Override
	public void addGreaterThan(String property, Object value) {
		and(property, ">", value);

	}

	@Override
	public void addIn(String property, Collection<?> values) {
		and(addParameter(values) + ".contains(" + property + ")");
	}

	@Override
	public void addLessEquals(String property, Object value) {
		and(property, "<=", value);

	}

	@Override
	public void addLessThan(String property, Object value) {
		and(property, "<", value);

	}

	@Override
	public void addLike(String property, String value) {
		and(property, "matches", value + ".*", true);
	}

	@Override
	public void addIlike(String property, String value) {
		and(property, "matches", "(?i)" + value + ".*", true);
	}

	@Override
	public void addNotEquals(String property, Object value) {
		and(property, "!=", value);
	}

	@Override
	public Criteria addOrder(Order order) {
		orders.add(order.getPropertyName() + " "
				+ (order.isAscending() ? "asc" : "desc"));

		return this;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.from = firstResult;
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.to = from + maxResults;
		return this;
	}

	@Override
	public void setRowCount() {
		jdoQuery.setUnique(true);
		jdoQuery.setResult("count(this)");
	}

}
