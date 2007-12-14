package org.regola.webapp.action.lookup;

import org.regola.service.GenericManager;
import org.regola.util.Ognl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import org.regola.model.ModelPattern;

public class BaseLookupStrategy<T, ID extends Serializable, F extends ModelPattern>
		implements LookupStrategy<T, ID, F> {

	private static final int DEFAULT_MAX_ROWS = 10;

	private F filter;
	
	// the name of the id field in the filter
	private String filterIdField = "id";
	
	// the name of the search field in the filter
	private String filterDescriptionField = "description";

	// the name of the id field in the model
	// private String modelIdField = "id";
	// the name of the search field in the model
	// private String modelDescriptionField = "description";
	// maxrow to show in the autocompletition
	private int maxRows = DEFAULT_MAX_ROWS;

	private GenericManager<T, ID> serviceManager;

	public F getFilter() {
		return filter;
	}

	public List<SelectItem> getList() {
		return new ArrayList<SelectItem>();
	}

	public List<T> getSelection() {
		return new ArrayList<T>();
	}

	public void init(T model, F filter, GenericManager<T, ID> manager) {

		setFilter(filter);
		setServiceManager(manager);

		getFilter().init(model);
		getFilter().setPageSize(getMaxRows());
	}

	public void setFilterDescription(String description) {
		Ognl.setValue(getFilterDescriptionField(), filter, description);
	}

	public String getFilterDescription() {
		Object value = Ognl.getValue(getFilterDescriptionField(), filter);
		return (value != null) ? value.toString() : null;
	}
	
	public void setFilterId(String id) {
		Ognl.setValue(getFilterIdField(), filter, id);
	}
	
	public String getFilterId() {
		Object value = Ognl.getValue(getFilterIdField(), filter);
		return (value != null) ? value.toString() : null;
	}

	public GenericManager<T, ID> getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(GenericManager<T, ID> serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getFilterDescriptionField() {
		return filterDescriptionField;
	}

	public void setFilterDescriptionField(String filterDescription) {
		this.filterDescriptionField = filterDescription;
	}

	public void setFilter(F filter) {
		this.filter = filter;
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	
	/**
	 * @return the label for the SelectItem object. To work properly with
	 *         IceFaces this must be a unique identifier of the model
	 */
	public String getSelectLabel(T model) {
		return Ognl.getValue("id", model).toString() + "-"
				+ getFilterDescription();
	}

	public String getFilterIdField() {
		return filterIdField;
	}

	public void setFilterIdField(String filterIdField) {
		this.filterIdField = filterIdField;
	}
}
