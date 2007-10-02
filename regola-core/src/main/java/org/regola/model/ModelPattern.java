package org.regola.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.LessThan;

import org.springframework.beans.BeanUtils;
import org.regola.util.Clonator;

public abstract class ModelPattern {
    
    	int pageSize = 20;

	int currentPage = 0;

	int totalItems = 0;

	List<ModelProperty> sortedColumns = new ArrayList<ModelProperty>();

	List<ModelProperty> allColumns = new ArrayList<ModelProperty>();

	List<ModelProperty> visibleColumns = new ArrayList<ModelProperty>();

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

	public List<ModelProperty> getSortedColumns() {
		return sortedColumns;
	}

	public void setSortedColumns(List<ModelProperty> propertiesFilter) {
		this.sortedColumns = propertiesFilter;
	}

	public int incCurrentPage() {
		int lastPage = getTotalItems() / getPageSize();

		if (currentPage == lastPage)
			return currentPage;
		return ++currentPage;
	}

	public int decCurrentPage() {
		if (currentPage == 0)
			return currentPage;
		return --currentPage;
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

	public List<ModelProperty> getAllColumns() {
		return allColumns;
	}

	public void setAllColumns(List<ModelProperty> allColumns) {
		this.allColumns = allColumns;
	}

	public List<ModelProperty> getVisibleColumns() {
		return visibleColumns;
	}

	public void setVisibleColumns(List<ModelProperty> visibleColumns) {
		this.visibleColumns = visibleColumns;
	}

	protected void defineColumn(String name, String prefix) {
		ModelProperty property = new ModelProperty(name, prefix);
		getAllColumns().add(property);
		getVisibleColumns().add(Clonator.clone(property));
	}

	protected void defineHiddenColumn(String name, String prefix) {
		ModelProperty property = new ModelProperty(name, prefix);
		getAllColumns().add(property);
		//getVisibleColumns().add(Clonator.clone(property));
	}

	@SuppressWarnings("unchecked")
	public PropertyDescriptor[] findPropetiesByAnnotation(Class[] annotations) {

		PropertyDescriptor descs[] = BeanUtils.getPropertyDescriptors(this.getClass());
		List<PropertyDescriptor> results = new ArrayList<PropertyDescriptor>();

		for (PropertyDescriptor desc : descs) {
			Method getter = desc.getReadMethod();

			if (getter != null) {
				   
				for (Class annotation: annotations)
				{
					if (desc.getReadMethod().isAnnotationPresent(annotation))
					{
						results.add(desc);
						break;
					}
				}
			}
		}

		PropertyDescriptor[] a ={};
		return  results.toArray(a);
	}
	
	private static Class[] filterAnnotations = {Equals.class, LessThan.class, GreaterThan.class};
	
	public String getActualFilterDescription()
	{
		StringBuilder builder = new StringBuilder();
		
		PropertyDescriptor[] properties = findPropetiesByAnnotation(filterAnnotations);
		
		for (PropertyDescriptor property: properties)
		{
			Object value = readProperty(property);
			
			if (isSet(value))
			{
				if(builder.length() > 0)
					builder.append(" - ");
				
				builder.append(property.getName()).append(": ").append(value);
			}
			
		}
		return builder.toString();
	}
	
	/** 
	 * @return true if at least one property is specified  
	 */
	public boolean isSpecified()
	{
		PropertyDescriptor[] properties = findPropetiesByAnnotation(filterAnnotations);
		
		for (PropertyDescriptor property: properties)
		{
			Object value = readProperty(property);
			if (isSet(value))
				return true;
		}
		
		return false;
	}

	private boolean isSet(Object value) 
	{
		return value!=null && (!(value instanceof String) || value.toString().length()>0);
	}
	
	public Object readProperty(PropertyDescriptor property)
	{
		try {
			return property.getReadMethod().invoke(this, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String findMethodByAnnotation(Class annotation) {

		PropertyDescriptor descs[] = BeanUtils.getPropertyDescriptors(this.getClass());

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
	 * 
	 * @return
	 */
	public String getFilterIdField() {
		return findMethodByAnnotation(Equals.class);
	}

	public String getFilterDescriptionField() {
		return findMethodByAnnotation(Equals.class);
	}
	
	public ModelPattern clone()
	{
		return Clonator.clone(this);
	}

	public void reset() {
		
		PropertyDescriptor[] properties = findPropetiesByAnnotation(filterAnnotations);
		
		for (PropertyDescriptor property: properties)
		{
			if (property.getWriteMethod()!=null)
			{
				try {
					property.getWriteMethod().invoke(this, (Object)null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
	}

	public boolean addSortedProperty(String name, Order order) {
		int index = getAllColumns().indexOf(new ModelProperty(name));
		ModelProperty property = getAllColumns().get(index);
		if (getSortedColumns().contains(property))
			return false;

		property.setOrder(order);
		getSortedColumns().add(Clonator.clone(property));

		return true;
	}

	public boolean addVisibleProperty(String name) {
		int index = getAllColumns().indexOf(new ModelProperty(name));
		ModelProperty property = getAllColumns().get(index);
		if (getVisibleColumns().contains(property))
			return false;

		getVisibleColumns().add(Clonator.clone(property));

		return true;
	}

}
