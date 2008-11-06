package org.regola.webapp.flow;

import java.util.ArrayList;
import java.util.List;

import org.regola.model.ModelPattern;
import org.regola.util.Ognl;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.faces.model.SerializableListDataModel;

public class DynamicReadDataModel extends OneSelectionTrackingListDataModel {

	private static final long serialVersionUID = 1L;

    public DynamicReadDataModel(ModelPattern pattern)
    {
    	super();
    	this.pattern = pattern;
    }

    public DynamicReadDataModel(ModelPattern pattern, List list)
    {
        super(list);
        this.pattern = pattern;
    }
	
	private ModelPattern pattern;
	private SerializableListDataModel columns;
	
	public SerializableListDataModel prepareColumns()
	{
		columns = new SerializableListDataModel(pattern.getVisibleProperties());
		return columns;
	}
	
	public String getCellValue()
	{
		if (isRowAvailable() && isRowAvailable())
		{
			int row = getRowIndex();
			int col = columns.getRowIndex();

			Object value = null;
			String column = getPattern().getVisibleProperties().get(col).getName();
			
			if (getRowData()!=null)
			{	
				value = Ognl.getValue(column, getRowData());
			}

			if (value != null)
				return value.toString();

		}
		return "-";
	}


	public void setPattern(ModelPattern pattern) {
		this.pattern = pattern;
	}

	public ModelPattern getPattern() {
		return pattern;
	}

	public void setColumns(SerializableListDataModel columns) {
		this.columns = columns;
	}

	public SerializableListDataModel getColumns() {
		return columns;
	}
	
}
