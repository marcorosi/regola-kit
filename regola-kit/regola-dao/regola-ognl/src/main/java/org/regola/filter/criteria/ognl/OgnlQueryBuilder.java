package org.regola.filter.criteria.ognl;

import static org.regola.util.Ognl.getValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractCriteriaBuilder;

public class OgnlQueryBuilder extends AbstractCriteriaBuilder {

	class ListContext {
		protected List<String> filters = new ArrayList<String>();
		protected List<ListContext> listContexts = new ArrayList<ListContext>();
		protected String path;

		public ListContext(String path) {
			this.path = path;
		}

		public ListContext findListContext(String path) {
			for (ListContext context : listContexts) {
				if (context.getPath().equals(path))
					return context;
			}

			return null;
		}
		
		public boolean isEmpty()
		{
			return filters.size()==0 && listContexts.size()==0;
		}

		public List<String> getFilters() {
			return filters;
		}

		public ListContext getListContext(String path) {
			ListContext context = findListContext(path);

			if (context == null) {
				context = new ListContext(path);
				listContexts.add(context);
			}

			return context;

		}

		public List<ListContext> getListContexts() {
			return listContexts;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

	}

	class Property {
		protected ListContext listContext;
		protected String name;

		Property(String name, ListContext listContext) {
			this.name = name;
			this.listContext = listContext;
		}

		public ListContext getListContext() {
			return listContext;
		}

		public String getName() {
			return name;
		}

