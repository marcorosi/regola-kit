package org.regola.filter.criteria.ibatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Criterion;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.Projection;
import org.regola.filter.criteria.Criterion.Operator;

public class IbatisCriteria implements Criteria {

	private List<Map<String, Object>> andConditions = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> orderBy = new ArrayList<Map<String, Object>>();
	private Map<String, Integer> limit = new HashMap<String, Integer>();

	public Map<String, Object> getQueryFilter() {
		Map<String, Object> queryFilter = new HashMap<String, Object>();
		queryFilter.put("andConditions", andConditions);
		queryFilter.put("orderBy", orderBy);
		queryFilter.put("limit", limit);
		return queryFilter;
	}

	public Criteria add(Criterion criterion) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("property", criterion.getProperty());
		Operator operator = criterion.getOperator();
		condition.put("operator", operator.toString());
		if (operator == Operator.LIKE || operator == Operator.ILIKE) {
			condition.put("value", criterion.getValue() + "%");
		} else {
			condition.put("value", criterion.getValue());
		}
		andConditions.add(condition);
		return this;
	}

	public Criteria addOrder(Order order) {
		Map<String, Object> ordering = new HashMap<String, Object>();
		ordering.put("property", order.getPropertyName());
		ordering.put("ascending", order.isAscending());
		orderBy.add(ordering);
		return this;
	}

	public Criteria setFirstResult(int firstResult) {
		limit.put("firstResult", firstResult);
		return this;
	}

	public Criteria setMaxResults(int maxResults) {
		limit.put("maxResults", maxResults);
		return this;
	}

	public Criteria setProjection(Projection projection) {
		// Nothing to do here. See Ibatis Dao's count() method.
		return this;
	}
}
