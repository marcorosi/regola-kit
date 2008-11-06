package org.regola.model;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.LessThan;
import org.regola.util.Clonator;
import org.springframework.beans.BeanUtils;

public abstract class ModelPattern implements Serializable{

	int pageSize = 20;
	boolean pagingEnabled = true;
	int currentPage = 0;
	int totalItems = 0;

	List<ModelProperty> sortedProperties = new ArrayList<ModelProperty>();
	List<ModelProperty> allProperties = new ArrayList<ModelProperty>();
	List<ModelProperty> visibleProperties = new ArrayList<ModelProperty>();

	public boolean isPagingEnabled() {
		return pagingEnabled;
	}

	public void setPagingEnabled(boolean pagingEnabled) {
		this.pagingEnabled = pagingEnabled;
	}

	public void disablePaging() {
		setPagingEnabled(false);
	}

	public void init(Object model) {
		// override this to initialize filter fields with a model object
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public List<ModelProperty> getSortedProperties() {
		return sortedProperties;
	}

	public void setSortedProperties(List<ModelProperty> properties) {
		this.sortedProperties = properties;
	}

	public int nextPage() {
		int lastPage = getTotalItems() / getPageSize();

		if (currentPage == lastPage) {
			return currentPage;
		}

		currentPage += 1;

		return currentPage;
	}

	public int previousPage() {
		if (currentPage == 0) {
			return currentPage;
		}

		currentPage -= 1;

		return currentPage;
	}
	
	public int getLastPage()
	{
		int lastPage = getTotalItems() / getPageSize();
		return lastPage;
	}

	public int gotoLastPage() {
		int lastPage = getTotalItems() / getPageSize();
		setCurrentPage(lastPage);

		return lastPage;
	}

	public int gotoPage(int page) {
		int lastPage = getTotalItems() / getPageSize();

		if (page > lastPage)
			page = lastPage;

		if (page < 0)
			page = 0;

		setCurrentPage(page);
		return page;
	}

	public List<ModelProperty> getAllProperties() {
		return allProperties;
	}

	public void setAllProperties(List<ModelProperty> allProperties) {
		this.allProperties = allProperties;
	}

	public List<ModelProperty> getVisibleProperties() {
		return visibleProperties;
	}

	public void setVisibleProperties(List<ModelProperty> visibleProperties) {
		this.visibleProperties = visibleProperties;
	}

	protected void addProperty(String name, String prefix) {
		ModelProperty property = new ModelProperty(name, prefix);
		getAllProperties().add(property);
		getVisibleProperties().add(Clonator.clone(property));
	}

	protected void addHiddenColumn(String name, String prefix) {
		ModelProperty property = new ModelProperty(name, prefix);
		getAllProperties().add(property);
		// getVisibleProperties().add(Clonator.clone(property));
	}

	public PropertyDescriptor[] findPropertiesByAnnotation(
			List<Class<? extends Annotation>> annotations) {

		PropertyDescriptor descs[] = BeanUtils.getPropertyDescriptors(this
				.getClass());
		List<PropertyDescriptor> results = new ArrayList<PropertyDescriptor>();

		for (PropertyDescriptor desc : descs) {
			Method getter = desc.getReadMethod();

			if (getter != null) {

				for (Class<? extends Annotation> annotation : annotations) {
					if (desc.getReadMethod().isAnnotationPresent(annotation)) {
						results.add(desc);
						break;
					}
				}
			}
		}

		PropertyDescriptor[] a = {};
		return results.toArray(a);
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends Annotation>> filterAnnotations = Arrays
			.asList(Equals.class, LessThan.class, GreaterThan.class);

	public String getCurrentFilterDescription() {
		StringBuilder builder = new StringBuilder();

		PropertyDescriptor[] properties = findPropertiesByAnnotation(filterAnnotations);

		for (PropertyDescriptor property : properties) {
			Object value = readProperty(property);

			if (isSet(value)) {
				if (builder.length() > 0)
					builder.append(" - ");

				builder.append(property.getName()).append(": ").append(value);
			}

		}
		return builder.toString();
	}

	/**
	 * @return true if at least one property is specified
	 */
	public boolean isSpecified() {
		PropertyDescriptor[] properties = findPropertiesByAnnotation(filterAnnotations);

		for (PropertyDescriptor property : properties) {
			Object value = readProperty(property);
			if (isSet(value))
				return true;
		}

		return false;
	}

	private boolean isSet(Object value) {
		return value != null
				&& (!(value instanceof String) || value.toString().length() > 0);
	}

	public Object readProperty(PropertyDescriptor property) {
		try {
			return property.getReadMethod().invoke(this, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String findMethodByAnnotation(Class<? extends Annotation> annotation) {

		PropertyDescriptor descs[] = BeanUtils.getPropertyDescriptors(this
				.getClass());

		for (PropertyDescriptor desc : descs) {
			Method getter = desc.getReadMethod();

			if (getter != null
					&& desc.getReadMethod().isAnnotationPresent(annotation)) {
				if (desc.getPropertyType() != String.class) {
					String msg = String
							.format(
									"Il getter annotation nel filtro deve restituire una stringa. (%s,%s) ",
									this.getClass().getName(), getter.getName());
					throw new RuntimeException(msg);
				}

				try {
					return (String) getter.invoke(this, (Object[]) null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return null;
	}

	/**
	 * Restituisce la proprietà del filtro che identifica la chiave primaria
	 */
	public String getFilterIdField() {
		return findMethodByAnnotation(Equals.class);
	}

	public String getFilterDescriptionField() {
		return findMethodByAnnotation(Equals.class);
	}

	public ModelPattern clone() {
		return Clonator.clone(this);
	}

	public void reset() {

		PropertyDescriptor[] properties = findPropertiesByAnnotation(filterAnnotations);

		for (PropertyDescriptor property : properties) {
			if (property.getWriteMethod() != null) {
				try {
					property.getWriteMethod().invoke(this, (Object) null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	/**
	 * 
	 * @param name
	 * @param order
	 * @return
	 * @throws PropertyNotFoundException if the property cannot be found in this model pattern 
	 */
	public boolean addSortedProperty(String name, Order order) {
		int index = getAllProperties().indexOf(new ModelProperty(name));
		if(index < 0)
			throw new PropertyNotFoundException(name,this);
		
		ModelProperty property = getAllProperties().get(index);
		if (getSortedProperties().contains(property))
			return false;

		property.setOrder(order);
		getSortedProperties().add(Clonator.clone(property));

		return true;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws PropertyNotFoundException if the property cannot be found in this model pattern
	 */
	public boolean addVisibleProperty(String name) {
		int index = getAllProperties().indexOf(new ModelProperty(name));
		if(index < 0)
			throw new PropertyNotFoundException(name,this);

		ModelProperty property = getAllProperties().get(index);
		if (getVisibleProperties().contains(property))
			return false;

		getVisibleProperties().add(Clonator.clone(property));

		return true;
	}

}