		public void setListContext(ListContext listContext) {
			this.listContext = listContext;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static String likePattern(String value) {

		return value.replace(".", "_").replace("*", ".*");
	}

	DynamicComparator comparator = new DynamicComparator();

	private int firstResult = -1;

	protected final Log log = LogFactory.getLog(getClass());

	private int maxResults = -1;

	protected ListContext rootListContext;

	private boolean rowCount = false;

	public OgnlQueryBuilder() {
		rootListContext = new ListContext("#root");
	}

	@Override
	public void addEquals(String propertyPath, Object value) {

		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " == " 
				 + rightValue(value));
	}
	@Override
	public void addGreaterEquals(String propertyPath, Object value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " >= " + rightValue(value));
	}
	@Override
	public void addGreaterThan(String propertyPath, Object value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " > " + rightValue(value));
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + ".toLowerCase().matches("
				+ rightValue(likePattern(value.toLowerCase())) + ")");
	}

	@Override
	public void addIn(String propertyPath, Collection<?> value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " in {"
				+ parametersList(value) + "}");
	}

	@Override
	public void addNotIn(String propertyPath, Collection<?> value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " not in {"
				+ parametersList(value) + "}");
	}
	
	@Override
	public void addLessEquals(String propertyPath, Object value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " <= " + rightValue(value));
	}

	@Override
	public void addLessThan(String propertyPath, Object value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " < " + rightValue(value));
	}

	@Override
	public void addLike(String propertyPath, String value) {
		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + ".matches("
				+ rightValue(likePattern(value)) + ")");
	}

	@Override
	public void addNotEquals(String propertyPath, Object value) {

		Property property = buildProperty(propertyPath);
		List<String> filter = property.getListContext().getFilters();

		filter.add(propertyReference(property) + " " + getNotEqualsOperator()
				+ " " + rightValue(value));
	}
	
	@Override
	public void addIsNull(String property) {
		//TODO
	}	
	
	@Override
	public void addIsNotNull(String property) {
		//TODO
	}			

	@Override
	public Criteria addOrder(Order order) {

		comparator.getProperties().add(order);

		return this;
	}

	protected Property buildProperty(String propertyPath) {

		String[] paths = propertyPath.split("\\[\\].?");
		ListContext listContext = getRootListContext();

		Property property = null;
		for (int i = 0; i < paths.length; i++) {

			String path = paths[i];

			if (i > 0) {
				listContext = listContext.getListContext(paths[i - 1]);
			}

			property = new Property(path, listContext);

		}
		return property;
	}

	/**
	 * Ricorsivamente costruisce la query Ognl
	 * @param context
	 * @param query
	 */
	protected void buildQueryString(ListContext context, StringBuilder query) {

		int count = context.getFilters().size();
		if (count > 0) {
			query.append(" ");
			query.append(joinFilters(context));
		}

		for (ListContext childContext : context.getListContexts()) {
			if (count > 0)
				query.append(" and ");
			query.append(" #this.");
			query.append(childContext.getPath());
			query.append(".{^ ");

			buildQueryString(childContext, query);

			query.append("}.size>0");
		}

	}

	@SuppressWarnings("unchecked")
	public Object executeQuery(Object target) {
		
		if (getRootListContext().isEmpty())
		{
			if (isRowCount()) {
				if (target instanceof Collection)
				{
					return ((Collection) target).size();
				} return 1;
			}
			
			//else return target; //bug: in questo caso non viene mai fatto l'ordinamento
			//fix
			else 
			{
				Collection targetCollection = (Collection)target;
				/*
				 * target potrebbe essere anche di tipo Set, e un cating a List fallirebbe
				 * (ad esempio persistentSet di Hibernate)
				 * N.B. Si perde il reference: ma cmq lo si perderebbe in caso di filtraggi
				 * con la getValue di ognl.
				 */
				List targetList = new ArrayList(targetCollection);
				Collections.sort(targetList, comparator);
				return targetList;				
				/*
				//Collections.sort((List)target, comparator);
				return target;
				*/	
			}
			
		}
		
		String ognl = buildQueryString();	
		Object result = getValue(ognl, target);

		if (isRowCount()) {
			return result;
		}

		List list = (List) result;

		Collections.sort(list, comparator);

		if (hasFirstResult()) {

			// forse è zero-offset?
			if (list.size() >= getFirstResult()) {
				
				for (int i = 0; i++ < getFirstResult(); list.remove(0))
					;
							
			} else
				list.clear();

		}
		if (hasMaxResults()) {

			if (list.size() > getMaxResults()) {
				
				for (int i = 0; i++ < getMaxResults(); list
						.remove(getMaxResults()))
					;
			;
			}

		}

		return result;

	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	protected String getNotEqualsOperator() {
		return "!=";
	}

	public String buildQueryString() {

		StringBuilder query = new StringBuilder();

		query.append("#root.{? ");

		buildQueryString(getRootListContext(), query);
		query.append("}");

		if (isRowCount()) {
			query.append(".size");
		}

		String ognl = query.toString();

		return ognl;

	}

	public ListContext getRootListContext() {
		return rootListContext;
	}

	protected boolean hasFirstResult() {
		return firstResult != -1;
	}

	protected boolean hasMaxResults() {
		return maxResults != -1;
	}

	public boolean isRowCount() {
		return rowCount;
	}

	protected String joinFilters(ListContext context) {
		StringBuilder text = new StringBuilder();
		int count = context.getFilters().size();
		for (int i = 0; i < count; i++) {
			// text.append("(");
			text.append(context.getFilters().get(i));
			// text.append(") ");
			if (i < count - 1) {
				text.append(" and ");
			}
		}
		return text.toString();
	}

	private String parametersList(Collection<?> values) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		for (Object value : values) {
			if (!first) {
				text.append(", ");
			}
			first = false;
			text.append(rightValue(value));
		}
		return text.toString();
	}

	protected String propertyReference(Property property) {

		return "#this." + property.getName();
	}

	protected String rightValue(Object value) {
		if (value == null)
			return "null";

		if(value instanceof Enum)
			return "@"+value.getClass().getCanonicalName()+"@"+value;
		
		if (value instanceof String)
			return "\"" + value + "\"";

		return value.toString();
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	@Override
	public void setRowCount() {
		rowCount = true;
	}

}
