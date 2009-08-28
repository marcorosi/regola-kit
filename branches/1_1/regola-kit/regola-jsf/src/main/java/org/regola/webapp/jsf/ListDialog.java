package org.regola.webapp.jsf;

import java.io.Serializable;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.ListPage;

public class ListDialog<T, ID extends Serializable, F extends ModelPattern> extends Dialog 
{
	private ListPage<T, ID, F> listPage;

	public ListPage<T, ID, F> getListPage() {
		return listPage;
	}

	public void setListPage(ListPage<T, ID, F> listPage) {
		this.listPage = listPage;
	}
	
	public F getFilter()
	{
		return listPage.getFilter();
	}
	
	public void setFilter(F filter)
	{
		this.listPage.setFilter(filter);
	}
}
