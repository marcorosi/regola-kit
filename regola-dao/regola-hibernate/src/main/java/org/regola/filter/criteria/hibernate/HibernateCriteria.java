package org.regola.filter.criteria.hibernate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractCriteriaBuilder;

public class HibernateCriteria<T> extends AbstractCriteriaBuilder {

	private static final Log log = LogFactory.getLog(HibernateCriteria.class);
	private static final String ROOT = "CriteriaPseudoBuilder_ROOT";
	private Map<String, org.hibernate.Criteria> criteriaMap;

	class CriteriaBuilder {
		String propertyPath;
		org.hibernate.Criteria criteria;

		public CriteriaBuilder(String propertyPath,
				org.hibernate.Criteria criteria) {
			this.propertyPath = propertyPath;
			this.criteria = criteria;
		}

		public void eq(Object value) {
			criteria.add(Restrictions.eq(propertyPath, value));
		}

		public void ne(Object value) {
			criteria.add(Restrictions.ne(propertyPath, value));
		}

		public void gt(Object value) {
			criteria.add(Restrictions.gt(propertyPath, value));
		}

		public void lt(Object value) {
			criteria.add(Restrictions.lt(propertyPath, value));
		}

		public void like(Object value) {
			criteria.add(Restrictions.like(propertyPath, value));
		}

		public void ilike(Object value) {
			criteria.add(Restrictions.ilike(propertyPath, value));
		}

		public void ge(Object value) {
			criteria.add(Restrictions.ge(propertyPath, value));
		}

		public void le(Object value) {
			criteria.add(Restrictions.le(propertyPath, value));
		}

		public void in(Collection<?> value) {
			criteria.add(Restrictions.in(propertyPath, value));
		}
	}

	/*
	 * public HibernateCriteria(org.hibernate.Criteria criteria) { criteriaMap =
	 * new HashMap<String, org.hibernate.Criteria>(); criteriaMap.put(ROOT,
	 * criteria); }
	 */

	private ClassMetadata classMetadata;

	private SessionFactory sessionFactory;

	public HibernateCriteria(Session session, Class<T> persistentClass,
			SessionFactory sessionFactory) {
		criteriaMap = new HashMap<String, org.hibernate.Criteria>();
		criteriaMap.put(ROOT, session.createCriteria(persistentClass));
		classMetadata = sessionFactory.getClassMetadata(persistentClass);
		this.sessionFactory = sessionFactory;
		if (log.isDebugEnabled()) {
			log.debug("Initializing " + this.getClass() + " for "
					+ persistentClass);
			for (String s : classMetadata.getPropertyNames()) {
				Type t = classMetadata.getPropertyType(s);
				log.debug(String.format(
						"property name: %s type: %s isAssociation: %s", s, t
								.getName(), t.isAssociationType()));
			}
		}
	}

	private CriteriaBuilder getCriteria(String path) {
		if (path == null) {
			throw new RuntimeException("propertyPath non può essere null");
		}

		String propertyPath = path;
		ClassMetadata metadata = classMetadata;

		StringBuilder fullPath = new StringBuilder();
		org.hibernate.Criteria criteria = getRootCriteria();
		if (propertyPath.contains(".")) {
			String[] paths = propertyPath.split("\\.");
			for (int i = 0; i < paths.length; i++) {
				propertyPath = paths[i];
				if (i < paths.length - 1) {
					fullPath.append(paths[i]);
					// Type type =
					// classMetadata.getPropertyType(fullPath.toString());
					Type type = metadata.getPropertyType(paths[i]);
					if (type.isAssociationType()) {
						if (criteriaMap.containsKey(fullPath)) {
							criteria = criteriaMap.get(fullPath);
						} else {
							criteria = criteria.createCriteria(paths[i]);
							criteriaMap.put(fullPath.toString(), criteria);
							metadata = sessionFactory
									.getClassMetadata(((AssociationType) type)
											.getAssociatedEntityName((SessionFactoryImplementor) sessionFactory));
						}
						fullPath.append(".");
						path = paths[i + 1];
					}
				}
			}
		}
		log.debug("getCriteria(): path=" + path + ", fullPath=" + fullPath);

		return new CriteriaBuilder(path, criteria);
	}

	@Override
	public void addEquals(String property, Object value) {
		getCriteria(property).eq(value);
	}

	@Override
	public void addNotEquals(String property, Object value) {
		getCriteria(property).ne(value);
	}

	@Override
	public void addGreaterThan(String property, Object value) {
		getCriteria(property).gt(value);
	}

	@Override
	public void addLessThan(String property, Object value) {
		getCriteria(property).lt(value);
	}

	@Override
	public void addLike(String property, String value) {
		getCriteria(property).like(value + "%");
	}

	@Override
	public void addIlike(String property, String value) {
		getCriteria(property).ilike(value + "%");
	}

	@Override
	public void addGreaterEquals(String property, Object value) {
		getCriteria(property).ge(value);
	}

	@Override
	public void addLessEquals(String property, Object value) {
		getCriteria(property).le(value);
	}

	@Override
	public void addIn(String property, Collection<?> value) {
		getCriteria(property).in(value);
	}

	@Override
	public Criteria addOrder(Order order) {
		if (order.isAscending()) {
			getRootCriteria().addOrder(
					org.hibernate.criterion.Order.asc(order.getPropertyName()));
		} else {
			getRootCriteria()
					.addOrder(
							org.hibernate.criterion.Order.desc(order
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
		// return criteria;
		return getRootCriteria();
	}

	private org.hibernate.Criteria getRootCriteria() {
		return criteriaMap.get(ROOT);
	}

}
