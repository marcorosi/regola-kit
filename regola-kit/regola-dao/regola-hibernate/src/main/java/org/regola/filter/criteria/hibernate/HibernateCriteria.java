package org.regola.filter.criteria.hibernate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractQueryBuilder;

/**
 * @author nicola
 */
public class HibernateCriteria extends AbstractQueryBuilder {

    protected final Log log = LogFactory.getLog(getClass());
        
    private static final String ROOT = "CriteriaPseudoBuilder_ROOT";
	private Map<String, org.hibernate.Criteria> criteri;
	//private org.hibernate.Criteria criteria;

	public HibernateCriteria(org.hibernate.Criteria criteria) {
            	criteri = new HashMap<String, org.hibernate.Criteria>();
		criteri.put(ROOT, criteria);
		//this.criteria = criteria;
	}

    private org.hibernate.Criteria getCriteria(String propertyPath)
	{ 
		if(propertyPath == null)
			throw new RuntimeException("propertyPath non può essere null");
		
		if(propertyPath.indexOf(".") < 0 || propertyPath.startsWith("id."))
			return getRootCriteria();

		String path = propertyPath.substring(0,propertyPath.lastIndexOf(".")); 
		
		if(!criteri.containsKey(path))
		{
			creaCriterio(path, getRootCriteria());				
		}
		
		return criteri.get(path);
	}
        
    private void creaCriterio(String path, org.hibernate.Criteria criteria) 
	{
		if(path.indexOf(".") < 0)
		{
			criteri.put(path, criteria.createCriteria(path));
			log.info("createCriteria("+path+")");
		}
		else
		{
			String subPath = path.substring(0,path.indexOf("."));
			if(!criteri.containsKey(subPath))
			{
				creaCriterio(subPath, criteria);
			}
			String alias = path.substring(path.lastIndexOf(".")+1,path.length());
			criteri.put(path, criteri.get(subPath).createCriteria(alias));
			log.info("createCriteria("+alias+")");
		}
	}
        
    private void add(String property, Criterion exp) {
        getCriteria(property).add(exp);
    }

	private String getPropertyName(String propertyPath) 
	{
		if(propertyPath == null)
			throw new RuntimeException("propertyPath non può essere null");
		
		if(propertyPath.lastIndexOf(".") >= 0 && !propertyPath.startsWith("id."))
		{
			return propertyPath.substring(propertyPath.lastIndexOf(".")+1,propertyPath.length());
		} else
		{
			return propertyPath;
		}
	}
    	
	@Override
	public void addEquals(String property, Object value) {
		add(property, Restrictions.eq(getPropertyName(property), value));
	}

	@Override
	public void addNotEquals(String property, Object value) {
		add(property, Restrictions.ne(property, value));
	}

	@Override
	public void addGreaterThan(String property, Object value) {
		add(property, Restrictions.gt(property, value));
	}

	@Override
	public void addLessThan(String property, Object value) {
		add(property, Restrictions.lt(property, value));
	}

	@Override
	public void addLike(String property, String value) {
		add(property, Restrictions.like(property, value + "%"));
	}

	@Override
	public void addIlike(String property, String value) {
		add(property, Restrictions.ilike(property, value + "%"));
	}

	@Override
	public void addGreaterEquals(String property, Object value) {
		add(property, Restrictions.ge(property, value));
	}

	@Override
	public void addLessEquals(String property, Object value) {
		add(property, Restrictions.le(property, value));
	}

	@Override
	public void addIn(String property, Collection<?> value) {
		add(property, Restrictions.in(property, value));
	}

	@Override
	public Criteria addOrder(Order order) {
		if (order.isAscending()) {
			getRootCriteria().addOrder(org.hibernate.criterion.Order.asc(order
					.getPropertyName()));
		} else {
			getRootCriteria().addOrder(org.hibernate.criterion.Order.desc(order
					.getPropertyName()));
		}
		return this;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		getRootCriteria().setFirstResult(firstResult);
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		getRootCriteria().setMaxResults(maxResults);
		return this;
	}

	@Override
	public void setRowCount() {
		getRootCriteria().setProjection(Projections.rowCount());
	}

	public org.hibernate.Criteria getCriteria() {
		//return criteria;
                return getRootCriteria();
	}

    private org.hibernate.Criteria getRootCriteria() {
            return criteri.get(ROOT);
    }

}
