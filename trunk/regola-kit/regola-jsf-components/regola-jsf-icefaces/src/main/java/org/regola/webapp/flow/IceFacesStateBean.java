package org.regola.webapp.flow;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.regola.util.Ognl;
import org.springframework.faces.model.SerializableListDataModel;

import com.icesoft.faces.component.datapaginator.PaginatorActionEvent;

@Deprecated
public class IceFacesStateBean extends StateBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected SerializableListDataModel columnList;
			
	public SerializableListDataModel getColumnList() {
		return columnList;
	}

	public void setColumnList(SerializableListDataModel columnList) {
		this.columnList = columnList;
	}
	
	public void paginatorListener(ActionEvent event) {
        PaginatorActionEvent e = (PaginatorActionEvent) event;

        if ("next".equals(e.getScrollerfacet())) {
            getModelPattern().nextPage();
        }

        if ("previous".equals(e.getScrollerfacet())) {
        	getModelPattern().previousPage();
        }

        if ("last".equals(e.getScrollerfacet())) {
        	getModelPattern().gotoLastPage();
        }

        if ("first".equals(e.getScrollerfacet())) {
        	getModelPattern().setCurrentPage(0);
        }

        if (e.getPageIndex() > 0) {
        	getModelPattern().gotoPage(e.getPageIndex() - 1);
        }

    }
	
	public String getCellValue()
	{
		if (getModelList().isRowAvailable() && columnList.isRowAvailable())
		{
			int row = getModelList().getRowIndex();
			int col = columnList.getRowIndex();

			Object value = null;
			String column = getModelPattern().getVisibleProperties().get(col).getName();
			
			if (getModelList().getRowData()!=null)
			{	
				value = Ognl.getValue(column, getModelList().getRowData());
			}

			if (value != null)
				return value.toString();

		}
		return "-";
	}



}
